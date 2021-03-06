package top.fluffcotton.mapper;

import org.apache.ibatis.annotations.Param;
import top.fluffcotton.pojo.GroupUser;
import top.fluffcotton.pojo.GroupUserExample;

import java.util.List;

public interface GroupUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    long countByExample(GroupUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByExample(GroupUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insert(GroupUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insertSelective(GroupUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    List<GroupUser> selectByExample(GroupUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    GroupUser selectByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExampleSelective(@Param("record") GroupUser record, @Param("example") GroupUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExample(@Param("record") GroupUser record, @Param("example") GroupUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKeySelective(GroupUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_user
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKey(GroupUser record);
}