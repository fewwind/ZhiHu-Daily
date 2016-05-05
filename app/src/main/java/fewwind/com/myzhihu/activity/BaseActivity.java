package fewwind.com.myzhihu.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.StatusBarCompat;
import fewwind.com.myzhihu.Utils.swipeback.SwipeBackActivity;
import fewwind.com.myzhihu.Utils.swipeback.SwipeBackLayout;


/**
 * Created by laucherish on 16/3/15.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    protected SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate(savedInstanceState);
        ((App) getApplication()).addActivity(this);

        initStatusBar();


    }

    protected void initStatusBar() {
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorPrimary));
    }

    public Toolbar initToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;

    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);


}
