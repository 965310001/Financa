package tech.com.commoncore.base;

import com.aries.ui.view.title.TitleBarView;

import tech.com.commoncore.R;
import tech.com.commoncore.delegate.BaseTitleDelegate;
import tech.com.commoncore.interf.IBaseTitleView;

/**
 * @Author: AriesHoo on 2018/7/23 10:35
 * @E-Mail: AriesHoo@126.com
 * Function: 包含TitleBarView的Activity
 * Description:
 */
public abstract class BaseTitleActivity extends BaseActivity implements IBaseTitleView {

    protected BaseTitleDelegate mBaseTitleDelegate;
    protected TitleBarView mTitleBar;

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        titleBar.setRightText("")
                .setLeftTextDrawable(R.mipmap.back_black)
                .setLeftText("返回")
                .setTitleMainTextMarquee(true)
                .setTitleMainText("")
                .setDividerHeight(0);
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        mBaseTitleDelegate = new BaseTitleDelegate(mContentView, this, this.getClass());
        mTitleBar = mBaseTitleDelegate.mTitleBar;
    }
}
