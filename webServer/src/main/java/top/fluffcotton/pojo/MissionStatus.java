package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MissionStatus
 * @Description: 任务状态
 * @date 2020.08.02 21:06
 */
public enum MissionStatus {
    /**
     * 未开始
     */
    UNSTART(0),
    /**
     * 进行中
     */
    PROGRESS(1),
    /**
     * 任务结束
     */
    FINISH(2),
    ;

    private final int code;
    MissionStatus(int code) {
        this.code = code;
    }

    /**
     * 获取byte
     * @Title getByte
     * @Description 获取byte
     * @return byte
     * @author 张逸辰
     * @Date 2020/8/2 21:33
     */
    public byte getByte(){
        return Byte.parseByte(toString());
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
