package tech.com.commoncore.basecomponent.empty_service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import tech.com.commoncore.basecomponent.service.IFragmentService;


/**
 * 默认的登陆模块接口
 */
public class EmptyFragmentService implements IFragmentService {
    @Override
    public Fragment newEntryFragment(Bundle bundle) {
        return DefaultFragment.newInstance();
    }
}
