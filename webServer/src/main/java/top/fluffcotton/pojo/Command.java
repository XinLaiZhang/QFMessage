package top.fluffcotton.pojo;

public class Command {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column command.groupID
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    private String groupid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column command.powerList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    private byte[] powerlist;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column command.commandList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    private byte[] commandlist;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column command.groupID
     *
     * @return the value of command.groupID
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column command.groupID
     *
     * @param groupid the value for command.groupID
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column command.powerList
     *
     * @return the value of command.powerList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public byte[] getPowerlist() {
        return powerlist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column command.powerList
     *
     * @param powerlist the value for command.powerList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void setPowerlist(byte[] powerlist) {
        this.powerlist = powerlist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column command.commandList
     *
     * @return the value of command.commandList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public byte[] getCommandlist() {
        return commandlist;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column command.commandList
     *
     * @param commandlist the value for command.commandList
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void setCommandlist(byte[] commandlist) {
        this.commandlist = commandlist;
    }
}