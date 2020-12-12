package top.fluffcotton.service;

/**   
 * @ClassName :  RSATool
 * @Description :RSA加密解密工具类
 * @author : 张逸辰
 * @date :   2020年5月23日 下午9:12:26
 */
public interface RSATool
{
	/**
	* 加密字符串
	* @Title encrypt
	* @Description RSA加密字符串
	* @param str 待加密字符串
	* @return 加密字符串
	* @throws: Exception
	 */
	String encrypt( String str) throws Exception;
	/**
	 * 解密字符串
	* @Title decrypt
	* @Description RSA解密字符串
	* @param str 待解密字符串
	* @return 明文字符串
	* @throws: Exception
	 */
	String decrypt(String str) throws Exception;
}
