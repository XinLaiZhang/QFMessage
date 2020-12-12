package top.fluffcotton.mapper;

import org.apache.ibatis.annotations.Param;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.pojo.QqGroupExample;

import java.util.List;

public interface QqGroupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    long countByExample(QqGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByExample(QqGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insert(QqGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insertSelective(QqGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    List<QqGroup> selectByExample(QqGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    QqGroup selectByPrimaryKey(String groupid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExampleSelective(@Param("record") QqGroup record, @Param("example") QqGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExample(@Param("record") QqGroup record, @Param("example") QqGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKeySelective(QqGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKey(QqGroup record);
}