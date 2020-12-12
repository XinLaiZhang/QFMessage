package top.fluffcotton.service.impl;

import com.alibaba.fastjson.JSON;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.fluffcotton.mapper.*;
import top.fluffcotton.pojo.*;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.GroupSetting;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupSettingImpl
 * @Description: 群组设置实现类
 * @date 2020.08.27 21:10
 */
@Component
public class GroupSettingImpl implements GroupSetting {
    /**
     * 查询群组
     */
    @Autowired
    private QqGroupMapper qqGroupMapper;

    /**
     * 绑定群组与用户
     */
    @Autowired
    private GroupUserMapper groupUserMapper;

    /**
     * 获取用户的群组
     */
    @Autowired
    private GroupFolderMapper groupFolderMapper;

    /**
     * 获取命令的
     */
    @Autowired
    private CommandMapper commandMapper;

    /**
     * 修改设置
     */
    @Autowired
    private GroupFunctionMapper groupFunctionMapper;

    /**
     * 获取信息
     */
    @Autowired
    private BotService botService;

    /**文件夹管理*/
    @Autowired
    private FolderGroupMapper folderGroupMapper;

    /**任务管理*/
    @Autowired
    private MissionMapper missionMapper;

    /**定时任务*/
    @Autowired
    private RemindmissionMapper remindmissionMapper;

    /**
     * 注册群组
     *
     * @param qqGroup 要注册的群组
     * @return 修改数据库条数
     * @Title insertGroup
     * @Description 注册群组
     * @author 张逸辰
     * @Date 2020/8/27 22:46
     */
    @Override
    public int insertGroup(QqGroup qqGroup) {
        int result;
        //群配置
        GroupFunction groupFunction = new GroupFunction();
        groupFunction.setGroupid(qqGroup.getGroupid());
        Command command = new Command();
        command.setGroupid(qqGroup.getGroupid());
        result = qqGroupMapper.insertSelective(qqGroup);
        result += groupFunctionMapper.insertSelective(groupFunction);
        result += commandMapper.insertSelective(command);
        return result;
    }

    /**
     * 添加群组与用户进行绑定
     *
     * @param qqGroup 群组
     * @param user    用户
     * @return 数据库更新行数
     * @Title bindGroup
     * @Description 添加群组与用户进行绑定
     * @author 张逸辰
     * @Date 2020/8/26 18:00
     */
    @Override
    public int bindGroup(QqGroup qqGroup, User user) {
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupid(qqGroup.getGroupid());
        groupUser.setUserid(user.getId());
        int result = 0;
        if (qqGroup.getDeletetime() != 0) {
            qqGroup.setDeletetime(0L);
            result = qqGroupMapper.updateByPrimaryKey(qqGroup);
        }
        result += groupUserMapper.insert(groupUser);
        //删除原记录
        FolderGroupExample folderGroupExample = new FolderGroupExample();
        folderGroupExample.createCriteria().andGroupidEqualTo(qqGroup.getGroupid());
        result += folderGroupMapper.deleteByExample(folderGroupExample);
        //默认群功能设置
        GroupFunction groupFunction = selectGroupFunction(qqGroup);
        groupFunction.setCommand(false);
        groupFunction.setCheckmsg(false);
        groupFunction.setAcceptgrouprequest(null);
        result += groupFunctionMapper.updateByPrimaryKey(groupFunction);
        //命令恢复默认
        Command command = commandMapper.selectByPrimaryKey(qqGroup.getGroupid());
        command.setCommandlist(null);
        command.setPowerlist(null);
        commandMapper.updateByPrimaryKeyWithBLOBs(command);
        //删除任务
        MissionExample missionExample = new MissionExample();
        missionExample.createCriteria().andGroupidEqualTo(qqGroup.getGroupid());
        result += missionMapper.deleteByExample(missionExample);
        //提醒任务
        RemindmissionExample remindmissionExample = new RemindmissionExample();
        remindmissionExample.createCriteria().andGroupidEqualTo(qqGroup.getGroupid());
        result += remindmissionMapper.deleteByExample(remindmissionExample);
        return result;
    }

    /**
     * 删除群组
     *
     * @param qqGroup 要删除的群
     * @return 数据库处理行数
     * @Title deleteGroup
     * @Description 删除群组
     * @author 张逸辰
     * @Date 2020/8/26 15:59
     */
    @Override
    public int deleteGroup(QqGroup qqGroup) {
        int result;
        qqGroup.setDeletetime(System.currentTimeMillis());
        result = qqGroupMapper.updateByPrimaryKey(qqGroup);
        result += groupUserMapper.deleteByPrimaryKey(qqGroup.getGroupid());
        return result;
    }

    /**
     * 更新群组
     *
     * @param qqGroup 群组
     * @return 修改数据库条数
     * @Title updateGroupList
     * @Description
     * @author 张逸辰
     * @Date 2020/8/26 22:17
     */
    @Override
    public int updateGroup(QqGroup qqGroup) {
        return qqGroupMapper.updateByPrimaryKey(qqGroup);
    }

    /**
     * 通过用户名查询拥有的群组
     *
     * @param user 用户
     * @return 用户所拥有的群组
     * @Title selectByUser
     * @Description 通过用户名查询拥有的群组
     * @author 张逸辰
     * @Date 2020/8/27 17:27
     */
    @Override
    public List<QqGroup> selectByUser(User user) {
        UserSelect userSelect = new UserSelect(user);
        userSelect.setRoot(false);
        return groupFolderMapper.selectQqGroupByUser(userSelect);
    }

    /**
     * 根据群id查询qq群
     *
     * @param id 群id
     * @param all 全部包括删除的
     * @return 群
     * @Title selectByGroupId
     * @Description 根据群id查询qq群
     * @author 张逸辰
     * @Date 2020/8/29 14:46
     */
    @Override
    public QqGroup selectByGroupId(String id, boolean all) {
        QqGroup qqGroup = qqGroupMapper.selectByPrimaryKey(id);
        if (qqGroup != null && (all || qqGroup.getDeletetime() == 0)) {
            return qqGroup;
        }else if(qqGroup == null){
            registerGroup();
            qqGroup = qqGroupMapper.selectByPrimaryKey(id);
            return qqGroup;
        }
        return null;
    }

    private void registerGroup() {
        synchronized (GroupSettingImpl.class){
            //查询全部注册群组
            List<QqGroup> qqGroups = qqGroupMapper.selectByExample(new QqGroupExample());
            //获取qq绑定的群组
            List<QqGroup> botGroups = new ArrayList<>();
            List<Bot> botList = botService.getBotList();
            for (Bot bot : botList) {
                ContactList<Group> groups = bot.getGroups();
                for (Group l : groups) {
                    QqGroup qqG = new QqGroup();
                    qqG.setGroupid(String.valueOf(l.getId()));
                    qqG.setGroupname(l.getName());
                    botGroups.add(qqG);
                }
            }
            //同步到数据库
            for (QqGroup botGroup : botGroups) {
                //判断数据库是否存在
                if (!isContains(qqGroups, botGroup)) {
                    //注册群
                    insertGroup(botGroup);
                }
            }
        }
    }

    /**
     * 从用户列表查找是否存在该QQ
     *
     * @param users 用户列表
     * @param code  qq号
     * @return null 不存在
     * @Title getUser
     * @Description 从用户列表查找是否存在该QQ
     * @author 张逸辰
     * @Date 2020/8/26 0:15
     */
    private User getUser(List<User> users, String code) {
        for (User user : users) {
            if (user.getQq().equals(code)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据群号，如果群号相同则认为相同
     *
     * @param qqGroups 数据库群组
     * @param botGroup 机器人群组
     * @return 是否群里包含该群组
     * @Title isContains
     * @Description 根据群号，如果群号相同则认为相同
     * @author 张逸辰
     * @Date 2020/8/25 21:38
     */
    private boolean isContains(List<QqGroup> qqGroups, QqGroup botGroup) {
        for (QqGroup qqGroup : qqGroups) {
            if (qqGroup.getGroupid().equals(botGroup.getGroupid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询群是否被绑定
     *
     * @param qqGroup 群
     * @return 是否被绑定
     * @Title isGroupBind
     * @Description 查询群是否被绑定
     * @author 张逸辰
     * @Date 2020/8/29 22:33
     */
    @Override
    public boolean isGroupBind(QqGroup qqGroup) {
        GroupUser groupUser = groupUserMapper.selectByPrimaryKey(qqGroup.getGroupid());
        return groupUser != null;
    }

    /**
     * 查询用户是否有管理权限
     *
     * @param qqGroup 群
     * @param user    用户
     * @return 有管理权限
     * @Title userGroupPower
     * @Description 查询用户是否有管理权限
     * @author 张逸辰
     * @Date 2020/8/29 22:44
     */
    @Override
    public boolean userGroupPower(QqGroup qqGroup, User user) {
        ContactList<Member> members = botService.getGroup(Long.parseLong(qqGroup.getGroupid())).getMembers();
        for(Member groupMember : members){
            if(groupMember.getId() == Long.parseLong(user.getQq())){
                return (groupMember.getPermission().equals(MemberPermission.OWNER) || groupMember.getPermission().equals(MemberPermission.ADMINISTRATOR));
            }
        }
        return false;
    }

    /**
     * 是否开放命令
     *
     * @param qqGroup 群
     * @return true开放命令
     * @Title openCommand
     * @Description 是否开放命令
     * @author 张逸辰
     * @Date 2020/9/14 18:27
     */
    @Override
    public boolean openCommand(QqGroup qqGroup) {
        GroupFunction groupFunction = groupFunctionMapper.selectByPrimaryKey(qqGroup.getGroupid());
        return groupFunction.getCommand();
    }


    /**
     * 添加命令
     *
     * @param qqGroup   群组
     * @param commandId 命令实现类
     * @param power     权限列表
     * @return 修改记录条数
     * @Title addCommand
     * @Description 添加命令
     * @author 张逸辰
     * @Date 2020/8/27 17:41
     */
    @Override
    public int addCommand(QqGroup qqGroup, String commandId, List<String> power) {
        CommandVo commandVo = new CommandVo(commandMapper.selectByPrimaryKey(qqGroup.getGroupid()));
        //获取命令列表与权限列表
        List<String> commandList = commandVo.getCommands();
        List<List<String>> powerList = commandVo.getPowers();
        int i;
        for (i = 0; i < commandList.size(); i++) {
            if (commandList.get(i).equals(commandId)) {
                break;
            }
        }
        if (i >= commandList.size()) {
            //添加
            commandList.add(commandId);
            powerList.add(power);
        } else {
            powerList.set(i, power);
        }
        //同步到数据库
        commandVo.setCommandlist(JSON.toJSONString(commandList).getBytes(StandardCharsets.UTF_8));
        commandVo.setPowerlist(JSON.toJSONString(powerList).getBytes(StandardCharsets.UTF_8));
        return commandMapper.updateByPrimaryKeyWithBLOBs(commandVo);
    }

    /**
     * 删除群组命令
     *
     * @param qqGroup   群组
     * @param commandId 命令实现类
     * @return 修改记录条数
     * @Title deleteCommand
     * @Description 删除命令
     * @author 张逸辰
     * @Date 2020/8/27 17:42
     */
    @Override
    public int deleteCommand(QqGroup qqGroup, String commandId) {
        Command c = commandMapper.selectByPrimaryKey(qqGroup.getGroupid());
        if (c == null) {
            return 0;
        }
        CommandVo commandVo = new CommandVo(c);
        List<String> commands = commandVo.getCommands();
        List<List<String>> powers = commandVo.getPowers();
        int i;
        //查找
        for (i = 0; i < commands.size(); i++) {
            if (commands.get(i).equals(commandId)) {
                break;
            }
        }
        if (i >= commands.size()) {
            return 0;
        }
        //删除
        commands.remove(i);
        powers.remove(i);
        commandVo.setCommandlist(JSON.toJSONString(commands).getBytes(StandardCharsets.UTF_8));
        commandVo.setPowerlist(JSON.toJSONString(powers).getBytes(StandardCharsets.UTF_8));
        return commandMapper.updateByPrimaryKeyWithBLOBs(commandVo);
    }

    /**
     * 查询群组命令
     *
     * @param qqGroup 群组
     * @return 群组, 内含List<String [ ]> 第一个元素为命令，第二个为权限
     * @Title selectCommand
     * @Description 查询群组命令
     * @author 张逸辰
     * @Date 2020/8/27 17:45
     */
    @Override
    public List<List<Object>> selectCommand(QqGroup qqGroup) {
        Command command = commandMapper.selectByPrimaryKey(qqGroup.getGroupid());
        if (command != null) {
            CommandVo commandVo = new CommandVo(command);
            List<String> commands = commandVo.getCommands();
            List<List<String>> powers = commandVo.getPowers();
            List<List<Object>> result = new ArrayList<>();
            for (int i = 0; i < commands.size(); i++) {
                ArrayList<Object> r = new ArrayList<>();
                r.add(commands.get(i));
                r.add(powers.get(i));
                result.add(r);
            }
            return result;
        }
        return null;
    }

    /**
     * 查询群组命令
     *
     * @param qqGroup 群组
     * @return 群组命令
     * @Title selectCommandVo
     * @Description 查询群组命令
     * @author 张逸辰
     * @Date 2020/8/30 23:01
     */
    @Override
    public CommandVo selectCommandVo(QqGroup qqGroup) {
        Command command = commandMapper.selectByPrimaryKey(qqGroup.getGroupid());
        if (command != null) {
            return new CommandVo(command);
        }
        return null;
    }

    /**
     * 获取群设置
     *
     * @param qqGroup 群组
     * @return 群组设置
     * @Title selectGroupFunction
     * @Description 获取群设置
     * @author 张逸辰
     * @Date 2020/8/28 14:54
     */
    @Override
    public GroupFunction selectGroupFunction(QqGroup qqGroup) {
        return groupFunctionMapper.selectByPrimaryKey(qqGroup.getGroupid());
    }

    /**
     * 用于修改群功能配置
     *
     * @param groupFunction 群功能配置类
     * @return 修改数据库条数
     * @Title updateGroupFunction
     * @Description 用于修改群功能配置
     * @author 张逸辰
     * @Date 2020/8/27 22:51
     */
    @Override
    public int updateGroupFunction(GroupFunction groupFunction) {
        return groupFunctionMapper.updateByPrimaryKey(groupFunction);
    }


}
