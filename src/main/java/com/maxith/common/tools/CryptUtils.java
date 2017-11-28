package com.maxith.common.tools;

import java.security.MessageDigest;

/**
 * 加解密工具
 * Created by zhouyou on 2017/6/2.
 */
public class CryptUtils {

    /**
     * md5 盐key
     */
    public static final String SALT_KEY = "buestc";

    /**
     * 消息摘要算法名称：SHA1
     */
    public static final String DIGEST_SHA1 = "SHA1";

    /**
     * 消息摘要算法名称：MD5
     */
    public static final String DIGEST_MD5 = "MD5";


    /**
     * 计算消息摘要值
     * MD5
     *
     * @param text
     * @param ts 时间戳
     * @return - 16进制字符串
     * @throws Exception
     */
    public static String generateDigestWithMD5(String text,long ts) {
        return generateDigest(DIGEST_MD5, text,ts);
    }

    public static String generateDigestWithMD5(String text) {
        return generateDigest(DIGEST_MD5, text);
    }

    /**
     * 计算消息摘要值
     * SHA1
     *
     * @param text
     * @param ts 时间戳
     * @return - 16进制字符串
     * @throws Exception
     */
    public static String generateDigestWithSHA1(String text,long ts) {
        return generateDigest(DIGEST_SHA1, text,ts);
    }

    public static String generateDigestWithSHA1(String text) {
        return generateDigest(DIGEST_SHA1, text);
    }

    /**
     * 计算消息摘要值
     * 不加时间戳
     *
     * @param algorithm
     * @param text
     * @return
     */
    public static String generateDigest(String algorithm, String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] value = digest.digest(text.getBytes("UTF-8"));

            //数据加盐再封装
            byte[] temp = DataUtils.bytesMerge(value,SALT_KEY.getBytes("UTF-8"));
            temp = digest.digest(temp);

            return parseByteToHexString(temp).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    /**
     * 计算消息摘要值
     *
     * @param algorithm - 算法，如：MD5, SHA1，推荐使用SHA1
     * @param text
     * @param ts 时间戳
     * @return - 16进制字符串
     * @throws Exception
     */
    public static String generateDigest(String algorithm, String text,long ts) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] value = digest.digest(text.getBytes("UTF-8"));

            //数据加盐再封装
            byte[] temp = DataUtils.bytesMerge(value,SALT_KEY.getBytes("UTF-8"));
            temp = digest.digest(temp);

            //结果加时间戳再封装
            byte[] result = DataUtils.bytesMerge(temp,DataUtils.long2Bytes(ts));
            result = digest.digest(result);

            return parseByteToHexString(result).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * 将字节数组转换为16进制字符串
     *
     * @param content
     * @return
     * @throws Exception
     * @author Julian
     */
    public static String parseByteToHexString(byte[] content) {
        if (content == null) throw new IllegalArgumentException("the parameter is null.");

        String hex = null;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < content.length; i++) {
            hex = Integer.toHexString(content[i] & 0xFF);
            if (hex.length() == 1) hex = '0' + hex;

            buffer.append(hex.toUpperCase());
        }

        return buffer.toString();
    }

    public static void main(String[] args) {
        String result = CryptUtils.generateDigestWithMD5("12345678");

        System.out.println(result);
    }
}
