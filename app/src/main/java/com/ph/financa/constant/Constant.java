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
    String WEIXIN_APP_ID = WECHATAPPKEY;

    int GET_TOKEN = 1;
    int CHECK_TOKEN = 2;
    int REFRESH_TOKEN = 3;
    int GET_INFO = 4;
    int GET_IMG = 5;
    /*支付宝APPID*/
    CharSequence AI_APP_ID = "2019050964432158";


    /*环信*/
    String EASEMOB_APPKEY = "";/*需要初始化清单文件*/

    /*支付价格*/
    String PRICE = "price";


}
