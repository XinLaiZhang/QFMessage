package top.fluffcotton.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;
/**
 * @author 张逸辰
 * @version V1.0
 * @Title: FileNameTools
 * @Description: 文件控制工具类，名称生成，路径生成，
 * @date 2020.08.31 20:16
 */
public class FileNameTools {


    /**
     * 获取随机文件名
     */
    public static String getUuidFilename(String filename) {
        String extetions = filename.substring(filename.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-", "") + extetions;
    }

    /**
     * 生成保存路径
     * @Title getRealPath
     * @Description 生成保存路径
     * @param uuidFilename 文件名
     * @return /8/6/类似
     * @author 张逸辰
     * @Date 2020/9/15 11:05
     */
    public static String getRealPath(String uuidFilename) {
        int code = uuidFilename.hashCode();
        String realPath = "/";
        for (int i = 0; i < 8; i++) {
            realPath += (code & 0xf) + "/";
            code >>>= 4;
        }
        return realPath;
    }
    /**
     * 获取文件md5
     * @Title getFileMD5
     * @Description 获取文件MD5
     * @param file 文件
     * @return MD5
     * @author 张逸辰
     * @Date 2020/9/15 11:05
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
