package com.ph.financa.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.nanchen.wavesidebar.WaveSideBarView;
import com.ph.financa.R;
import com.ph.financa.activity.adpater.ContactsAdapter;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.ContactModel;
import com.ph.financa.constant.Constant;
import com.ph.financa.dialog.SearchDialog;
import com.ph.financa.view.PinnedHeaderDecoration;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 选择导入通讯录
 */
public class SelectAddressActivity extends BaseTitleActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.main_side_bar)
    WaveSideBarView mWaveSideBarView;
    @BindView(R.id.main_search)
    AppCompatTextView mSearch;

    private List<ContactModel> mContactModels;

    private ContactsAdapter mAdapter;

    private AppCompatTextView mSearchEditText;

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        mContactModels = new ArrayList<>(getContacts());

        mAdapter = new ContactsAdapter(R.layout.layaout_item_contacts, mContactModels);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, (parent, adapterPosition) -> true);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.setAdapter(mAdapter);

        // 侧边设置相关
        mWaveSideBarView.setOnSelectIndexItemListener(letter -> {
            for (int i = 0; i < mContactModels.size(); i++) {
                if (mContactModels.get(i).getIndex().equals(letter)) {
                    ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });

        // 搜索按钮相关
        mSearchEditText = findViewById(R.id.main_search);

        mSearchEditText.setOnClickListener(view -> SearchDialog.show(getSupportFragmentManager(), "", getContacts(), new SearchDialog.SearchDialogImpl() {
            @Override
            public void onContactModel(ContactModel model) {
                insertContact(Arrays.asList(model));
            }
        }));

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ContactModel item = (ContactModel) adapter.getItem(position);
            item.setCheck(!item.isCheck());
            adapter.notifyItemChanged(position);
        });
    }

    /*从通讯录导入*/
    private void insertContact(List<ContactModel> beans) {
        if (null != beans && beans.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ContactModel bean : beans) {
                if (bean.isCheck()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerName", bean.getName());
                    map.put("telephone", Arrays.asList(bean.getPhone()));
                    jsonArray.put(new JSONObject(map));
                }
            }
            String str = jsonArray.toString();
            if (!TextUtils.isEmpty(str) && !str.equals("[]")) {
                ViseHttp.POST(ApiConstant.INSERT_CONTACT)
                        .setJson(jsonArray)
                        .request(new ACallback<BaseTResp2>() {
                            @Override
                            public void onSuccess(BaseTResp2 data) {
                                if (data.isSuccess()) {
                                    ToastUtil.show("上传成功！");

                                    SPHelper.setBooleanSF(mContext, Constant.ISREFRESH, true);
                                    finish();
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
                ToastUtil.show("请选择通讯录");
            }
        } else {
            ToastUtil.show("通讯录为空");
        }
    }

    private List<ContactModel> getContacts() {
        return ContactModel.getContacts(getPhoneContacts());
    }

    /*从通讯录导入*/
    private List<ContactModel> getPhoneContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            List<ContactModel> beanList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); // id
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // 姓名
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 电话
                beanList.add(new ContactModel(id, name, number));
            }
            cursor.close();
            return beanList;
        }
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_select_address;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("通讯录").setRightText("确定").setOnRightTextClickListener(view -> {
            Log.i(TAG, "setTitleBar: ");
            insertContact(mAdapter.getData());
        });
    }
}