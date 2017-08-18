package com.mkm.hanium.jjack.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by MIN on 2017-07-10.
 * xml과 바인딩하는 액티비티. 상속해서 사용할 클래스이며, 하위 클래스에선 binding.(viewID) 으로 불러온다.
 */

public abstract class BindActivity<BindingType extends ViewDataBinding> extends BaseActivity {

    protected BindingType binding;

    /**
     * layout ID로 바인딩
     * @return R.layout.(layout ID)
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }
}
