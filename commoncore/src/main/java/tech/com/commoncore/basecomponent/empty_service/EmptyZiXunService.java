package tech.com.commoncore.basecomponent.empty_service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import tech.com.commoncore.basecomponent.service.IZiXunService;


/**
 * 默认的登陆模块接口
 */
public class EmptyZiXunService implements IZiXunService {

    @Override
    public Fragment newArticalFragment(Bundle bundle) {
        return DefaultFragment.newInstance();
    }

    @Override
    public Fragment newFlashFragment(Bundle bundle) {
        return DefaultFragment.newInstance();
    }

    @Override
    public Fragment newCalendarFragment(Bundle bundle) {
        return DefaultFragment.newInstance();
    }
}
