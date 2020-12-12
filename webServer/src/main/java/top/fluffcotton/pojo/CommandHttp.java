package top.fluffcotton.pojo;


import top.fluffcotton.listen.handle.CommandHandle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: CommandHttp
 * @Description: 命令用于传输与前端交互
 * @date 2020.08.30 21:23
 */
public class CommandHttp {
    /**命令id*/
    private String commandId;
    /**命令名称*/
    private String commandName;
    /**权限*/
    private List<String> power;
    /**是否开启*/
    private boolean open;
    /**用于html*/
    private boolean inputVisible;
    /**用于html*/
    private String inputValue;

    public CommandHttp() {
        this.inputVisible = false;
        this.inputValue = "";
        this.open = false;
        this.setPower(new ArrayList<>());
    }

    public CommandHttp(CommandHandle commandHandle) {
        this.commandId = commandHandle.getID();
        this.commandName = commandHandle.getName();
        this.open = false;
        this.inputVisible = false;
        this.inputValue = "";
        this.setPower(new ArrayList<>());
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public List<String> getPower() {
        return power;
    }

    public void setPower(List<String> power) {
        this.power = power;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isInputVisible() {
        return inputVisible;
    }

    public void setInputVisible(boolean inputVisible) {
        this.inputVisible = inputVisible;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
}
