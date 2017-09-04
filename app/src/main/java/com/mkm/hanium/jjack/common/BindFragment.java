package com.mkm.hanium.jjack.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MIN on 2017-07-15.
 * xml 요소들과 자동으로 binding하는 프래그먼트 클래스. 상속받아서 사용.
 * 로그 작업에도 관여함.
 * binding.(viewID) 형식으로 불러올 수 있음
 */

public abstract class BindFragment<BindingType extends ViewDataBinding> extends BaseFragment {

    protected BindingType binding;

    /**
     * 반드시 정의. binding에 필요함.
     * @return R.layout.layout_name
     */
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        return binding.getRoot();
    }
}