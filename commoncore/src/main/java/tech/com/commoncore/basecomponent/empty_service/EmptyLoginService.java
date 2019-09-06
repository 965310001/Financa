package tech.com.commoncore.basecomponent.empty_service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import tech.com.commoncore.basecomponent.service.ILoginService;


/**
 * 默认的登陆模块接口
 */
public class EmptyLoginService implements ILoginService {

    @Override
    public Fragment newUserFragment(Bundle bundle) {
        return DefaultFragment.newInstance();
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public boolean hasLogin() {
        return false;
    }
}
