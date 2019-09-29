package com.ph.financa.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.view.title.TitleBarView;
import com.nanchen.wavesidebar.SearchEditText;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;
import com.ph.financa.R;
import com.ph.financa.activity.adpater.ContactsAdapter;
import com.ph.financa.activity.bean.ContactModel;
import com.ph.financa.view.PinnedHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import tech.com.commoncore.base.BaseTitleActivity;

/**
 * 选择导入通讯录
 */
public class SelectAddressActivity extends BaseTitleActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.main_side_bar)
    WaveSideBarView mWaveSideBarView;

    private List<ContactModel> mContactModels;
    private List<ContactModel> mShowModels;

    private ContactsAdapter mAdapter;
    private SearchEditText mSearchEditText;

    @Override
    public void initView(Bundle savedInstanceState) {

        initData();

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
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mShowModels.clear();
                for (ContactModel model : mContactModels) {
                    String str = Trans2PinYinUtil.trans2PinYin(model.getName());
                    if (str.contains(s.toString()) || model.getName().contains(s.toString())) {
                        mShowModels.add(model);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        mContactModels = new ArrayList<>();
        mShowModels = new ArrayList<>();
        mContactModels.addAll(getContacts());
        mShowModels.addAll(mContactModels);
    }

    private List<ContactModel> getContacts() {
        return ContactModel.getContacts();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_select_address;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("导入通讯录");
    }
}
