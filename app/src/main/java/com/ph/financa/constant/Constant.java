package com.ph.financa.constant;

public interface Constant {

    /*是否登录*/
    String ISLOGIN = "LOGIN";

    /*用户Id*/
    String USERID = "USERID";

    /*用户手机号码*/
    String USERPHONE = "USER_PHONE";

    /*用户姓名*/
    String USERNAME = "USERNAME";

    /*用户公司名字*/
    String USERCOMPANYNAME = "USERCOMPANYNAME";

    /*用户的头像*/
    String USERHEAD = "USERHEAD";

    /*openId*/
    String WXOPENID = "openId";
    String WEIXINCODE = "WEIXINCODE";

    String WECHATAPPKEY = "wx7faa69a8d98bfb10";
    String WECHATAPPSECRET = "912a872b037cf863b9cf12371538d73f";
    /*String WEIXIN_APP_ID = WECHATAPPKEY;*/

    int GET_TOKEN = 1;
    int CHECK_TOKEN = 2;
    int REFRESH_TOKEN = 3;
    int GET_INFO = 4;
    int GET_IMG = 5;
    /*支付宝APPID*/
    CharSequence AI_APP_ID = "2019050964432158";


    /*环信*/
    String EASEMOB_APPKEY = "1104190909181743#radar";/*需要初始化清单文件*/

    /*支付价格*/
    String PRICE = "price";


    /*新浪微博*/
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    String APP_KEY = "2045436852";
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    String REDIRECT_URL = "http://www.sina.com";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";


    /*客服id*/
    String CUSTOMSERVICE="1173549052970016768";
}
