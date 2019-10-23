package com.ph.financa.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.githang.statusbar.StatusBarCompat;
import com.hyphenate.easeui.utils.DateUtil;
import com.hyphenate.easeui.utils.GlideManager;
import com.hyphenate.easeui.utils.SPHelper;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.DataBaseBean;
import com.ph.financa.constant.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.permission.Permission;
import tech.com.commoncore.permission.UsesPermission;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.FileUtils;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 资料库
 */
public class DatabaseActivity extends BaseTitleActivity {

    @BindView(R.id.sl_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.ll_add_data)
    LinearLayout mLlAddData;

    private DataBaseAdapter mAdapter;

    private int REQUEST_CODE = 1234;

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));//设置Header
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));//设置Footer

        mSmartRefreshLayout.setOnRefreshListener(onRefresh);
        mSmartRefreshLayout.setOnLoadMoreListener(onLoadMore);

        mRvContent.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new DataBaseAdapter(R.layout.item_data_base);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.i(TAG, "onItemClick: ");
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (DataBaseBean) adapter.getData().get(position));
//            FastUtil.startActivity(mContext, PdfActivity.class, bundle);
        });
        mRvContent.setAdapter(mAdapter);
        mSmartRefreshLayout.autoRefresh();
//        ArrayList<DataBaseBean> data = getData();
//        if (null != data) {
//            mAdapter.addData(data);
//        }
        saveData(getIntent());
    }

    private void saveData(Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "pdf 的数据接收" + action + " " + intent.getType() + " " + intent.getData());
        if (null != intent && null != action) {
            DataBaseBean bean = new DataBaseBean(action, "", DateUtil.formatDate(new Date(), DateUtil.FORMAT_101));
            ArrayList<DataBaseBean> list = SPHelper.getDeviceData(mContext, Constant.DATABASE);

            uploadFile(intent.getData().toString());
            if (null != bean) {
                boolean isAdd = false;
                if (null != list) {
                    if (list.size() > 0) {
                        for (DataBaseBean baseBean : list) {
                            if (baseBean.getPath().equals(bean.getPath()) && baseBean.getTitle().equals(bean.getTitle())) {
                                isAdd = true;
                                break;
                            }
                        }
                    } else if (list.size() == 0) {
//                        for (int i = 0; i < 5; i++) {
//                            list.add(new DataBaseBean("", "产品使用说明.pdf", "2019-07-25 14:58:54"));
//                        }
                    }
                } else if (list == null) {
                    list = new ArrayList<>();
                }
                if (!isAdd) {
                    list.add(bean);
                }
                SPHelper.saveDeviceData(mContext, Constant.DATABASE, list);
            }
        }
        mSmartRefreshLayout.autoRefresh();
    }

    private void uploadFile(String action) {
        /*上传文件*/
        File file = FileUtils.getFileByUri(mContext, Uri.parse(action));
        Log.i(TAG, "uploadFile: " + action);
        if (null != file) {
            showLoading();
            Map<String, File> map = new HashMap<>();
            map.put("file", file);
            Log.i(TAG, "initView: " + file.length());
            ViseHttp.UPLOAD(ApiConstant.FILE_UPLOAD).addFiles(map).request(new ACallback<BaseTResp2<List<String>>>() {
                @Override
                public void onSuccess(BaseTResp2<List<String>> data) {
                    Log.i(TAG, "onSuccess: " + data.getMsg() + data);
                    if (data.isSuccess()) {
                        Log.i(TAG, "onSuccess: 上传成功");
                        // TODO: 2019/10/22 更新数据
                        mSmartRefreshLayout.autoRefresh();
//                        loadData();
                    } else {
                        hideLoading();
                        ToastUtil.show(data.getDetailMsg());
                    }
                }

                @Override
                public void onFail(int errCode, String errMsg) {
                    hideLoading();
                    Log.i(TAG, "onFail: " + errMsg + " " + errCode);
                    ToastUtil.show(errMsg);
                }
            });
        } else {
            ToastUtil.show("文件不存在或者为空");
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        mAdapter.getData().clear();

        showLoading();
        ViseHttp.GET(ApiConstant.DOC_LIST)
                .request(new ACallback<BaseTResp2<List<DataBaseBean>>>() {

                    @Override
                    public void onSuccess(BaseTResp2<List<DataBaseBean>> data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            Log.i(TAG, "onSuccess: " + data.getData());
                            if (null != data.getData()) {
                                mAdapter.addData(data.getData());
                            }
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show(errMsg);
                    }
                });

//        ArrayList<DataBaseBean> data = getData();

    }

//    private ArrayList<DataBaseBean> getData() {
//        return SPHelper.getDeviceData(mContext, Constant.DATABASE);
//    }


    public class DataBaseAdapter extends BaseQuickAdapter<DataBaseBean, BaseViewHolder> {
        public DataBaseAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, DataBaseBean item) {
            ImageView ivImg = helper.getView(R.id.iv_head);
            GlideManager.loadImg(R.mipmap.ic_data_base, ivImg);
            helper.setText(R.id.tv_title, item.getTitle()).setText(R.id.tv_time, item.getTime());
        }
    }

    private OnRefreshListener onRefresh = r -> {
        loadData();
        r.finishRefresh();
        r.setEnableLoadMore(false);
    };

    private OnLoadMoreListener onLoadMore = r -> {
        loadData();
        r.finishLoadMore();
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_data:
                requestPermission();
                break;
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        new UsesPermission(this, Permission.READ_EXTERNAL_STORAGE) {
            @Override
            protected void onTrue(@NonNull ArrayList<String> lowerPermissions) {
                //获取了全部权限执后行此函数，
                Log.i(TAG, "onTrue: ");
                takePhoto();
            }

            @Override
            protected void onFalse(@NonNull ArrayList<String> rejectFinalPermissions, @NonNull ArrayList<String> rejectPermissions, @NonNull ArrayList<String> invalidPermissions) {
                //未全部授权时执行此函数
                Log.i(TAG, "onFalse: ");
//                cancelFilePathCallback();
            }

            //要么实现上面两个方法即可，onTrue或onFalse只会有一个会被调用一次
            //要么仅仅实现下面这个方法，不管授权了几个权限都会调用一次

            @Override
            protected void onComplete(@NonNull ArrayList<String> resolvePermissions, @NonNull ArrayList<String> lowerPermissions, @NonNull ArrayList<String> rejectFinalPermissions, @NonNull ArrayList<String> rejectPermissions, @NonNull ArrayList<String> invalidPermissions) {
                //完成回调，可能全部已授权、全部未授权、或者部分已授权
                //通过resolvePermissions.contains(Permission.XXX)来判断权限是否已授权
                Log.i(TAG, "onComplete: ");
//                cancelFilePathCallback();
            }
        };

//        PermissionManager.instance().request(this, new OnPermissionCallback() {
//            @Override
//            public void onRequestAllow(String permissionName) {
//                takePhoto();
//            }
//
//            @Override
//            public void onRequestRefuse(String permissionName) {
//                Log.i(TAG, "onRequestRefuse: " + permissionName);
//                cancelFilePathCallback();
//            }
//
//            @Override
//            public void onRequestNoAsk(String permissionName) {
//                Log.i(TAG, "onRequestNoAsk: " + permissionName);
//                cancelFilePathCallback();
//            }
//        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
//        String filePath = Environment.getExternalStorageDirectory() + File.separator
//                + Environment.DIRECTORY_PICTURES + File.separator;
//        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//        Uri imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);
        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//        Intent Photo = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
//
//        startActivityForResult(chooserIntent, REQUEST_CODE);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (requestCode == REQUEST_CODE) {
            saveData(data);
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
//            if (mUploadCallbackBelow != null) {
//                chooseBelow(resultCode, data);
//            } else if (mUploadCallbackAboveL != null) {
//                chooseAbove(resultCode, data);
//            } else {
////                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
//                ToastUtil.show("发生错误");
//            }

            Log.i(TAG, "onActivityResult: " + data.getType());
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("资料库").setRightText("添加")
                .setOnRightTextClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.URL, String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
                            ApiConstant.HOW_TO_UPLOAD,
                            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
                            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, "")));
                    FastUtil.startActivity(mContext, WebActivity.class, bundle, true);
                });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_database;
    }
}
