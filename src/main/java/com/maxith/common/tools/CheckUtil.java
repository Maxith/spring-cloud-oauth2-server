package com.maxith.common.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串校验工具类
 * Created by zhouyou on 2017/5/19.
 */
public class CheckUtil {

    /**
     * 15位身份证号校验正则表达式
     */
    public static final String REGULAR_ID_CARD_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 18位身份证号校验正则表达式
     */
    public static final String REGULAR_ID_CARD_18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";
    /**
     * 纯数字验证
     */
    public static final String REGULAR_NUMBER = "^[0-9]*$";
    /**
     * 身份证号验证
     * @param idCard
     */
    public static boolean checkIDCard(String idCard){
        Pattern pattern;
        if(idCard.length() == 15){
            pattern = Pattern.compile(REGULAR_ID_CARD_15);
        }else if (idCard.length() == 18){
            pattern = Pattern.compile(REGULAR_ID_CARD_18);
        }else{
            return false;
        }
        Matcher matcher = pattern.matcher(idCard);
        return matcher.matches();
    }

    /**
     * 验证纯数字
     * @param number
     * @return
     */
    public static boolean checkNumber(String number){
        Pattern pattern = Pattern.compile(REGULAR_NUMBER);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
