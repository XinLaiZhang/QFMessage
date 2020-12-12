package top.fluffcotton.pojo;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CommandVo
 * @Description: Command增强类
 * @date 2020.07.28 23:25
 */
public class CommandVo extends Command {
    private List<List<String>> powers;

    private List<String> commands;

    public CommandVo() {
    }

    public CommandVo(Command c) {
        super.setGroupid(c.getGroupid());
        super.setCommandlist(c.getCommandlist());
        super.setPowerlist(c.getPowerlist());
    }

    /**
     * 将 父类 powerlist转为PowerType列表
     *
     * @return PowerType列表
     * @Title getPowerList
     * @Description 将 父类 powerlist转为PowerType列表
     * @author 张逸辰
     * @Date 2020/7/28 23:34
     */
    public List<List<String>> getPowers() {
        if (powers == null) {
            if (super.getPowerlist() == null) {
                powers = new ArrayList<>();
            } else {
                String powerJson = new String(super.getPowerlist(), StandardCharsets.UTF_8);
                powers = JSON.parseObject(powerJson, List.class);
            }
        }
        return powers;
    }

    /**
     * 将父类Commandlist转为数组
     *
     * @return List<String>
     * @Title getCommandList
     * @Description 将父类Commandlist转为数组
     * @author 张逸辰
     * @Date 2020/7/28 23:35
     */
    public List<String> getCommands() {
        if (commands == null) {
            if (super.getCommandlist() == null) {
                commands = new ArrayList<>();
            } else {
                String commandJson = new String(super.getCommandlist());
                commands = JSON.parseObject(commandJson, List.class);
            }
        }
        return commands;
    }
}
