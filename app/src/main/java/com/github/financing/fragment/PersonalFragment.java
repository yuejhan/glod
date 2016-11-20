package com.github.financing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.financing.R;
import com.github.financing.base.BaseFragment;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9
 * 描述：
 *******************************************/
public class PersonalFragment extends BaseFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_personal, null);
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
        return view;

    }
}
