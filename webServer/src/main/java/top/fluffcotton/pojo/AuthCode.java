package top.fluffcotton.pojo;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: AuthCode
 * @Description: 验证码类
 * @date 2020.08.26 16:29
 */
public class AuthCode implements Delayed {

    /**
     * 用于验证时间
     */
    private final long validity;
    /**
     * 接受验证码的qq
     */
    private String qq;
    /**
     * 提示语
     */
    private String tip;
    /**
     * 有效期单位分，范围1-60分钟
     */
    private long time;
    /**
     * 唯一标志
     */
    private Object sign;

    /**
     * 验证码
     */
    private String code;

    /**
     * 构造验证码pojo
     *
     * @param qq qq号
     * @param tip 提示信息
     * @param time 有效时间 范围1-60
     * @param sign 标志
     * @Title AuthCode
     * @Description 构造验证码pojo
     * @author 张逸辰
     * @Date 2020/8/26 16:39
     */
    public AuthCode(String qq, String tip, long time, Object sign) {
        if(time >= 1 && time <= 60){
            this.qq = qq;
            this.tip = tip;
            this.time = time*60*1000;
            this.sign = sign;
            this.validity = this.time + System.currentTimeMillis();
            this.code = null;
        }else {
            throw new RuntimeException("时间超出范围");
        }
    }

    /**
     * Returns the remaining delay associated with this object, in the
     * given time unit.
     *
     * @param unit the time unit
     * @return the remaining delay; zero or negative values indicate
     * that the delay has already elapsed
     */
    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return validity - System.currentTimeMillis();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Delayed o) {
        return o.getDelay(TimeUnit.MILLISECONDS) <= 0 ? -1 : 1;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getSign() {
        return sign;
    }

    public void setSign(Object sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
