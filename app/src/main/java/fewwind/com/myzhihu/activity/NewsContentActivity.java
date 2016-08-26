package fewwind.com.myzhihu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.NetUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.Utils.imageloader.ImageLoaderFactory;
import fewwind.com.myzhihu.bean.NewSendBean;
import fewwind.com.myzhihu.bean.StoriesEntityBean;
import fewwind.com.myzhihu.bean.ZhiContentBean;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.net.ZhiHuApi;
import fewwind.com.myzhihu.ui.view.BottomSheetDialogView;
import rx.Subscriber;

public class NewsContentActivity extends BaseActivity {
    public static String NEW_INFO = "newInfoKey";
    public static String STORY_BEAN = "storiesEntityBean";


    public static final String VIEW_NAME_HEADER_IMAGE = "shareTransition";

    private NewSendBean newInfo;

    private StoriesEntityBean storiesEntityBean;
    private  List<StoriesEntityBean> mFavlist;

    boolean isFav;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.iv_news)
    ImageView mHeader;
    @Bind(R.id.id_detail_tv_title)
    TextView mTitle;
    @Bind(R.id.id_detail_load_news)
    ContentLoadingProgressBar mLoading;

    @Bind(R.id.id_detail_wv_news)
    WebView mWebView;

    @Bind(R.id.id_fab_discuss)
    FloatingActionButton mFabComment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_content;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        //覆盖掉变色状态栏的方法
        initStatusBar();
        Bundle bundle = getIntent().getExtras();
        newInfo = (NewSendBean) bundle.getSerializable(NEW_INFO);
        storiesEntityBean = (StoriesEntityBean) bundle.getSerializable(STORY_BEAN);


        isFav = (Boolean) SPUtils.get(getApplicationContext(), "" + newInfo.getId(), false);
        init();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab == null) return;
        fab.setImageResource(isFav ? R.drawable.ic_star_full : R.drawable.ic_star_null);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isFav = (Boolean) SPUtils.get(getApplicationContext(), "" + newInfo.getId(), false);
                //如果传过来的信息是空，就不能收藏，比如轮播图就是空
                if (storiesEntityBean == null) {
                    Snackbar.make(view, "轮播图不能收藏，别问为什么", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (isFav) {
                    SPUtils.remove(getApplicationContext(), newInfo.getId() + "");
                    Snackbar.make(view, "取消成功", Snackbar.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_star_null);

                    handleFav(storiesEntityBean, false);
                } else {
                    SPUtils.put(getApplicationContext(), newInfo.getId() + "", true);
                    Snackbar.make(view, "收藏成功", Snackbar.LENGTH_SHORT).show();
                    fab.setImageResource(R.drawable.ic_star_full);
                    handleFav(storiesEntityBean, true);
                }
            }
        });


        mFabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogView view = new BottomSheetDialogView(NewsContentActivity.this, newInfo.getId());
            }
        });
    }

    protected void initStatusBar() {
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

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ViewCompat.setTransitionName(mHeader, VIEW_NAME_HEADER_IMAGE);
        //            如果是无图模式，仅仅在无线的条件加载图片
        if ((Boolean) SPUtils.get(this, ZhiHuApi.SET_IMAGE, true)) {
            if (NetUtil.getNetWorkType(this)==NetUtil.NETWORKTYPE_WIFI) {
                ImageLoaderFactory.getLoader().displayImage(mHeader, newInfo.getImage(), null);
            }
        } else {
            ImageLoaderFactory.getLoader().displayImage(mHeader, newInfo.getImage(), null);
        }


        toolbarLayout.setTitle("");
        toolbarLayout.setTitleEnabled(true);
        mLoading.setVisibility(View.VISIBLE);

        initNewsContent();


    }

    private void initNewsContent() {
        ZhiContentBean zhiContentBean = App.cacheHelper.getAsSerializable(newInfo.getId() + "");
        if (zhiContentBean != null) {
            handleContent(zhiContentBean);

            return;
        }

        HttpMethods.getInstance().getZhiContent(new Subscriber<ZhiContentBean>() {
            @Override
            public void onCompleted() {
                mLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                mLoading.setVisibility(View.GONE);
                Snackbar.make(toolbarLayout, "哎呀，我也不知道发生了什么", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ZhiContentBean zhiContentBean) {
                App.cacheHelper.put(newInfo.getId() + "", zhiContentBean);
                handleContent(zhiContentBean);
            }
        }, newInfo.getId());
    }

    /**
     * 处理收藏的方法
     *
     * @param data 收藏的实体
     * @param flag false 取消
     */
    public void handleFav(StoriesEntityBean data, boolean flag) {
        int pos = -1;
        mFavlist = App.cacheHelper.getAsSerializable(ZhiHuApi.KEY_FAV_NEWS);
        if (mFavlist == null) mFavlist = new ArrayList<>();
        if (data == null) return;
        if (flag) {
            if (mFavlist.contains(data)) return;
            mFavlist.add(data);
        } else {
            for (StoriesEntityBean bean :
                    mFavlist) {
                if (bean.getId() == data.getId()) {
                    pos = mFavlist.indexOf(bean);

                }
            }
            if (pos >= 0)
                mFavlist.remove(pos);
        }
        App.cacheHelper.put(ZhiHuApi.KEY_FAV_NEWS, (Serializable) mFavlist);
    }

    private void handleContent(ZhiContentBean zhiContentBean) {
        mTitle.setText(zhiContentBean.getTitle());
        toolbarLayout.setTitle(zhiContentBean.getTitle());


        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(zhiContentBean.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(zhiContentBean.getImage_source()).append("</span>")
                .append("<img src=\"").append(zhiContentBean)
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + zhiContentBean.getBody().replace("<div class=\"img-place-holder\">", "");
//                        + zhiContentBean.getBody().replace("<div class=\"img-place-holder\">", sb.toString());

        String mNewsContentNoImg = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style_no_img.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + zhiContentBean.getBody().replace("<div class=\"img-place-holder\">", "");

        mWebView.loadDataWithBaseURL("file:///android_asset/", mNewsContent, "text/html", "UTF-8", null);
//        mWebView.loadDataWithBaseURL("file:///android_asset/", mNewsContentNoImg, "text/html", "UTF-8", null);
        mWebView.setDrawingCacheEnabled(true);
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自专注日报的分享" + newInfo.getTitle() + "，http://daily.zhihu.com/story/" + newInfo.getId());
        startActivity(Intent.createChooser(intent, newInfo.getTitle()));
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }



}
