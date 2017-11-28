package com.maxith.common.tools;

import java.nio.ByteBuffer;

/**
 * 数据工具
 * Created by zhouyou on 2017/6/2.
 */
public class DataUtils {

    /**
     * 数组合并
     * @param byte_1
     * @param byte_2
     * @return
     */
    public static byte[] bytesMerge(byte[] byte_1,byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 长整形转字节数组
     * @param data
     * @return
     */
    public static byte[] long2Bytes(long data){
        //默认16位字符数组
        ByteBuffer buffer = ByteBuffer.allocate(16);
        return buffer.putLong(data).array();
    }
}
