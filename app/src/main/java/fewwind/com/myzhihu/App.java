package fewwind.com.myzhihu;

import android.app.Activity;
import android.app.Application;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fewwind.com.myzhihu.Utils.AppContextUtil;
import fewwind.com.myzhihu.Utils.disk.DiskLruCacheHelper;
import fewwind.com.myzhihu.Utils.imageloader.UniversalAndroidImageLoader;


/**
 * Created by fewwind on 2016/4/7.
 */
public class App extends Application {


    public List<Activity> mActivityList = new ArrayList<>();
    public static DiskLruCacheHelper cacheHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("zhihu");
        UniversalAndroidImageLoader.init(this);
        AppContextUtil.init(this);

        try {
            cacheHelper = new DiskLruCacheHelper(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void exitApp() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }
}
