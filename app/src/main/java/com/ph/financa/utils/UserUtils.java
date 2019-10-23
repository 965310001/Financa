package com.ph.financa.utils;

import com.ph.financa.activity.bean.UserBean;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.Utils;

/**
 * 用户保存数据
 */
public class UserUtils {

    /*保存用户数据*/
    public static void saveUser(UserBean data) {
        if (null != data) {
            SPHelper.setStringSF(Utils.getContext(), Constant.USERNAME, data.getName());
            SPHelper.setStringSF(Utils.getContext(), Constant.USERCOMPANYNAME, data.getCompanyName());
            SPHelper.setStringSF(Utils.getContext(), Constant.USERHEAD, data.getHeadImgUrl());
            SPHelper.setStringSF(Utils.getContext(), Constant.USERID, String.valueOf(data.getId()));
            SPHelper.setStringSF(Utils.getContext(), Constant.USERPHONE, data.getTelephone());

            SPHelper.setIntergerSF(Utils.getContext(), Constant.ISVIP, data.getUserType());
        }
    }

    /*退出登录*/
    public static void logout() {
        SPHelper.clearShareprefrence(Utils.getContext());
        SPHelper.setBooleanSF(Utils.getContext(), Constant.ISGUIDE, true);
        SPHelper.setBooleanSF(Utils.getContext(), Constant.ISLOGIN, false);
    }
}
