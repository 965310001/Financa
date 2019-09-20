package tech.com.commoncore.constant;

/**
 * Anthor:HeChuan
 * Time:2018/11/5
 * Desc:
 */
public class ApiConstant {

    public static final String BASE_URL_ZP = "http://wealth.phscitech.com/";

    //    public static final String Account_Send_messages = "user/sendPhoneMessage";//发送注册或登录短信(获取验证码)
//    public static final String Account_register = "user/phoneLogin";//短信登录或注册登陆
//    public static final String UerfectUserDetail = "user/perfectUserDetail";//完善个人信息
//    public static final String searchNearInBlood = "user/searchNearInBlood";//近亲列表
//    public static final String delUser = "user/delUser";//删除个人信息
//    public static final String editUser = "user/editUser";//编辑个人信息
//    public static final String inviteUser = " user/inviteUser";//邀请加入
//    public static final String setAsCenter = "user/setAsCenter";//设为中心，父系列表
//    public static final String addUsers = "user/addUser";//添加关系
//    public static final String searchUserDeatil = "user/searchUserDeatil";//查看详情
//    public static final String getRelationshipChain = "user/getRelationshipChain";//查看关系链
//    public static final String addDeeds = "deeds/addDeeds";//添加事迹
//    public static final String searchClan = "user/searchClan";//搜索
//
//    public static final String familyBook = "familyBook/search";//查询族册
//    public static final String familyBook_edit = "familyBook/edit";//编辑族册
//    public static final String familyBook_uploadImg = "familyBook/uploadImg";//家族照上传
//    public static final String familyBook_delImg = "familyBook/delImg";//删除家族照
//    public static final String familyBook_editImg = "familyBook/editImg";//录入简介-家族照重新上传
//
//    public static final String album_searchMyAlbum = "album/searchMyAlbum";//我的相册
//    public static final String album_searchFamilyAlbum = "album/searchFamilyAlbum";//家族相册
//    public static final String album_create = "album/create";//创建相册
//    public static final String album_edit = "album/edit";//修改相册
//    public static final String album_del = "album/del";//删除相册
//    public static final String album_uploadImgs = "album/uploadImgs";//图片上传
//    public static final String album_delImgs = "album/delImgs";//删除照片
//    public static final String log_search = "log/search";//日志
//
//
//    public static final String searchContactPerson = "user/searchContactPerson";//环信通讯录
//
//
//    public static final String GETWUFU = "user/getWuFu";//五服
//
//
//    public static final String ISREGISTER = "user/isRegister";//是否注册

    public static final String AGREEMENT = "xxx";//隐私政策
    public static final String PROTOCOL = "xxx";//用户协议

    /*首页*/
    public static final String H5 = "h5/";/*首页URL：*/
    /*文章详情*/
    public static final String article = "h5/article/:id";
    /*文章编辑*/
    public static final String edit = "edit/";
    /*插入产品*/
    public static final String insert = "insert";
    /*文章详情----微信访问：*/
    public static final String wxarticle = "wxarticle";
    /*添加文章*/
    public static final String addArticle = "add-article";
    /*历史分享文章*/
    public static final String HISTORYARTICLE = "h5/history-article";


    /*谁看了我*/
    /*分享*/
    public static final String SHARE = "h5/share";
    /*转发*/
    public static final String FORWARD = "h5/forward";
    /*转发详情*/
//    public static final String SHARE ="http://wealth.phscitech.com/h5/forward-detail";
    /*访客*/
    public static final String VISIT = "h5/visit";
    /*访客画像*/
//    public static final String SHARE ="http://wealth.phscitech.com/h5/visit-picture";
    /*浏览详情*/
//    public static final String SHARE ="http://wealth.phscitech.com/h5/browse-detail";


    /*客户*/
    public static final String CUSTOMER = "h5/customer";
    /*新建客户*/
    public static final String ADD_CUSTOMER = "h5/add-customer";
    /*新建分组*/
    public static final String CUSTOMER_GROUP = "h5/customer-group";
    /*客户分组*/
    public static final String visit_leading_in = "h5/visit-leading-in";

    /*批量或单个删除客户*/
    public static final String DELETE_BATCH = "api/customer/delete/batch";


    /*我的*/
    public static final String mine = "h5/mine";
    /*我的收藏*/
    public static final String COLLECTION = "h5/collection";
    /*消息通知*/
    public static final String message = "h5/message";
    /*公告通知*/
    public static final String MESSAGE_NOTICE = "h5/message-notice";
    /*消息详情*/
    public static final String message_detail = "h5/message-detail";
    /*个人名片*/
    public static final String MY_CARD = "h5/my-card";
    /*微信名片*/
    public static final String wx_my_card = "h5/wx-my-card";
    /*编辑名片*/
    public static final String edit_my_card = "h5/edit-my-card";
    /*我的产品*/
    public static final String MY_PRODUCT = "h5/my-product";
    /*产品详情*/
    public static final String product_detail = "h5/product-detail";
    /*资料库*/
    public static final String data_lib = "h5/data-lib";
    /*怎么上传——帮助*/
    public static final String how_to_upload = "h5/how-to-upload";

    /*发送验证码*/
    public static final String SEND_CODE = "/api/phone/send";

    /*登录*/
    public static final String LOGIN = "/api/login";

    /*验证验证码*/
    public static final String VERIFICATION_CODE = "/api/phone/verify";
    /*输入公司*/
    public static final String EDIT_COMPANY = "/api/update/companyname";


    /*退出登录*/
    public static final String LOGINOUT = "/api/loginout";

    /*套餐模块-判断是否已开通vip套餐服务*/
    public static final String IS_VIP = "api/order_package/is_vip";

    /*支付*/
    public static final String PAY = "api/order/pay";

    /*订单模块-创建订单*/
    public static final String INSERT = "api/order/insert";
    /*套餐模板模块-根据服务类型查询对应所有订单模板*/
    public static final String ORDER_TEMPLATE_SELECT = "api/order_template/select";

    /*查询谁看了我列表*/
    public static final String CUSTOMER_LIST = "api/customer/list";

    /*从通讯录导入*/
    public static final String INSERT_CONTACT = "api/customer/insert/batch/contact";

    /*分享文章成功回调接口*/
    public static final String SHARE_SUCCESS = "api/cms/share/success";

    /*更换手机号*/
    public static final String UPDATE_PHONE = "api/phone/update";

    /*获取用户信息接口*/
    public static final String GET_USER = "api/user/get";

    /*套餐申请模块-体验和团购发起申请接口*/
    public static final String PACKAGE_APPLY_APPLY = "api/package_apply/apply";
}
