package com.kmzyc.search.facade.constants;

/**
 * @title 系统常量类
 * @describtion 存放系统所有常量
 * @author Administrator
 * @date 2013-09-24
 */
public class Constants {

    /**
     * 从session中获取用户信息的属性名称
     */
    public static final String SESSION_USER_KEY = "user";

    /**
     * session用户ID
     */
    public static final String SESSION_USER_ID = "b2b_sessionUserId";

    /**
     * session用户名
     */
    public static final String SESSION_USER_NAME = "b2b_sessionUserName";

    /**
     * session昵称
     */
    public static final String SESSION_NICK_NAME = "b2b_sessionNickName";

    /**
     * COOKIE SESSION ID
     */
    public static final String COOKIE_SESSION_ID = "b2b_cookieSessionId";

    /**
     * COOKIE用户名
     */
    public static final String COOKIE_USER_NAME = "b2b_cookieUserName";
    /**
     * COOKIE昵称
     */
    public static final String COOKIE_NICK_NAME = "b2b_cookieNickName";

    /**
     * 从session中获取临时用户信息的属性名称
     */
    public static final String UNLOGIN_SESSION_USER_KEY = "unloginuser";

    /**
     * 远程系统编号：订单系统
     */
    public static final String REMOTE_SERVICE_ORDER = "01";

    /**
     * 远程系统编号：产品系统
     */
    public static final String REMOTE_SERVICE_PRODUCT = "02";

    /**
     * 远程系统编号：客户系统
     */
    public static final String REMOTE_SERVICE_CUSTOMER = "03";

    /**
     * 远程系统编号：CMS系统
     */
    public static final String REMOTE_SERVICE_CMS = "04";

    /**
     * 远程系统编号：促销系统
     */
    public static final String REMOTE_SERVICE_PROMOTION = "11";
    /**
     * 编码
     */
    public static final String defaultCharacter = "UTF-8";
    /**
     * 请求渠道参数
     */
    public static final String requestChannel = "request.site.param";

    /**
     * 20150616 add 康美自营的shopCode默认为221
     */
    public static final String KM_SHOP_CODE = "221";

}
