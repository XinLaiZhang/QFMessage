package top.fluffcotton.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.fluffcotton.pojo.*;
import top.fluffcotton.service.ExcelUtils;
import top.fluffcotton.service.FileNameTools;
import top.fluffcotton.service.GroupSetting;
import top.fluffcotton.service.MissionService;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: MissionControl
 * @Description: 任务控制
 * @date 2020.08.31 20:16
 */
@RestController
@RequestMapping("/user")
@PropertySource("classpath:GroupTaskListConfig.properties")
public class MissionControl {

    /**
     * excel处理
     */
    private final ExcelUtils excelUtils = ExcelUtils.getExcelUtils();
    /**
     * 任务处理类
     */
    @Autowired
    private MissionService missionService;
    /**
     * 群设置
     */
    @Autowired
    private GroupSetting groupSetting;
    /**
     * 文件夹名称
     */
    @Value("${groupTask.url}")
    private String groupTaskUrl;

    /**
     * 根据群号查询任务
     *
     * @param groupId 群号
     * @return {
     * code:200,成功
     * data:群
     * code:314,群不存在
     * }
     * @Title getHttpMission
     * @Description 查询任务
     * @author 张逸辰
     * @Date 2020/9/14 19:57
     */
    @RequestMapping("/getHttpMission")
    public Map<String, Object> getHttpMission(String groupId) {
        Map<String, Object> map = new HashMap<>(5);
        QqGroup qqGroup = groupSetting.selectByGroupId(groupId, false);
        if (qqGroup != null) {
            List<Mission> missions = missionService.selectMission(qqGroup);
            List<Remindmission> remindmissions = missionService.selectRemindMission(qqGroup);
            List<HttpMission> httpMissionList = new ArrayList<>();
            if (missions.size() > 0) {
                for (Mission m : missions) {
                    httpMissionList.add(new HttpMission(m));
                }
            }
            if (remindmissions.size() > 0) {
                for (Remindmission rm : remindmissions) {
                    httpMissionList.add(new HttpMission(rm));
                }
            }
            map.put("code", JsonCode.SUCCESS.toString());
            map.put("data", httpMissionList);
        } else {
            map.put("code", JsonCode.NOT_HAS.toString());
        }
        return map;
    }

    /**
     * 添加任务
     *
     * @param httpMission httpMission json
     * @return {
     * code：200成功
     * data：任务
     * code:308添加失败
     * }
     * @Title insertHttpMission
     * @Description 添加任务
     * @author 张逸辰
     * @Date 2020/9/15 10:51
     */
    @RequestMapping("/insertHttpMission")
    public Map<String, Object> insertHttpMission(String httpMission) {
        Map<String, Object> map = new HashMap<>(5);
        HttpMission hm = JSON.parseObject(httpMission).toJavaObject(HttpMission.class);
        Mission mission = hm.getMission();
        if (mission != null) {
            missionService.insertMission(mission);
            map.put("data", new HttpMission(mission));
        }
        Remindmission remindMission = hm.getRemindMission();
        if (remindMission != null) {
            missionService.insertRemindMission(remindMission);
            map.put("data", new HttpMission(remindMission));
        }
        if (map.containsKey("data")) {
            map.put("code", JsonCode.SUCCESS.toString());
        } else {
            map.put("code", JsonCode.ADD_FAIL.toString());
        }
        return map;
    }

    /**
     * 修改任务
     *
     * @param httpMission httpMission json
     * @return {
     * code：200成功
     * data：任务
     * code:310修改失败
     * }
     * @Title updateHttpMission
     * @Description 修改任务
     * @author 张逸辰
     * @Date 2020/9/25 16:20
     */
    @RequestMapping("/updateHttpMission")
    public Map<String, Object> updateHttpMission(String httpMission) {
        Map<String, Object> map = new HashMap<>(5);
        HttpMission hm = JSON.parseObject(httpMission).toJavaObject(HttpMission.class);
        Mission mission = hm.getMission();
        if (mission != null) {
            missionService.updateMission(mission);
            map.put("data", new HttpMission(mission));
        }
        Remindmission remindMission = hm.getRemindMission();
        if (remindMission != null) {
            missionService.updateRemindMission(remindMission);
            map.put("data", new HttpMission(remindMission));
        }
        if (map.containsKey("data")) {
            map.put("code", JsonCode.SUCCESS.toString());
        } else {
            map.put("code", JsonCode.CHANGE_FAIL.toString());
        }
        return map;
    }
    /**
     * 删除任务
     *
     * @param m_ID 任务id
     * @return {
     * code：200成功
     * code:313 未查询到
     * }
     * @Title updateHttpMission
     * @Description 删除任务
     * @author 张逸辰
     * @Date 2020/9/25 16:20
     */
    @RequestMapping("/deleteHttpMission")
    public Map<String, Object> deleteHttpMission(String m_ID) {
        Map<String, Object> map = new HashMap<>(5);
        Mission mission = missionService.selectMission(m_ID);
        Remindmission remindmission = missionService.selectRemindMission(m_ID);
        if(mission != null){
            missionService.deleteMission(mission);
            map.put("code",JsonCode.SUCCESS.toString());
        }
        if(remindmission != null){
            missionService.deleteRemindMission(remindmission);
            map.put("code",JsonCode.SUCCESS.toString());
        }
        if(!map.containsKey("code")){
            map.put("code",JsonCode.NOT_HAS.toString());
        }
        return map;
    }



    /**
     * 上传名单列表
     *
     * @param file 文件
     * @return {
     * code:200成功
     * data：位置
     * code:314文件为空
     * code:400异常
     * }
     * @Title uploadList
     * @Description 上传名单列表
     * @author 张逸辰
     * @Date 2020/9/15 11:14
     */
    @RequestMapping("/uploadList")
    public Map<String, Object> uploadList(@RequestParam("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>(5);
        if (file.isEmpty()) {
            map.put("code", JsonCode.FILE_IS_NULL.toString());
            return map;
        }
        String fileName = FileNameTools.getUuidFilename(Objects.requireNonNull(file.getOriginalFilename()));
        String filePath = excelUtils.getGroupTaskListURL();
        String realPath = FileNameTools.getRealPath(fileName);
        File folder = new File(filePath + groupTaskUrl + realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File dest = new File(filePath + groupTaskUrl + realPath + fileName);
        try {
            file.transferTo(dest);
            map.put("code", JsonCode.SUCCESS.toString());
            map.put("data", realPath + fileName);
        } catch (IOException e) {
            map.put("code", JsonCode.EXCEPTION.toString());
        }
        return map;
    }
    /**
     * 查询任务结果
     * @Title selectResult
     * @Description 查询任务结果
     * @param m_ID 任务id
     * @return {
     *     code:200 成功
     *          314 没有名单
     *          309 没有任务
     *     data:{
     *          qq:
     *          name:
     *          finish:
     *          data0:
     *          ...
     *     }
     * }
     * @author 张逸辰
     * @Date 2020/9/26 15:18
     */
    @RequestMapping("/selectResult")
    public Map<String, Object> selectResult(String m_ID) {
        Map<String, Object> map = new HashMap<>(5);
        Mission mission = missionService.selectMission(m_ID);
        if(mission != null){
            if(mission.getGrouplist() == null || "".equals(mission.getGrouplist())){
                map.put("code",JsonCode.FILE_IS_NULL.toString());
            }else
            {
                String fileUrl = excelUtils.getGroupTaskListURL() + groupTaskUrl + mission.getGrouplist();
                List<Map<Integer, String>> userList = excelUtils.getUserList(m_ID, fileUrl);
                List<JSONObject> results = new ArrayList<>();
                for(Map<Integer, String> user:userList){
                    JSONObject result = new JSONObject();
                    result.put("qq",user.get(ExcelUtils.PRIMARY_KEY));
                    result.put("name",user.get(ExcelUtils.NAME));
                    String finish = user.get(ExcelUtils.FINISH_FLAG);
                    if(finish == null || "".equals(finish)){
                        finish = "false";
                    }
                    result.put("finish",finish);
                    for (int i = ExcelUtils.FINISH_FLAG+1; i < user.size(); i++) {
                        int j = i - 1 - ExcelUtils.FINISH_FLAG;
                        result.put("data" + j,user.get(i));
                    }
                    results.add(result);
                }
                map.put("data",results);
                map.put("code",JsonCode.SUCCESS.toString());
            }
        }else{
            map.put("code",JsonCode.FIND_FAIL.toString());
        }
        return map;
    }
}
