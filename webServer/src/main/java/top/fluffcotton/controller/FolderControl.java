package top.fluffcotton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fluffcotton.pojo.*;
import top.fluffcotton.service.GroupFileService;
import top.fluffcotton.service.GroupSetting;
import top.fluffcotton.service.RSATool;
import top.fluffcotton.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: FolderControl
 * @Description: 管理用户管理中心api
 * @date 2020.08.28 17:05
 */
@RestController
@RequestMapping("/user")
public class FolderControl {

    /**
     * 默认文件夹名
     */
    public static final String FOLDER_DEFAULT_NAME = "文件夹";

    /**
     * 群组文件夹管理工具
     */
    @Autowired
    private GroupFileService groupFileService;

    /**
     * 群组设置
     */
    @Autowired
    private GroupSetting groupSetting;

    /**
     * 用户管理
     */
    @Autowired
    private UserService userService;

    /**
     * 解密工具
     */
    @Autowired
    private RSATool rsaTool;

    /**
     * 获取文件列表
     *
     * @param id       文件夹id，若为null 则获取根目录
     * @param username 登陆用户名
     * @return {
     * data:文件列表
     * code:200 成功,
     * code:400 异常
     * }
     * @Title getFolder
     * @Description 获取文件列表
     * @author 张逸辰
     * @Date 2020/8/28 22:04
     */
    @RequestMapping("/getFolder")
    public Map<String, Object> getFolder(String id, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        List<GroupFolder> groupFolders = null;
        if (id == null) {
            try {
                username = rsaTool.decrypt(username);
                UserSelect userSelect = new UserSelect(userService.selectUserByUsername(username));
                userSelect.setRoot(true);
                groupFolders = groupFileService.selectGroupFolder(userSelect);
            } catch (Exception e) {
                map.put("code", JsonCode.EXCEPTION.toString());
                e.printStackTrace();
            }
        } else {
            Folder folder = new Folder();
            folder.setFileid(id);
            groupFolders = groupFileService.selectGroupFolder(folder);
        }
        map.put("code", JsonCode.SUCCESS.toString());
        map.put("data", groupFolders);
        return map;
    }

    /**
     * 创建文件夹
     *
     * @param id       父文件夹id，可以为空，则为根目录
     * @param folderId 子文件夹id，String【】，可以为空
     * @param groupId  群组id     string【】，可以为空
     * @param username cookie
     * @return { code:200成功
     * data:文件夹json
     * code:308文件夹添加失败
     * code:400异常
     * }
     * @Title createFolder
     * @Description 创建文件夹
     * @author 张逸辰
     * @Date 2020/8/29 16:41
     */
    @RequestMapping("/createFolder")
    public Map<String, Object> createFolder(String id, String[] folderId, String[] groupId, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        Folder father = null;
        //新建的文件夹
        Folder folder = new Folder();
        folder.setFileid(UUID.randomUUID().toString());
        if (id != null) {
            father = groupFileService.selectGroupFolder(id, false);
            folder.setIsroot(false);
        }
        folder.setFilename(FOLDER_DEFAULT_NAME);
        try {
            username = rsaTool.decrypt(username);
            User user = userService.selectUserByUsername(username);
            folder.setUserid(user.getId());
            int result = groupFileService.createFolder(father, folder);
            //失败
            if (result <= 0) {
                map.put("code", JsonCode.ADD_FAIL.toString());
            } else {
                //添加成功后，添加子文件
                if (folderId != null) {
                    for (String fId : folderId) {
                        Folder f = groupFileService.selectGroupFolder(fId, false);
                        groupFileService.insertFolder(folder, f);
                    }
                }
                if (groupId != null) {
                    for (String gId : groupId) {
                        QqGroup g = groupSetting.selectByGroupId(gId, false);
                        groupFileService.insertFolder(folder, g);
                    }
                }
                map.put("code", JsonCode.SUCCESS.toString());
                map.put("data", new GroupFolder(folder));
            }
        } catch (Exception e) {
            map.put("code", JsonCode.EXCEPTION.toString());
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 删除群或文件夹
     *
     * @param folderId 文件夹id，允许为null
     * @param groupId  群id， 允许null
     * @return {
     * code:200 成功
     * }
     * @Title deleteFile
     * @Description 删除群或文件夹
     * @author 张逸辰
     * @Date 2020/8/29 17:35
     */
    @RequestMapping("/deleteFile")
    public Map<String, Object> deleteFile(String[] folderId, String[] groupId) {
        Map<String, Object> map = new HashMap<>(5);
        int result = 0;
        if (folderId != null) {
            for (String f : folderId) {
                Folder folder = groupFileService.selectGroupFolder(f, false);
                if (folder != null) {
                    result += groupFileService.deleteFile(folder);
                }
            }
        }
        if (groupId != null) {
            for (String g : groupId) {
                QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                if (qqGroup != null) {
                    result += groupSetting.deleteGroup(qqGroup);
                }
            }
        }
        if(result > 0){
            map.put("code", JsonCode.SUCCESS.toString());
        }
        return map;
    }

    /**
     * 修改文件夹名
     *
     * @param folderId   文件夹id
     * @param folderName 文件夹更新的名称
     * @return {
     * code:200 成功
     * code:309 没有此文件夹
     * code:310 修改失败
     * }
     * @Title renameFolder
     * @Description 修改文件夹名
     * @author 张逸辰
     * @Date 2020/8/29 17:40
     */
    @RequestMapping("/renameFolder")
    public Map<String, Object> renameFolder(String folderId, String folderName) {
        Map<String, Object> map = new HashMap<>(5);
        Folder folder = groupFileService.selectGroupFolder(folderId, false);
        if (folder == null) {
            map.put("code", JsonCode.FIND_FAIL.toString());
        } else {
            folder.setFilename(folderName);
            int result = groupFileService.updateFolderName(folder);
            if (result > 0) {
                map.put("code", JsonCode.SUCCESS.toString());
            } else {
                map.put("code", JsonCode.CHANGE_FAIL.toString());
            }
        }
        return map;
    }

    /**
     * 获取设置，允许多个设置一同获取，结果取&集
     *
     * @param groupId 群id
     * @return {
     * code:200
     * command: 命令开关
     * checkMsg: 消息审计开关
     * }
     * @Title getGroupSetting
     * @Description 获取设置，允许多个设置一同获取，结果取&集
     * @author 张逸辰
     * @Date 2020/8/29 21:46
     */
    @RequestMapping("/getGroupSetting")
    public Map<String, Object> getGroupSetting(String[] groupId) {
        Map<String, Object> map = new HashMap<>(5);
        if (groupId != null && groupId.length > 0) {
            boolean command = true;
            boolean checkMsg = true;
            for (String g : groupId) {
                QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                if (qqGroup != null) {
                    GroupFunction groupFunction = groupSetting.selectGroupFunction(qqGroup);
                    command &= groupFunction.getCommand();
                    checkMsg &= groupFunction.getCheckmsg();
                }
            }
            map.put("code", JsonCode.SUCCESS.toString());
            map.put("command", command);
            map.put("checkMsg", checkMsg);
        }
        return map;
    }

    /**
     * 设置群设置
     *
     * @param groupId  群号，允许多个
     * @param command  命令
     * @param checkMsg 检测消息
     * @return {
     * code:200成功
     * }
     * @Title setGroupSetting
     * @Description 设置群设置
     * @author 张逸辰
     * @Date 2020/8/29 21:51
     */
    @RequestMapping("/setGroupSetting")
    public Map<String, Object> setGroupSetting(String[] groupId, Boolean command, Boolean checkMsg) {
        Map<String, Object> map = new HashMap<>(5);
        if (groupId != null && groupId.length > 0) {
            for (String g : groupId) {
                QqGroup qqGroup = groupSetting.selectByGroupId(g, false);
                if (qqGroup != null) {
                    GroupFunction groupFunction = groupSetting.selectGroupFunction(qqGroup);
                    if (command != null) {
                        groupFunction.setCommand(command);
                    }
                    if (checkMsg != null) {
                        groupFunction.setCheckmsg(checkMsg);
                    }
                    groupSetting.updateGroupFunction(groupFunction);
                }
            }
            map.put("code", JsonCode.SUCCESS.toString());
        }
        return map;
    }

    /**
     * 查询群信息
     *
     * @param groupId 群号
     * @return {
     * code:309 查询失败
     * code:311 群已经被绑定
     * code:312 没有权限
     * code:200 成功
     * data:群名
     * }
     * @Title getGroupInfo
     * @Description 查询群信息
     * @author 张逸辰
     * @Date 2020/8/29 22:38
     */
    @RequestMapping("/getGroupInfo")
    public Map<String, Object> getGroupInfo(String groupId, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        QqGroup qqGroup = groupSetting.selectByGroupId(groupId, true);
        if (qqGroup == null) {
            //查询不到
            map.put("code", JsonCode.FIND_FAIL.toString());
        } else {
            boolean groupBind = groupSetting.isGroupBind(qqGroup);
            if (groupBind) {
                //被绑定
                map.put("code", JsonCode.BIND.toString());
            } else {
                try {
                    username = rsaTool.decrypt(username);
                    User user = userService.selectUserByUsername(username);
                    if (groupSetting.userGroupPower(qqGroup, user)) {
                        map.put("code", JsonCode.SUCCESS.toString());
                        map.put("data", qqGroup.getGroupname());
                    } else {
                        //没有权限
                        map.put("code", JsonCode.NOT_POWER.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("code", JsonCode.EXCEPTION.toString());
                }
            }
        }
        return map;
    }

    /**
     * 绑定群
     *
     * @param groupId  群号
     * @param username cookie
     * @return {
     * code:200成功
     * code:400异常
     * }
     * @Title bindGroup
     * @Description 绑定群
     * @author 张逸辰
     * @Date 2020/8/29 23:04
     */
    @RequestMapping("/bindGroup")
    public Map<String, Object> bindGroup(String folderId, String[] groupId, @CookieValue("username") String username) {
        Map<String, Object> map = new HashMap<>(5);
        Folder father = null;
        if (folderId != null) {
            father = groupFileService.selectGroupFolder(folderId, false);
        }
        if (groupId != null && groupId.length > 0) {
            try {
                username = rsaTool.decrypt(username);
                User user = userService.selectUserByUsername(username);
                for (String g : groupId) {
                    QqGroup qqGroup = groupSetting.selectByGroupId(g, true);
                    if (qqGroup.getDeletetime() != 0) {
                        qqGroup.setDeletetime(0L);
                        qqGroup.setIsroot(true);
                        groupSetting.updateGroup(qqGroup);
                    }
                    groupSetting.bindGroup(qqGroup, user);
                    //添加父目录
                    if (father != null) {
                        groupFileService.insertFolder(father, qqGroup);
                    }
                }
                map.put("code", JsonCode.SUCCESS.toString());
            } catch (Exception e) {
                e.printStackTrace();
                map.put("code", JsonCode.EXCEPTION.toString());
            }
        }
        return map;
    }

    


}
