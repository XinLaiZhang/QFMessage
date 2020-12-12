package top.fluffcotton.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fluffcotton.listen.CommandListen;
import top.fluffcotton.listen.handle.CommandHandle;
import top.fluffcotton.pojo.CommandHttp;
import top.fluffcotton.pojo.JsonCode;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.service.GroupSetting;

import java.util.*;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CommandControl
 * @Description: 命令控制任务
 * @date
 */
@RestController
@RequestMapping("/user")
public class CommandControl {

    /**
     * 群组设置
     */
    @Autowired
    private GroupSetting groupSetting;

    /**
     * 获取命令设置
     *
     * @param groupId 群号
     * @return {
     * code:200成功
     * data:命令的数据
     * }
     * @Title getCommand
     * @Description 获取命令设置
     * @author 张逸辰
     * @Date 2020/8/30 22:21
     */
    @RequestMapping("/getCommand")
    public Map<String, Object> getCommand(String[] groupId) {
        Map<String, Object> map = new HashMap<>(5);
        if (groupId != null && groupId.length > 0) {
            List<CommandHttp> commandHttpList = new ArrayList<>();
            //添加命令列表
            List<CommandHandle> commandHandleList = CommandListen.getCommandHandleList();
            for (CommandHandle commandHandle : commandHandleList) {
                commandHttpList.add(new CommandHttp(commandHandle));
            }
            commandHttpList.sort(Comparator.comparing(CommandHttp::getCommandId));
            //遍历获取命令id和权限
            Map<String, Integer> commands = new HashMap<>(commandHandleList.size());
            Map<String, List<String>> powers = new HashMap<>(commandHandleList.size());
            for (String g : groupId) {
                QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                List<List<Object>> lists = groupSetting.selectCommand(qqGroup);
                for (List<Object> list : lists) {
                    String commandId = (String) list.get(0);
                    List<String> power = (List<String>) list.get(1);
                    if (commands.containsKey(commandId)) {
                        commands.put(commandId, commands.get(commandId) + 1);
                        //合并相同权限
                        List<String> newPower = new ArrayList<>();
                        List<String> oldPower = powers.get(commandId);
                        for (String p : oldPower) {
                            if (power.contains(p)) {
                                newPower.add(p);
                            }
                        }
                        powers.put(commandId, newPower);
                    } else {
                        commands.put(commandId, 1);
                        powers.put(commandId, power);
                    }
                }
            }
            //遍历获取通用设置
            Set<String> keySet = commands.keySet();
            int i = 0;
            for (String key : keySet) {
                if (commands.get(key) == groupId.length) {
                    for (; i < commandHttpList.size(); i++) {
                        if (commandHttpList.get(i).getCommandId().equals(key)) {
                            commandHttpList.get(i).setOpen(true);
                            commandHttpList.get(i).setPower(powers.get(key));
                            break;
                        }
                    }
                } else {
                    for (; i < commandHttpList.size(); i++) {
                        if (commandHttpList.get(i).getCommandId().equals(key)) {
                            commandHttpList.get(i).setOpen(false);
                            break;
                        }
                    }
                }
            }
            map.put("code", JsonCode.SUCCESS.toString());
            map.put("data", commandHttpList);
        }
        return map;
    }

    /**
     * 更新命令设置
     *
     * @param groupId      群id
     * @param commandHttps 群设置
     * @return {
     * code:200成功
     * }
     * @Title updateCommand
     * @Description 更新命令设置
     * @author 张逸辰
     * @Date 2020/8/30 22:30
     */
    @RequestMapping("/updateCommand")
    public Map<String, Object> updateCommand(String[] groupId, String commandHttps) {
        Map<String, Object> map = new HashMap<>(5);
        if (groupId != null && groupId.length > 0) {
            List<CommandHttp> commandHttpList = JSON.parseArray(commandHttps, CommandHttp.class);
            for (CommandHttp commandHttp : commandHttpList) {
                if (commandHttp.isOpen()) {
                    //开启
                    for (String g : groupId) {
                        QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                        groupSetting.addCommand(qqGroup, commandHttp.getCommandId(), commandHttp.getPower());
                    }
                } else {
                    //关闭
                    for (String g : groupId) {
                        QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                        groupSetting.deleteCommand(qqGroup, commandHttp.getCommandId());
                    }
                }
            }
            map.put("code", JsonCode.SUCCESS.toString());
        }
        return map;
    }


}
