package com.ph.financa.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.ph.financa.R;
import com.ph.financa.activity.bean.ContactModel;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.utils.DisplayUtils;

/**
 * 搜索
 */
public class SearchDialog extends DialogFragment {

    private List<ContactModel> mData;

    private List<ContactModel> mShowModels;/*搜索显示是数据*/

    private ListAdapter mAdapter;

    private SearchDialogImpl mClick;

    public static SearchDialog show(FragmentManager manager, String tag, List<ContactModel> data, SearchDialogImpl click) {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(manager, tag);
        searchDialog.setClick(click);
        searchDialog.setData(data);
        return searchDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TranslucentNoTitle);
    }

    public interface SearchDialogImpl {
        void onContactModel(ContactModel model);
    }

    public void setClick(SearchDialogImpl mClick) {
        this.mClick = mClick;
    }

    public void setData(List<ContactModel> data) {
        this.mData = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_list, container);
        AppCompatEditText mEtSearch = view.findViewById(R.id.et_search);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mShowModels.clear();
                for (ContactModel model : mData) {
                    String str = Trans2PinYinUtil.trans2PinYin(model.getName());
                    if (str.contains(s.toString()) || model.getName().contains(s.toString())) {
                        mShowModels.add(model);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        TextView mTvCancel = view.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(v -> dismiss());

        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ListAdapter(R.layout.item_select_text);
        mAdapter.setNewData(mShowModels = new ArrayList<>());

        mAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            if (null != mClick) {
                Log.i("TAG", "onCreateView: " + position);
                ContactModel item = (ContactModel) adapter.getItem(position);
                item.setCheck(true);
                mClick.onContactModel(item);

                DisplayUtils.hideSoftInput(getContext(), getView());
            }
        });

        recyclerView.setAdapter(mAdapter);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DisplayUtils.hideSoftInput(getContext(), getView());
//            }
//        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DisplayUtils.hideSoftInput(getContext(), getView());
                return true;
            }
        });
        return view;
    }

    /*设置透明*/
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.TOP;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }


    class ListAdapter extends BaseQuickAdapter<ContactModel, BaseViewHolder> {
        public ListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, ContactModel item) {
            helper.setText(R.id.tv_name, item.getName()).addOnClickListener(R.id.btn_input);
        }
    }


}
