package top.fluffcotton.service;

import top.fluffcotton.pojo.User;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: UserControl
 * @Description: 用户操作类
 * @date 2020.08.28 16:33
 */
public interface UserService {

    /**
     * 新增用户
     *
     * @param user 用户
     * @return 修改数据库行数
     * @Title insertUser
     * @Description 新增用户
     * @author 张逸辰
     * @Date 2020/8/28 16:35
     */
    int insertUser(User user);

    /**
     * 删除用户
     *
     * @param user 用户
     * @return 修改数据库行数
     * @Title deleteUser
     * @Description 删除用户
     * @author 张逸辰
     * @Date 2020/8/28 16:36
     */
    int deleteUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 更新后的用户信息
     * @return 修改数据库行数
     * @Title updateUser
     * @Description 更新用户信息
     * @author 张逸辰
     * @Date 2020/8/28 16:36
     */
    int updateUser(User user);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户，不存在则为null
     * @Title selectUserByUsername
     * @Description 通过用户名查询用户
     * @author 张逸辰
     * @Date 2020/8/28 16:37
     */
    User selectUserByUsername(String username);

    /**
     * 通过qq查询用户
     *
     * @param qq qq号
     * @return 用户，不存在为null
     * @Title selectUserByQq
     * @Description 通过qq查询用户
     * @author 张逸辰
     * @Date 2020/8/28 16:37
     */
    User selectUserByQq(String qq);

    /**
     * 根据用户名/qq查询用户
     *
     * @param username 用户名或qq
     * @return 用户，不存在 null
     * @Title selectUser
     * @Description  根据用户名/qq查询用户
     * @author 张逸辰
     * @Date 2020/8/28 16:51
     */
    User selectUserByUsernameOrQq(String username);

}
