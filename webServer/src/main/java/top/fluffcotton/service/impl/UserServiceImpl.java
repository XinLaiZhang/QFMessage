package top.fluffcotton.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fluffcotton.mapper.UserMapper;
import top.fluffcotton.pojo.User;
import top.fluffcotton.pojo.UserExample;
import top.fluffcotton.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: UserControlImpl
 * @Description: 用户管理类
 * @date 2020.08.28 16:45
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用于查询与添加用户
     */
    @Autowired
    private UserMapper userMapper;

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
    @Override
    public int insertUser(User user) {
        if(user.getId() == null)
        {
            user.setId(UUID.randomUUID().toString());
        }
        return userMapper.insertSelective(user);
    }

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
    @Override
    public int deleteUser(User user) {
        return userMapper.deleteByPrimaryKey(user.getId());
    }

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
    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

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
    @Override
    public User selectUserByUsername(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            return null;
        }
        return users.get(0);
    }

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
    @Override
    public User selectUserByQq(String qq) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andQqEqualTo(qq);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    /**
     * 根据用户名/qq查询用户
     *
     * @param username 用户名或qq
     * @return 用户，不存在 null
     * @Title selectUser
     * @Description 根据用户名/qq查询用户
     * @author 张逸辰
     * @Date 2020/8/28 16:51
     */
    @Override
    public User selectUserByUsernameOrQq(String username) {
        //根据用户名查询用户
        UserExample userExample = new UserExample();
        userExample.or().andQqEqualTo(username);
        userExample.or().andUsernameEqualTo(username);
        userExample.or(userExample.createCriteria());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0){
            return null;
        }
        return users.get(0);
    }
}
