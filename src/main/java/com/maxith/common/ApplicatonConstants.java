package com.maxith.common;

/**
 * 应用常量
 *
 * @author Xiong Hao
 */
public class ApplicatonConstants {

    public static String APP_OK_CODE = "000000";
    public static String APP_OK_RESULT = "操作成功";
    public static String APP_EXPIRE_CODE = "000001";
    public static String APP_EXPIRE_RESULT = "授权过期,请重新登录";
    public static String APP_ERROR_CODE = "000002";
    public static String APP_ERROR_RESULT = "操作失败";

    public static String APP_UNAUTHORIZED_CODE = "000003";
    public static String APP_UNAUTHORIZED_RESULT = "未授权的访问";

    public static final String API_SUCCESS_CODE = "SUCCESS";

    public static final String LOGIN_SUCCESS_MSG = "用户名或密码错误";

    public static final String LOGIN_ERROR = "login_error";
    public static final String LOGIN_ERRER_MSG = "用户名或密码错误";
    public static final Integer MAX_EXCEL_COUNT = 5000;


    /**
     * session存放系统用户的name
     */
    public static final String SESSION_USER = "session_user";

    /**
     * session 存放客户端信息的name
     */
    public static final String SESSION_CLIENT_ID = "session_client_id";

    /**
     * 是否授权
     */
    public static final String IS_APPROVAL = "isApproval";
    /**
     * oauth登录
     */
    public static final String OAUTH_LOGIN_VIEW = "oauth.login.view";
    /**
     * oauth授权
     */
    public static final String OAUTH_APPROVAL_VIEW = "oauth.approval.view";
    /**
     * oauth 授权关键字
     */
    public static final String OAUTH_APPROVAL_KEYWORD = "oauth.approval.keyword";
    /**
     * 基础文件路径
     */
    public static final String FILE_BASE_PATH = "file.base.path";

    /**
     * redis的host
     */
    public static final String REDIS_URL = "spring.redis.host";
    /**
     * redis的port
     */
    public static final String REDIS_PORT = "spring.redis.port";
    /**
     * redis使用的数据库
     */
    public static final String REDIS_DATABASE = "spring.redis.database";
    /**
     * redis过期时间
     */
    public static final String REDIS_TIME_OUT = "spring.redis.timeout";

    /**
     * 系统加载的配置文件
     */
    public static final String PROFILES_ACTIVE = "spring.profiles.active";
    /**
     * 是否开启单一用户登录
     */
    public static final String SINGLE_USER_LOGIN = "single.user.login";

}
