package com.wm.toec.microenv.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wm.toec.microenv.R;
import com.wm.toec.microenv.databinding.ActivityBaseBinding;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2018/7/3.
 * 带toolbar的activity的base activity
 */

public class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {
    //绑定的ViewDataBinding
    protected V mBindView;
    //基础的ViewDataBinding
    protected ActivityBaseBinding mBaseView;
    //复合的订阅 方便统一取消订阅
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void setContentView(@LayoutRes int layoutRedId) {
        mBaseView = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base,null,false);
        mBindView = DataBindingUtil.inflate(getLayoutInflater(),layoutRedId,null,false);
        //标题栏以外的内容
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mBindView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout)mBaseView.getRoot().findViewById(R.id.container);
        mContainer.addView(mBindView.getRoot());
        getWindow().setContentView(mBaseView.getRoot());

        // 设置透明状态栏，兼容4.4
        //StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme),0);
        setTitlebar();
    }
    /**
     * 设置标题栏
     */
    public void setTitlebar(){
        setSupportActionBar(mBaseView.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        }
        mBaseView.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    /**
     * 设置toolbar主标题
     */
    @Override
    public void setTitle(CharSequence title) {
        mBaseView.toolbar.setTitle(title);
    }
    /**
     * 添加disposable
     */
    public void addDisposable(Disposable disposable){
        if (this.mCompositeDisposable==null){
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    /**
     * 销毁时取消全部订阅
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeDisposable != null && !this.mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }
}

