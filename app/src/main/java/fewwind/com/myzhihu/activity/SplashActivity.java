package fewwind.com.myzhihu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;

import butterknife.Bind;
import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.MainActivity;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.net.ZhiHuApi;
import fewwind.com.myzhihu.ui.view.catloading.CatLoadingView;
import rx.Subscriber;

public class SplashActivity extends BaseActivity {

    public static final String KEY_TOP_STORY = "keyTopStory";
    @Bind(R.id.id_splash)
    RelativeLayout splash;

    @Bind(R.id.id_splash_loading)
    CatLoadingView mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {


        mLoading.startAnim();
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopStory();
            }
        }, 500);
    }

    public void getTopStory() {
        HttpMethods.getInstance().getStoryMain(new Subscriber<StoryMain>() {
            @Override
            public void onCompleted() {
                mLoading.setVisibility(View.GONE);
                mLoading.stopAnim();
            }

            @Override
            public void onError(Throwable e) {
                Logger.d("onError" + e.toString());
                mLoading.setVisibility(View.GONE);
                mLoading.stopAnim();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                StoryMain cache =  App.cacheHelper.getAsSerializable(ZhiHuApi.KEY_LATEST_NEWS);
                if (cache==null){
                    Snackbar.make(splash, "加载失败········", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra(KEY_TOP_STORY,cache );
                startActivity(intent);
                finish();
            }

            @Override
            public void onNext(StoryMain storyMain) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra(KEY_TOP_STORY, (Serializable) storyMain);
                App.cacheHelper.put(ZhiHuApi.KEY_LATEST_NEWS, storyMain);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
