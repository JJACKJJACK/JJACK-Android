package com.mkm.hanium.jjack.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by MIN on 2017-07-10.
 */

public abstract class BindActivity<BindingType extends ViewDataBinding> extends BaseActivity {

    protected BindingType binding;
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }
}
