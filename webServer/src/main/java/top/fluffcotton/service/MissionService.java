package top.fluffcotton.service;

import top.fluffcotton.pojo.GroupList;
import top.fluffcotton.pojo.Mission;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.pojo.Remindmission;

import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MissionService
 * @Description: 任务设置
 * @date 2020.08.30 16:47
 */
public interface MissionService {

    /**
     * 增加 信息收集任务
     *
     * @param mission 任务
     * @return 添加的任务
     * @Title insertMission
     * @Description 增加 信息收集任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    Mission insertMission(Mission mission);

    /**
     * 删除 信息收集任务
     *
     * @param mission 任务
     * @return 数据库修改条数
     * @Title deleteMission
     * @Description 删除 信息收集任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    int deleteMission(Mission mission);

    /**
     * 更新 信息收集任务
     *
     * @param mission 任务
     * @return 数据库修改条数
     * @Title updateMission
     * @Description 更新 信息收集任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    int updateMission(Mission mission);

    /**
     * 根据群查询拥有的任务
     *
     * @param qqGroup 群
     * @return 任务列表
     * @Title selectMission
     * @Description 根据群查询拥有的任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    List<Mission> selectMission(QqGroup qqGroup);
    /**
     * 根据id查询任务
     * @Title selectMission
     * @Description 根据id查询任务
     * @param m_ID id
     * @return  任务
     * @author 张逸辰
     * @Date 2020/9/25 19:55
     */
    Mission selectMission(String m_ID);

    /**
     * 查询任务拥有的名单
     *
     * @param mission 任务
     * @return 名单
     * @Title selectGroupList
     * @Description 查询任务拥有的名单
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    List<GroupList> selectGroupList(Mission mission);

    /**
     * 添加提醒任务
     * @Title insertRemindMission
     * @Description 添加提醒任务
     * @param remindmission 提醒任务
     * @return 添加的任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    Remindmission insertRemindMission(Remindmission remindmission);

    /**
     * 删除提醒任务
     * @Title deleteRemindMission
     * @Description 删除提醒任务
     * @param remindmission 提醒任务
     * @return 数据库修改条数
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    int deleteRemindMission(Remindmission remindmission);

    /**
     * 更新提醒任务
     * @Title updateRemindMission
     * @Description 更新提醒任务
     * @param remindmission 提醒任务
     * @return 数据库修改条数
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    int updateRemindMission(Remindmission remindmission);

    /**
     * 更加群查找提醒任务
     * @Title selectRemindMission
     * @Description 更加群查找提醒任务
     * @param qqGroup 群
     * @return 提醒任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    List<Remindmission> selectRemindMission(QqGroup qqGroup);

    /**
     * 根据id查询任务
     * @Title selectRemindMission
     * @Description 根据id查询任务
     * @param m_ID id
     * @return  任务
     * @author 张逸辰
     * @Date 2020/9/25 19:56
     */
    Remindmission selectRemindMission(String m_ID);


}
