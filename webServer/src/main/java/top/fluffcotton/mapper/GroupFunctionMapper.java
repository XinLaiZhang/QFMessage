package top.fluffcotton.mapper;

import org.apache.ibatis.annotations.Param;
import top.fluffcotton.pojo.GroupFunction;
import top.fluffcotton.pojo.GroupFunctionExample;

import java.util.List;

public interface GroupFunctionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    long countByExample(GroupFunctionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByExample(GroupFunctionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insert(GroupFunction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insertSelective(GroupFunction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    List<GroupFunction> selectByExample(GroupFunctionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    GroupFunction selectByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExampleSelective(@Param("record") GroupFunction record, @Param("example") GroupFunctionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExample(@Param("record") GroupFunction record, @Param("example") GroupFunctionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKeySelective(GroupFunction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table group_function
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKey(GroupFunction record);
}