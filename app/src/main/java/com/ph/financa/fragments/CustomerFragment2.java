//package com.ph.financa.fragments;
//
//import android.Manifest;
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import androidx.appcompat.widget.AppCompatEditText;
//
//import com.aries.ui.view.title.TitleBarView;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.ph.financa.R;
//import com.ph.financa.activity.WebActivity;
//import com.ph.financa.activity.adpater.CustomerListAdapter;
//import com.ph.financa.activity.bean.BaseTResp2;
//import com.ph.financa.activity.bean.ContactColumnBean;
//import com.ph.financa.activity.bean.CustomerBean;
//import com.ph.financa.dialog.ListDialog;
//import com.ph.financa.dialog.MoreDialog;
//import com.ph.financa.dialog.SortNameDialog;
//import com.vise.xsnow.http.ViseHttp;
//import com.vise.xsnow.http.callback.ACallback;
//import com.vise.xsnow.http.request.GetRequest;
//import com.vise.xsnow.permission.OnPermissionCallback;
//import com.vise.xsnow.permission.PermissionManager;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import tech.com.commoncore.base.BaseTitleRefreshLoadFragment;
//import tech.com.commoncore.constant.ApiConstant;
//import tech.com.commoncore.utils.DisplayUtils;
//import tech.com.commoncore.utils.FastUtil;
//import tech.com.commoncore.utils.ToastUtil;
//
///**
// * 客户
// */
//public class CustomerFragment2 extends BaseTitleRefreshLoadFragment<CustomerBean> implements View.OnClickListener {
//
//    private String[] SORTINDEX = {"ASC", "DESC"};
//
//    private SortNameDialog mSortNameDialog;
//    private MoreDialog moreDialog;
//    private ListDialog mListDialog;
//    private RelativeLayout mRlTitle;
//    private LinearLayout mRlSearch;
//    private AppCompatEditText mEtInput;
//    private boolean isErr;
//    private RelativeLayout mRlDel;
//
//    @Override
//    public void initView(Bundle savedInstanceState) {
//        mContentView.findViewById(R.id.iv_search).setOnClickListener(this);
//        mContentView.findViewById(R.id.iv_more).setOnClickListener(this);
//
//        mContentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
//
//        mRlTitle = mContentView.findViewById(R.id.rl_title);
//        mRlSearch = mContentView.findViewById(R.id.rl_search);
//
//        mEtInput = mContentView.findViewById(R.id.et_input);
//        mEtInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent != null && keyEvent.getKeyCode()
//                    == KeyEvent.KEYCODE_SEARCH) {
//                Log.i(TAG, "onEditorAction: " + mEtInput.getText().toString());
//
//                String text = mEtInput.getText().toString();
//                if (!TextUtils.isEmpty(text)) {
//                    getCustomerList(text, "");
//                } else {
//                    ToastUtil.show("请输入搜索");
//                }
//                return true;
//            }
//            return false;
//        });
//
//        mRlDel = mContentView.findViewById(R.id.rl_del);
//        mContentView.findViewById(R.id.tv_del_cancel).setOnClickListener(this);
//        mContentView.findViewById(R.id.tv_del).setOnClickListener(this);
//
//
//
//        /*设置长按*/
//        mBaseRefreshLoadDelegate.mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
//            List<CustomerBean> data = mBaseRefreshLoadDelegate.mAdapter.getData();
//            for (CustomerBean bean : data) {
//                bean.setSelect(true);
//            }
//            showDel(true);
//            mRecyclerView.getAdapter().notifyDataSetChanged();
//            return true;
//        });
//    }
//
//    @Override
//    public void onItemClicked(BaseQuickAdapter<CustomerBean, BaseViewHolder> adapter, View view, int position) {
//        super.onItemClicked(adapter, view, position);
//
//        CustomerBean bean = adapter.getData().get(position);
//        bean.setCheck(!bean.isCheck());
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_search:
//                showSearch(true);
//                break;
//
//            case R.id.iv_more:
//                showMoreDialog();
//                break;
//
//            case R.id.tv_cancel:
//                showSearch(false);
//                break;
//
//            case R.id.tv_del_cancel:
//                List<CustomerBean> data = mBaseRefreshLoadDelegate.mAdapter.getData();
//                for (CustomerBean bean : data) {
//                    bean.setSelect(false);
//                    bean.setCheck(false);
//                }
//                showDel(false);
//                mBaseRefreshLoadDelegate.mAdapter.notifyDataSetChanged();
//                break;
//
//            case R.id.tv_del:
//                data = mBaseRefreshLoadDelegate.mAdapter.getData();
//                if (null != data && data.size() > 0) {
//                    List<String> list = new ArrayList<>();
//                    for (CustomerBean bean : data) {
//                        if (bean.isCheck()) {
//                            list.add(String.valueOf(bean.getId()));
//                        }
//                    }
//                    if (list.size() > 0) {
//                        deleteBatch(list);
//                    } else {
//                        ToastUtil.show("至少选中一个");
//                    }
//                }
//                break;
//        }
//    }
//
//    /*true:显示 false：隐藏*/
//    private void showDel(boolean isDel) {
//        mRlDel.setVisibility(isDel ? View.VISIBLE : View.GONE);
//    }
//
//    private void showSearch(boolean isSearch) {
//        if (isSearch) {
//            mRlTitle.setVisibility(View.GONE);
//            mRlSearch.setVisibility(View.VISIBLE);
//        } else {
//            mRlTitle.setVisibility(View.VISIBLE);
//            mRlSearch.setVisibility(View.GONE);
//            DisplayUtils.hideSoftInput(mContext, mEtInput);
//
//            mEtInput.setText("");
//        }
//    }
//
//    private void showMoreDialog() {
//        moreDialog = MoreDialog.show(getFragmentManager(), "", index -> {
//            moreDialog.dismiss();
//            switch (index) {
//                case 0:/*新建客户*/
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.ADD_CUSTOMER));
//                    goActivity(WebActivity.class, bundle);
//                    break;
//                case 1:/*分组*/
//                    bundle = new Bundle();
//                    bundle.putString("url", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.CUSTOMER_GROUP));
//                    goActivity(WebActivity.class, bundle);
//                    break;
//                case 2:
//                    showSortDialog();
//                    break;
//                case 3:
//                    showListDialog();
//                    break;
//            }
//        });
//    }
//
//
//    private void goActivity(Class clazz, Bundle bundle) {
//        if (null != bundle) {
//            FastUtil.startActivity(mContext, clazz, bundle);
//        } else {
//            FastUtil.startActivity(mContext, clazz);
//        }
//    }
//
//    private void showListDialog() {
//        List<String> list = Arrays.asList("从谁看了我导入", "从通讯录导入", "取消");
//        mListDialog = ListDialog.show(getFragmentManager(), "", list, (adapter, view, position) -> {
//            mListDialog.dismiss();
//            switch (position) {
//                case 0:
////                    getCustomerList();
//                    break;
//                case 1:
//                    PermissionManager.instance().request(getActivity(), new OnPermissionCallback() {
//                        @Override
//                        public void onRequestAllow(String permissionName) {
//                            Log.i(TAG, "onRequestAllow: " + permissionName);
//                            List<ContactColumnBean> beans = getPhoneContacts();
//                            insertContact(beans);
//                        }
//
//                        @Override
//                        public void onRequestRefuse(String permissionName) {
//                            Log.i(TAG, "onRequestRefuse: " + permissionName);
//                        }
//
//                        @Override
//                        public void onRequestNoAsk(String permissionName) {
//                            Log.i(TAG, "onRequestNoAsk: " + permissionName);
//                        }
//                    }, Manifest.permission.READ_CONTACTS);
//                    break;
//                case 2:
//                    break;
//            }
//        });
//    }
//
//    /*从通讯录导入*/
//    private void insertContact(List<ContactColumnBean> beans) {
//        if (null != beans && beans.size() > 0) {
//            JSONArray jsonArray = new JSONArray();
//            for (ContactColumnBean bean : beans) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("customerName", bean.getName());
//                map.put("telephone", Arrays.asList(bean.getNumber()));
//                jsonArray.put(new JSONObject(map));
//            }
//            ViseHttp.POST(ApiConstant.INSERT_CONTACT)
//                    .setJson(jsonArray)
//                    .request(new ACallback<BaseTResp2>() {
//                        @Override
//                        public void onSuccess(BaseTResp2 data) {
//                            if (data.isSuccess()) {
//                                ToastUtil.show("上传成功！");
//                                loadData();
//                            } else {
//                                ToastUtil.show(data.getMsg());
//                            }
//                        }
//
//                        @Override
//                        public void onFail(int errCode, String errMsg) {
//                            ToastUtil.show(errMsg);
//                        }
//                    });
//        } else {
//            ToastUtil.show("通讯录为空");
//        }
//    }
//
//    /*批量或单个删除客户*/
//    private void deleteBatch(List<String> ids) {
//        if (null != ids && ids.size() > 0) {
//            StringBuffer sb = new StringBuffer();
//            for (String id : ids) {
//                sb.append(id).append(",");
//            }
//            ViseHttp.POST(ApiConstant.DELETE_BATCH)
//                    .addForm("ids", sb.toString())
//                    .request(new ACallback<BaseTResp2>() {
//                        @Override
//                        public void onSuccess(BaseTResp2 data) {
//                            if (data.isSuccess()) {
//                                ToastUtil.show("删除成功");
//                                mBaseRefreshLoadDelegate.mAdapter.getData().clear();
//                                mBaseRefreshLoadDelegate.mAdapter.notifyDataSetChanged();
//                                loadData();
//                            } else {
//                                ToastUtil.show(data.getMsg());
//                            }
//                        }
//
//                        @Override
//                        public void onFail(int errCode, String errMsg) {
//                            ToastUtil.show(errMsg);
//                        }
//                    });
//        }
//    }
//
//    @Override
//    public void loadData() {
//        super.loadData();
//        getCustomerList("", "");
//    }
//
//    /*从通讯录导入*/
//    private List<ContactColumnBean> getPhoneContacts() {
//        ContentResolver resolver = getActivity().getContentResolver();
//        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        if (cursor != null) {
//            List<ContactColumnBean> beanList = new ArrayList<>();
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); // id
//                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // 姓名
//                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 电话
//                beanList.add(new ContactColumnBean(id, name, number));
//                Log.i(TAG, "getPhoneContacts: " + id + " " + name + " " + number);
//            }
//            cursor.close();
//            return beanList;
//        }
//        return null;
//    }
//
//    /*从谁看了我导入*/
//    private void getCustomerList(String customerName, String sort) {
//        mStatusManager.showSuccessLayout();
//        GetRequest request = ViseHttp.GET(ApiConstant.CUSTOMER_LIST);
//        if (!TextUtils.isEmpty(customerName)) {
//            request.addParam("customerName", customerName);
//        }
//        if (!TextUtils.isEmpty(sort)) {
//            request.addParam("sort", sort);
//        }
//        request.request(new ACallback<BaseTResp2<List<CustomerBean>>>() {
//            @Override
//            public void onSuccess(BaseTResp2<List<CustomerBean>> data) {
//                if (data.isSuccess()) {
//                    setData(data.getData());
//                } else {
//                    mStatusManager.showErrorLayout();
//                    isErr = true;
//                    ToastUtil.show(data.getMsg());
//                }
//            }
//
//            @Override
//            public void onFail(int errCode, String errMsg) {
//                mStatusManager.showErrorLayout();
//                ToastUtil.show(errMsg);
//                isErr = true;
//            }
//        });
//    }
//
//
//    private void showSortDialog() {
//        mSortNameDialog = SortNameDialog.show(getFragmentManager(), "", index -> {
//            mSortNameDialog.dismiss();
//            switch (index) {
//                case 0:
//                    getCustomerList("", SORTINDEX[0]);
//                    break;
//                case 1:
//                    getCustomerList("", SORTINDEX[1]);
//                    break;
//                case 2:
//                    getCustomerList("", SORTINDEX[0]);
//                    break;
//            }
//        });
//    }
//
//
//    @Override
//    public int getContentLayout() {
//        return R.layout.fragment_customer2;
//    }
//
//    @Override
//    public BaseQuickAdapter<CustomerBean, BaseViewHolder> getAdapter() {
//        return new CustomerListAdapter(R.layout.item_customer_list);
//    }
//
//    @Override
//    public void loadData(int page) {
//        Log.i(TAG, "loadData: ");
//        if (isErr) {
//            isErr = false;
//            getCustomerList("", "");
//        }
//    }
//
//    @Override
//    public boolean isLoadMoreEnable() {
//        return false;
//    }
//
//    @Override
//    public void setTitleBar(TitleBarView titleBar) {
//    }
//}
