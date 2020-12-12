package top.fluffcotton.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fluffcotton.listen.CountMission;
import top.fluffcotton.listen.RemindMission;
import top.fluffcotton.mapper.MissionMapper;
import top.fluffcotton.mapper.RemindmissionMapper;
import top.fluffcotton.pojo.*;
import top.fluffcotton.service.ExcelUtils;
import top.fluffcotton.service.MissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MissionServiceImpl
 * @Description: 任务服务
 * @date 2020.08.31 17:29
 */
@Service
public class MissionServiceImpl implements MissionService {

    /**统计任务*/
    @Autowired
    private CountMission countMission;

    /**提醒任务*/
    @Autowired
    private RemindMission remindMission;

    /**统计任务*/
    @Autowired
    private MissionMapper missionMapper;

    /**提醒任务*/
    @Autowired
    private RemindmissionMapper remindmissionMapper;


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
    @Override
    public Mission insertMission(Mission mission) {
        mission.setmId(UUID.randomUUID().toString());
        missionMapper.insert(mission);
        countMission.register(mission, false);
        return mission;
    }

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
    @Override
    public int deleteMission(Mission mission) {
        countMission.delete(mission);
        return missionMapper.deleteByPrimaryKey(mission.getmId());
    }

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
    @Override
    public int updateMission(Mission mission) {
        int result = missionMapper.updateByPrimaryKey(mission);
        if(result > 0){
            //清空之前的任务
            countMission.delete(mission);
            //注册新任务
            countMission.register(mission, false);
        }
        return result;
    }

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
    @Override
    public List<Mission> selectMission(QqGroup qqGroup) {
        MissionExample missionExample = new MissionExample();
        missionExample.createCriteria().andGroupidEqualTo(qqGroup.getGroupid());
        return missionMapper.selectByExample(missionExample);
    }

    /**
     * 根据id查询任务
     *
     * @param m_ID id
     * @return 任务
     * @Title selectMission
     * @Description 根据id查询任务
     * @author 张逸辰
     * @Date 2020/9/25 19:55
     */
    @Override
    public Mission selectMission(String m_ID) {
        return missionMapper.selectByPrimaryKey(m_ID);
    }

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
    @Override
    public List<GroupList> selectGroupList(Mission mission) {
        List<Map<Integer, String>> userList = countMission.getUserList(mission);
        List<GroupList> groupLists = new ArrayList<>();
        for(Map<Integer, String > user:userList){
            GroupList groupList = new GroupList();
            groupList.setName(user.get(ExcelUtils.NAME));
            groupList.setQq(user.get(ExcelUtils.PRIMARY_KEY));
            groupList.setFinish(user.get(ExcelUtils.FINISH_FLAG).equals(String.valueOf(true)));
            List<String> attr = new ArrayList<>();
            for(int i = ExcelUtils.FINISH_FLAG + 1; i < user.size(); i++){
                attr.add(user.get(i));
            }
            groupList.setSaveAttr(attr);
            groupLists.add(groupList);
        }
        return groupLists;
    }

    /**
     * 添加提醒任务
     *
     * @param remindmission 提醒任务
     * @return 添加的任务
     * @Title insertRemindMission
     * @Description 添加提醒任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    @Override
    public Remindmission insertRemindMission(Remindmission remindmission) {
        remindmission.setmId(UUID.randomUUID().toString());
        int insert = remindmissionMapper.insert(remindmission);
        if(insert > 0){
            remindMission.register(remindmission);
        }
        return remindmission;
    }

    /**
     * 删除提醒任务
     *
     * @param remindmission 提醒任务
     * @return 数据库修改条数
     * @Title deleteRemindMission
     * @Description 删除提醒任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    @Override
    public int deleteRemindMission(Remindmission remindmission) {
        int i = remindmissionMapper.deleteByPrimaryKey(remindmission.getmId());
        if(i > 0){
            remindMission.delete(remindmission);
        }
        return i;
    }

    /**
     * 更新提醒任务
     *
     * @param remindmission 提醒任务
     * @return 数据库修改条数
     * @Title updateRemindMission
     * @Description 更新提醒任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    @Override
    public int updateRemindMission(Remindmission remindmission) {
        int i = remindmissionMapper.updateByPrimaryKey(remindmission);
        if(i > 0){
            remindMission.delete(remindmission);
            remindMission.register(remindmission);
        }
        return i;
    }

    /**
     * 更加群查找提醒任务
     *
     * @param qqGroup 群
     * @return 提醒任务
     * @Title selectRemindMission
     * @Description 更加群查找提醒任务
     * @author 张逸辰
     * @Date 2020/8/30 16:58
     */
    @Override
    public List<Remindmission> selectRemindMission(QqGroup qqGroup) {
        RemindmissionExample remindmissionExample = new RemindmissionExample();
        remindmissionExample.createCriteria().andGroupidEqualTo(qqGroup.getGroupid());
        return remindmissionMapper.selectByExample(remindmissionExample);
    }

    /**
     * 根据id查询任务
     *
     * @param m_ID id
     * @return 任务
     * @Title selectRemindMission
     * @Description 根据id查询任务
     * @author 张逸辰
     * @Date 2020/9/25 19:56
     */
    @Override
    public Remindmission selectRemindMission(String m_ID) {
        return remindmissionMapper.selectByPrimaryKey(m_ID);
    }


}
