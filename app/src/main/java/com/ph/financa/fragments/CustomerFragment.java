package com.ph.financa.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ph.financa.R;
import com.ph.financa.activity.adpater.CustomerListAdapter;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.ContactColumnBean;
import com.ph.financa.activity.bean.CustomerBean;
import com.ph.financa.dialog.ListDialog;
import com.ph.financa.dialog.MoreDialog;
import com.ph.financa.dialog.SortNameDialog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.GetRequest;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.com.commoncore.base.BaseTitleRefreshLoadFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 客户
 */
public class CustomerFragment extends BaseTitleRefreshLoadFragment<CustomerBean> implements View.OnClickListener {

    private SortNameDialog mSortNameDialog;
    private MoreDialog moreDialog;
    private ListDialog mListDialog;
    private RelativeLayout mRlTitle;
    private LinearLayout mRlSearch;
    private AppCompatEditText mEtInput;
    private boolean isErr;

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.iv_search).setOnClickListener(this);
        mContentView.findViewById(R.id.iv_more).setOnClickListener(this);

        mContentView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        mRlTitle = mContentView.findViewById(R.id.rl_title);
        mRlSearch = mContentView.findViewById(R.id.rl_search);

        mEtInput = mContentView.findViewById(R.id.et_input);
        mEtInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent != null && keyEvent.getKeyCode()
                    == KeyEvent.KEYCODE_SEARCH) {
                Log.i(TAG, "onEditorAction: " + mEtInput.getText().toString());

                String text = mEtInput.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    getCustomerList(text, "");
                } else {
                    ToastUtil.show("请输入搜索");
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                showSearch(true);
                break;

            case R.id.iv_more:
                showMoreDialog();
                break;

            case R.id.tv_cancel:
                showSearch(false);
                break;

        }
    }

    private void showSearch(boolean isSearch) {
        if (isSearch) {
            mRlTitle.setVisibility(View.GONE);
            mRlSearch.setVisibility(View.VISIBLE);
        } else {
            mRlTitle.setVisibility(View.VISIBLE);
            mRlSearch.setVisibility(View.GONE);

            mEtInput.setText("");
        }
    }


    private void showMoreDialog() {
        moreDialog = MoreDialog.show(getFragmentManager(), "", index -> {
            switch (index) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    moreDialog.dismiss();
                    showSortDialog();
                    break;
                case 3:
                    showListDialog();
                    break;
            }
        });
    }

    private void showListDialog() {
        List<String> list = Arrays.asList("从谁看了我导入", "从通讯录导入", "取消");
        mListDialog = ListDialog.show(getFragmentManager(), "", list, (adapter, view, position) -> {
            mListDialog.dismiss();
            switch (position) {
                case 0:
//                    getCustomerList();
                    break;
                case 1:
                    getPhoneContacts();
                    break;
                case 2:
                    break;
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();

        PermissionManager.instance().request(getActivity(), new OnPermissionCallback() {
            @Override
            public void onRequestAllow(String permissionName) {
                Log.i(TAG, "onRequestAllow: " + permissionName);
                getPhoneContacts();
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

        getCustomerList("", "");
    }

    /*从通讯录导入*/
    private void getPhoneContacts() {
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
        }
    }

    /*从谁看了我导入*/
    private void getCustomerList(String customerName, String sort) {
        mStatusManager.showSuccessLayout();
        GetRequest request = ViseHttp.GET(ApiConstant.CUSTOMER_LIST);
        if (!TextUtils.isEmpty(customerName)) {
            request.addParam("customerName", customerName);
        }
        if (!TextUtils.isEmpty(sort)) {
            request.addParam("sort", sort);
        }
        request.request(new ACallback<BaseTResp2<List<CustomerBean>>>() {
            @Override
            public void onSuccess(BaseTResp2<List<CustomerBean>> data) {
                if (data.isSuccess()) {
                    setData(data.getData());
                } else {
                    mStatusManager.showErrorLayout();
                    isErr = true;
                    ToastUtil.show(data.getMsg());
                }
            }

            @Override
            public void onFail(int errCode, String errMsg) {
                mStatusManager.showErrorLayout();
                ToastUtil.show(errMsg);
                isErr = true;
            }
        });
    }

    private void showSortDialog() {
        mSortNameDialog = SortNameDialog.show(getFragmentManager(), "", index -> {
            mSortNameDialog.dismiss();
            switch (index) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_customer;
    }

    @Override
    public BaseQuickAdapter<CustomerBean, BaseViewHolder> getAdapter() {
        return new CustomerListAdapter(R.layout.item_customer_list);
    }

    @Override
    public void loadData(int page) {
        Log.i(TAG, "loadData: ");
        if (isErr) {
            isErr = false;
            getCustomerList("", "");
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}
