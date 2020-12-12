package top.fluffcotton.pojo;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MissonTimeJobType
 * @Description: 任务时间的类型
 * @date
 */
public enum MissionTimeJobType {
    /**
     * 默认
     */
    DEFAULT,
    /**
     * 启动统计任务
     */
    START,
    /**
     * 统计任务结束
     */
    FINISH,
    /**
     * 统计任务提醒
     */
    REMIND
}
