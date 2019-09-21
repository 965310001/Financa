package com.ph.financa.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.ContactColumnBean;
import com.ph.financa.constant.Constant;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 客户
 */
public class CustomerFragment extends BaseTitleFragment {

    private static final String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
            ApiConstant.CUSTOMER,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));

    private AgentWeb mAgentWeb;

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (null!=mContentView) {
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
            StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        }
        super.onVisibleChanged(isVisibleToUser);
        Log.i(TAG, "onVisibleChanged: ");
    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(URL)) {
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(URL);

            mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, getContext()));
        }
    }

    class AndroidInterface extends Object {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void importAddressBook(String content) {
            Log.i(TAG, "importAddressBook: " + content);
            PermissionManager.instance().request(getActivity(), new OnPermissionCallback() {
                @Override
                public void onRequestAllow(String permissionName) {
                    insertContact(getPhoneContacts());
                }

                @Override
                public void onRequestRefuse(String permissionName) {
                    Log.i(TAG, "onRequestRefuse: " + permissionName);
                }

                @Override
                public void onRequestNoAsk(String permissionName) {
                    Log.i(TAG, "onRequestNoAsk: " + permissionName);
                }
            }, Manifest.permission.READ_CONTACTS);

        }
    }

    /*从通讯录导入*/
    private List<ContactColumnBean> getPhoneContacts() {
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            List<ContactColumnBean> beanList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); // id
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // 姓名
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 电话
                beanList.add(new ContactColumnBean(id, name, number));
                Log.i(TAG, "getPhoneContacts: " + id + " " + name + " " + number);
            }
            cursor.close();
            return beanList;
        }
        return null;
    }

    /*从通讯录导入*/
    private void insertContact(List<ContactColumnBean> beans) {
        if (null != beans && beans.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ContactColumnBean bean : beans) {
                Map<String, Object> map = new HashMap<>();
                map.put("customerName", bean.getName());
                map.put("telephone", Arrays.asList(bean.getNumber()));
                jsonArray.put(new JSONObject(map));
            }
            ViseHttp.POST(ApiConstant.INSERT_CONTACT)
                    .setJson(jsonArray)
                    .request(new ACallback<BaseTResp2>() {
                        @Override
                        public void onSuccess(BaseTResp2 data) {
                            if (data.isSuccess()) {
                                ToastUtil.show("上传成功！");
                                loadData();
                            } else {
                                ToastUtil.show(data.getMsg());
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show(errMsg);
                        }
                    });
        } else {
            ToastUtil.show("通讯录为空");
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_customer;
    }
}
