package top.fluffcotton.mapper;

import org.apache.ibatis.annotations.Param;
import top.fluffcotton.pojo.Mission;
import top.fluffcotton.pojo.MissionExample;

import java.util.List;

public interface MissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    long countByExample(MissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByExample(MissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int deleteByPrimaryKey(String mId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insert(Mission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int insertSelective(Mission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    List<Mission> selectByExample(MissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    Mission selectByPrimaryKey(String mId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExampleSelective(@Param("record") Mission record, @Param("example") MissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByExample(@Param("record") Mission record, @Param("example") MissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKeySelective(Mission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mission
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    int updateByPrimaryKey(Mission record);
}