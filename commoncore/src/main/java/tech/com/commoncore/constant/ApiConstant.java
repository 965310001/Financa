package tech.com.commoncore.constant;

/**
 * Anthor:HeChuan
 * Time:2018/11/5
 * Desc:
 */
public interface ApiConstant {

    String BASE_URL_ZP = "http://wealth.phscitech.com/";//生产环境
//    String BASE_URL_ZP = "http://develop.phscitech.com/";//开发环境

    String AGREEMENT = "h5/user-agreement";//隐私政策
    String PROTOCOL = "h5/privacy-policy";//用户协议

    /*首页*/
    String H5 = "h5/";/*首页URL：*/
    /*文章详情*/
    String ARTICLE = "h5/article/";
    /*文章编辑*/
    String edit = "edit/";
    /*插入产品*/
    String insert = "insert";
    /*文章详情----微信访问：*/
    String wxarticle = "wxarticle";
    /*添加文章*/
    String ADDARTICLE = "h5/add-article";
    /*历史分享文章*/
    String HISTORYARTICLE = "h5/history-article";


    /*谁看了我*/
    /*分享*/
    String SHARE = "h5/share";
    /*转发*/
    String FORWARD = "h5/forward";
    /*转发详情*/
    String FORWARD_DETAIL = "h5/forward-detail";
    /*访客*/
    String VISIT = "h5/visit";
    /*访客画像*/
    String VISIT_PICTURE = "h5/visit-picture";
    /*浏览详情*/
    String SHARE_DETAIL = "h5/share-detail";


    /*客户*/
    String CUSTOMER = "h5/customer";
    /*新建客户*/
    String ADD_CUSTOMER = "h5/add-customer";
    /*新建分组*/
    String CUSTOMER_GROUP = "h5/customer-group";
    /*客户分组*/
    String visit_leading_in = "h5/visit-leading-in";

    /*前往访客画像*/
    String BROWSE_DETAIL = "h5/browse-detail";

    /*前往新增客户*/
    String VISIT_ADD = "h5/visit-add";

    /*批量或单个删除客户*/
    String DELETE_BATCH = "api/customer/delete/batch";


    /*我的*/
    String mine = "h5/mine";
    /*我的收藏*/
    String COLLECTION = "h5/collection";
    /*消息通知*/
    String MESSAGE = "h5/message";
    /*公告通知*/
    String MESSAGE_NOTICE = "h5/message-notice";
    /*消息详情*/
    String message_detail = "h5/message-detail";
    /*个人名片*/
    String MY_CARD = "h5/my-card";
    /*微信名片*/
    String wx_my_card = "h5/wx-my-card";
    /*编辑名片*/
    String edit_my_card = "h5/edit-my-card";
    /*我的产品*/
    String MY_PRODUCT = "h5/my-product";
    /*产品详情*/
    String product_detail = "h5/product-detail";
    /*资料库*/
    String DATA_LIB = "h5/data-lib";
    /*怎么上传——帮助*/
    String how_to_upload = "h5/how-to-upload";

    /*发送验证码*/
    String SEND_CODE = "/api/phone/send";

    /*登录*/
    String LOGIN = "/api/login";

    /*验证验证码*/
    String VERIFICATION_CODE = "/api/phone/verify";
    /*输入公司*/
    String EDIT_COMPANY = "/api/update/companyname";


    /*退出登录*/
    String LOGINOUT = "/api/loginout";

    /*套餐模块-判断是否已开通vip套餐服务*/
    String IS_VIP = "api/order_package/is_vip";

    /*支付*/
    String PAY = "api/order/pay";

    /*订单模块-创建订单*/
    String INSERT = "api/order/insert";
    /*套餐模板模块-根据服务类型查询对应所有订单模板*/
    String ORDER_TEMPLATE_SELECT = "api/order_template/select";

    /*查询谁看了我列表*/
    String CUSTOMER_LIST = "api/customer/list";

    /*从通讯录导入*/
    String INSERT_CONTACT = "api/customer/insert/batch/contact";

    /*分享文章成功回调接口*/
    String SHARE_SUCCESS = "api/cms/share/success";

    /*更换手机号*/
    String UPDATE_PHONE = "api/phone/update";

    /*获取用户信息接口*/
    String GET_USER = "api/user/get";

    /*套餐申请模块-体验和团购发起申请接口*/
    String PACKAGE_APPLY_APPLY = "api/package_apply/apply";

    /*粘贴文章链接*/
    String PASTEARTICLE = "h5/copy-article";

    /*消息模块-用户拉取信息*/
    String MESSAGE_PULL = "api/message/pull";

    /*消息模块-获取用户未读统计*/
    String MESSAGE_COUNT = "api/message/count";

    /*H5支付页面*/
    String PAYMENT = "h5/payment";

    /*是否隐藏游客登录  yes隐藏*/
    String VISITORLOGIN="api/pay/ios";

    /*登录*/
    String ANONYMOUS_LOGIN="api/anonymous/login";

}
