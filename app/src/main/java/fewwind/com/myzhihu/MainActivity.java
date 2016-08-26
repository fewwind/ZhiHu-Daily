package fewwind.com.myzhihu;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fewwind.com.myzhihu.Utils.AppContextUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.activity.BaseActivity;
import fewwind.com.myzhihu.activity.SettingActivity;
import fewwind.com.myzhihu.activity.SplashActivity;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.bean.ThemesBean;
import fewwind.com.myzhihu.bean.VersionBean;
import fewwind.com.myzhihu.fragment.MainFragment;
import fewwind.com.myzhihu.fragment.fragmentutil.FragmentAdapter;
import fewwind.com.myzhihu.fragment.fragmentutil.FragmentNavigator;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.net.ZhiHuApi;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;


public class MainActivity extends BaseActivity implements BottomNavigation.OnMenuItemSelectionListener {
    private static final int DEFAULT_POSITION = 0;
    private FragmentNavigator mNavigator;
    public StoryMain mStoryMain;
    public ThemesBean mThemesBean;
    private Toolbar toolbar;
    private BottomNavigation mBottomNavigation;

    private int mCurrentPos = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        initView(savedInstanceState);

        initData();

    }

    private void initView(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mStoryMain = (StoryMain) getIntent().getSerializableExtra(SplashActivity.KEY_TOP_STORY);
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.id_container);
        mNavigator.setDefaultPosition(DEFAULT_POSITION);
        mNavigator.onCreate(savedInstanceState);
        mNavigator.showFragment(DEFAULT_POSITION);

        mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        mBottomNavigation.setOnMenuItemClickListener(this);

        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
               updateContent();
            }
        }, 800);

    }


    /**
     * 初始化 知乎专栏 的标题信息
     */
    private void initData() {

        versionCheck();

        mThemesBean = App.cacheHelper.getAsSerializable(ZhiHuApi.KEY_COLUMN_TITLE);
        if (mThemesBean != null) {
            return;
        }
        HttpMethods.getInstance().getZhiThemes(new Subscriber<ThemesBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                com.orhanobut.logger.Logger.e(e.toString());
            }

            @Override
            public void onNext(ThemesBean themesBean) {
                Logger.d(themesBean.toString());
                mThemesBean = themesBean;
                App.cacheHelper.put(ZhiHuApi.KEY_COLUMN_TITLE, themesBean);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    public StoryMain getLastInfo() {
        return mStoryMain;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main_share) {
            sharePackageFile();
            return true;
        }

        if (id == R.id.action_main_setting) {
            SettingActivity.startSettingActivity(this);
            return true;
        }

        if (id == R.id.action_main_comment) {
            AppContextUtil.launchAppDetail(getPackageName(), "com.coolapk.market", MainActivity.this);
            return true;
        }


        if (id == R.id.action_main_top) {
            Fragment currentFragment = mNavigator.getCurrentFragment();
            if (currentFragment instanceof MainFragment) {
                ((MainFragment) currentFragment).scrollTop();
            } else {
                Toast.makeText(this, "主页的专属技能", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((App) getApplication()).exitApp();
    }


    private void sharePackageFile() {

        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        List<ResolveInfo> resInfos = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> labelIntent = new ArrayList<>();
        for (int i = 0; i < resInfos.size(); i++) {
            ResolveInfo resInfo = resInfos.get(i);
            String packageName = resInfo.activityInfo.packageName;
            if (packageName.contains("tencent") || packageName.contains("blue")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(applicationInfo.sourceDir)));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                labelIntent.add(new LabeledIntent(intent, packageName, resInfo.loadLabel(pm), resInfo.icon));
            }
        }


        LabeledIntent[] extraIntents = labelIntent.toArray(new LabeledIntent[labelIntent.size()]);

        Intent openInChooser = Intent.createChooser(labelIntent.remove(0), warpChooserTitle(getResources().getString(R.string.app_name)));
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);

    }

    /**
     * warp choose title and make app title accent
     *
     * @param appName app name
     * @return warped chooser title
     */
    private SpannableStringBuilder warpChooserTitle(String appName) {
        @SuppressLint("StringFormatMatches")
        String title = String.format(getString(R.string.select_transfer_way_apk, appName));
        ForegroundColorSpan fontSpanRed = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
        int start = 2;
        int end = start + appName.length() + 3;
        SpannableStringBuilder mSpannableBuilder = new SpannableStringBuilder(title);
        mSpannableBuilder.setSpan(fontSpanRed, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return mSpannableBuilder;
    }

    public ThemesBean getColumnInfo() {
        return mThemesBean;
    }


    private void versionCheck() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/fewwind/InCarMedia/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZhiHuApi version = retrofit.create(ZhiHuApi.class);
        Call<VersionBean> versionInfo = version.getVersionInfo();
        versionInfo.enqueue(new Callback<VersionBean>() {
            @Override
            public void onResponse(Response<VersionBean> response, Retrofit retrofit) {
                Logger.v(response.body().toString());
                VersionBean version = response.body();
                if(AppContextUtil.getAppVersion(MainActivity.this).equals(version.getLatest())) return;

                jumpBrowser(version);

            }

            @Override
            public void onFailure(Throwable t) {
                Logger.w(t.toString());

            }
        });

    }

    public void  jumpBrowser(final VersionBean updateInfo){


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("发现新版本")
                .setMessage(updateInfo.getMsg())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse(updateInfo.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("就不",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    public void updateContent(){

        if ((Boolean) SPUtils.get(this, ZhiHuApi.KEY_IS_FIRST, false)) return;

        new AlertDialog.Builder(this)
                .setTitle("更新内容")
                .setMessage(" 1、更灵活的bottom bar，5.0以上效果更炫\n 2、新增查看评论功能 \n 3、新增根据日期选择查看\n 4、新增滑动返回\n" +
                        " 5、全新UI，优化多处问题")
                .setPositiveButton("给好评", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppContextUtil.launchAppDetail(getPackageName(), "com.coolapk.market", MainActivity.this);
                    }
                })

                .show();

        SPUtils.put(this, ZhiHuApi.KEY_IS_FIRST, true);
    }


    @Override
    public void onMenuItemSelect(@IdRes int i, int index) {
        mCurrentPos = index;
        if (index == 2) {
            Logger.v("收藏页面变得可见==Click");
            mNavigator.showFragment(2, true);

        } else {

            mNavigator.showFragment(index);
        }
    }

    @Override
    public void onMenuItemReselect(@IdRes int i, int index) {

    }
}
