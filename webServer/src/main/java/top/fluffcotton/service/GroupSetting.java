package top.fluffcotton.service;

import top.fluffcotton.pojo.CommandVo;
import top.fluffcotton.pojo.GroupFunction;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.pojo.User;

import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupSetting
 * @Description: 群与设置管理
 * @date 2020.08.25 17:28
 */
public interface GroupSetting {

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
    int insertGroup(QqGroup qqGroup);

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
    int bindGroup(QqGroup qqGroup, User user);

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
    int deleteGroup(QqGroup qqGroup);

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
    int updateGroup(QqGroup qqGroup);

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
    List<QqGroup> selectByUser(User user);

    /**
     * 根据群id查询qq群
     *
     * @param id 群id
     * @param all 全部包括删除文件
     * @return 群
     * @Title selectByGroupId
     * @Description 根据群id查询qq群
     * @author 张逸辰
     * @Date 2020/8/29 14:46
     */
    QqGroup selectByGroupId(String id, boolean all);

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
    boolean isGroupBind(QqGroup qqGroup);

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
    boolean userGroupPower(QqGroup qqGroup, User user);

    /**
     * 是否开放命令
     * @Title openCommand
     * @Description 是否开放命令
     * @param qqGroup 群
     * @return true开放命令
     * @author 张逸辰
     * @Date 2020/9/14 18:27
     */
    boolean openCommand(QqGroup qqGroup);

    /**
     * 添加命令，如果command存在的话则为更新
     *
     * @param qqGroup   群组
     * @param commandId 命令id
     * @param power     权限列表
     * @return 修改记录条数
     * @Title addCommand
     * @Description 添加命令
     * @author 张逸辰
     * @Date 2020/8/27 17:41
     */
    int addCommand(QqGroup qqGroup, String commandId, List<String> power);

    /**
     * 删除群组命令
     *
     * @param qqGroup   群组
     * @param commandId 命令ID
     * @return 修改记录条数
     * @Title deleteCommand
     * @Description 删除命令
     * @author 张逸辰
     * @Date 2020/8/27 17:42
     */
    int deleteCommand(QqGroup qqGroup, String commandId);


    /**
     * 查询群组命令
     *
     * @param qqGroup 群组
     * @return 群组, 内含List<object>第一个 String命令
     * 第二个 List<String>权限
     * @Title selectCommand
     * @Description 查询群组命令
     * @author 张逸辰
     * @Date 2020/8/27 17:45
     */
    List<List<Object>> selectCommand(QqGroup qqGroup);

    /**
     * 查询群组命令
     * @Title selectCommandVo
     * @Description 查询群组命令
     * @param qqGroup 群组
     * @return  群组命令
     * @author 张逸辰
     * @Date 2020/8/30 23:01
     */
    CommandVo selectCommandVo(QqGroup qqGroup);


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
    GroupFunction selectGroupFunction(QqGroup qqGroup);

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
    int updateGroupFunction(GroupFunction groupFunction);
}
