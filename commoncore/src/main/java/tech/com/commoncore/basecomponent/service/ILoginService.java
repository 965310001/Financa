package tech.com.commoncore.basecomponent.service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * Anthor:NiceWind
 * Time:2019/3/22
 * Desc:The ladder is real, only the climb is all.
 */
public interface ILoginService {
    String getUserId();

    boolean hasLogin();

    Fragment newUserFragment(Bundle bundle);
}
