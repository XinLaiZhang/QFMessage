package top.fluffcotton.pojo;

import java.util.ArrayList;
import java.util.List;

public class QqGroupExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public QqGroupExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andGroupidIsNull() {
            addCriterion("groupID is null");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNotNull() {
            addCriterion("groupID is not null");
            return (Criteria) this;
        }

        public Criteria andGroupidEqualTo(String value) {
            addCriterion("groupID =", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotEqualTo(String value) {
            addCriterion("groupID <>", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThan(String value) {
            addCriterion("groupID >", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThanOrEqualTo(String value) {
            addCriterion("groupID >=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThan(String value) {
            addCriterion("groupID <", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThanOrEqualTo(String value) {
            addCriterion("groupID <=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLike(String value) {
            addCriterion("groupID like", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotLike(String value) {
            addCriterion("groupID not like", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidIn(List<String> values) {
            addCriterion("groupID in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotIn(List<String> values) {
            addCriterion("groupID not in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidBetween(String value1, String value2) {
            addCriterion("groupID between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotBetween(String value1, String value2) {
            addCriterion("groupID not between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGrouplistIsNull() {
            addCriterion("groupList is null");
            return (Criteria) this;
        }

        public Criteria andGrouplistIsNotNull() {
            addCriterion("groupList is not null");
            return (Criteria) this;
        }

        public Criteria andGrouplistEqualTo(String value) {
            addCriterion("groupList =", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistNotEqualTo(String value) {
            addCriterion("groupList <>", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistGreaterThan(String value) {
            addCriterion("groupList >", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistGreaterThanOrEqualTo(String value) {
            addCriterion("groupList >=", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistLessThan(String value) {
            addCriterion("groupList <", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistLessThanOrEqualTo(String value) {
            addCriterion("groupList <=", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistLike(String value) {
            addCriterion("groupList like", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistNotLike(String value) {
            addCriterion("groupList not like", value, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistIn(List<String> values) {
            addCriterion("groupList in", values, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistNotIn(List<String> values) {
            addCriterion("groupList not in", values, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistBetween(String value1, String value2) {
            addCriterion("groupList between", value1, value2, "grouplist");
            return (Criteria) this;
        }

        public Criteria andGrouplistNotBetween(String value1, String value2) {
            addCriterion("groupList not between", value1, value2, "grouplist");
            return (Criteria) this;
        }

        public Criteria andPowerIsNull() {
            addCriterion("power is null");
            return (Criteria) this;
        }

        public Criteria andPowerIsNotNull() {
            addCriterion("power is not null");
            return (Criteria) this;
        }

        public Criteria andPowerEqualTo(Byte value) {
            addCriterion("power =", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotEqualTo(Byte value) {
            addCriterion("power <>", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThan(Byte value) {
            addCriterion("power >", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerGreaterThanOrEqualTo(Byte value) {
            addCriterion("power >=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThan(Byte value) {
            addCriterion("power <", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerLessThanOrEqualTo(Byte value) {
            addCriterion("power <=", value, "power");
            return (Criteria) this;
        }

        public Criteria andPowerIn(List<Byte> values) {
            addCriterion("power in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotIn(List<Byte> values) {
            addCriterion("power not in", values, "power");
            return (Criteria) this;
        }

        public Criteria andPowerBetween(Byte value1, Byte value2) {
            addCriterion("power between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andPowerNotBetween(Byte value1, Byte value2) {
            addCriterion("power not between", value1, value2, "power");
            return (Criteria) this;
        }

        public Criteria andIsbanIsNull() {
            addCriterion("isBan is null");
            return (Criteria) this;
        }

        public Criteria andIsbanIsNotNull() {
            addCriterion("isBan is not null");
            return (Criteria) this;
        }

        public Criteria andIsbanEqualTo(Boolean value) {
            addCriterion("isBan =", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanNotEqualTo(Boolean value) {
            addCriterion("isBan <>", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanGreaterThan(Boolean value) {
            addCriterion("isBan >", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isBan >=", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanLessThan(Boolean value) {
            addCriterion("isBan <", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanLessThanOrEqualTo(Boolean value) {
            addCriterion("isBan <=", value, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanIn(List<Boolean> values) {
            addCriterion("isBan in", values, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanNotIn(List<Boolean> values) {
            addCriterion("isBan not in", values, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanBetween(Boolean value1, Boolean value2) {
            addCriterion("isBan between", value1, value2, "isban");
            return (Criteria) this;
        }

        public Criteria andIsbanNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isBan not between", value1, value2, "isban");
            return (Criteria) this;
        }

        public Criteria andGroupnameIsNull() {
            addCriterion("groupName is null");
            return (Criteria) this;
        }

        public Criteria andGroupnameIsNotNull() {
            addCriterion("groupName is not null");
            return (Criteria) this;
        }

        public Criteria andGroupnameEqualTo(String value) {
            addCriterion("groupName =", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotEqualTo(String value) {
            addCriterion("groupName <>", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThan(String value) {
            addCriterion("groupName >", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThanOrEqualTo(String value) {
            addCriterion("groupName >=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThan(String value) {
            addCriterion("groupName <", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThanOrEqualTo(String value) {
            addCriterion("groupName <=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLike(String value) {
            addCriterion("groupName like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotLike(String value) {
            addCriterion("groupName not like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameIn(List<String> values) {
            addCriterion("groupName in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotIn(List<String> values) {
            addCriterion("groupName not in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameBetween(String value1, String value2) {
            addCriterion("groupName between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotBetween(String value1, String value2) {
            addCriterion("groupName not between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andIsrootIsNull() {
            addCriterion("isRoot is null");
            return (Criteria) this;
        }

        public Criteria andIsrootIsNotNull() {
            addCriterion("isRoot is not null");
            return (Criteria) this;
        }

        public Criteria andIsrootEqualTo(Boolean value) {
            addCriterion("isRoot =", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootNotEqualTo(Boolean value) {
            addCriterion("isRoot <>", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootGreaterThan(Boolean value) {
            addCriterion("isRoot >", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isRoot >=", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootLessThan(Boolean value) {
            addCriterion("isRoot <", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootLessThanOrEqualTo(Boolean value) {
            addCriterion("isRoot <=", value, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootIn(List<Boolean> values) {
            addCriterion("isRoot in", values, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootNotIn(List<Boolean> values) {
            addCriterion("isRoot not in", values, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootBetween(Boolean value1, Boolean value2) {
            addCriterion("isRoot between", value1, value2, "isroot");
            return (Criteria) this;
        }

        public Criteria andIsrootNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isRoot not between", value1, value2, "isroot");
            return (Criteria) this;
        }

        public Criteria andDeletetimeIsNull() {
            addCriterion("deleteTime is null");
            return (Criteria) this;
        }

        public Criteria andDeletetimeIsNotNull() {
            addCriterion("deleteTime is not null");
            return (Criteria) this;
        }

        public Criteria andDeletetimeEqualTo(Long value) {
            addCriterion("deleteTime =", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeNotEqualTo(Long value) {
            addCriterion("deleteTime <>", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeGreaterThan(Long value) {
            addCriterion("deleteTime >", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeGreaterThanOrEqualTo(Long value) {
            addCriterion("deleteTime >=", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeLessThan(Long value) {
            addCriterion("deleteTime <", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeLessThanOrEqualTo(Long value) {
            addCriterion("deleteTime <=", value, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeIn(List<Long> values) {
            addCriterion("deleteTime in", values, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeNotIn(List<Long> values) {
            addCriterion("deleteTime not in", values, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeBetween(Long value1, Long value2) {
            addCriterion("deleteTime between", value1, value2, "deletetime");
            return (Criteria) this;
        }

        public Criteria andDeletetimeNotBetween(Long value1, Long value2) {
            addCriterion("deleteTime not between", value1, value2, "deletetime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table qq_group
     *
     * @mbg.generated do_not_delete_during_merge Mon Sep 14 19:04:24 CST 2020
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table qq_group
     *
     * @mbg.generated Mon Sep 14 19:04:24 CST 2020
     */
    public static class Criterion {
        private final String condition;
        private final String typeHandler;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}