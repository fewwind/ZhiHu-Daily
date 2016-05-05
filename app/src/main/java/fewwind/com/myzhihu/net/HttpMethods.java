package fewwind.com.myzhihu.net;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import fewwind.com.myzhihu.bean.StoryExtraBean;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.bean.ThemesBean;
import fewwind.com.myzhihu.bean.ZhiCommentsBean;
import fewwind.com.myzhihu.bean.ZhiContentBean;
import fewwind.com.myzhihu.bean.ZhiMsgBean;
import fewwind.com.myzhihu.bean.ZhiThemeBean;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fewwind on 2016/3/17.
 */
public class HttpMethods {

//    retrofit的完美封装，下边是源地址
    //http://gank.io/post/56e80c2c677659311bed9841


    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private ZhiHuApi zhiHuApi;

    private HttpMethods() {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        zhiHuApi = retrofit.create(ZhiHuApi.class);
    }


    private static class SingleTonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    private static HttpMethods instance = null;

    public static HttpMethods getInstanceSync() {
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods();
                }
            }
        }
        return instance;
    }

    public void getStoryMain(Subscriber<StoryMain> subscriber) {

        zhiHuApi.getStoryMain().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getZhiThemes(Subscriber<ThemesBean> subscriber) {

        zhiHuApi.getZhiThemes().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getZhiContent(Subscriber<ZhiContentBean> subscriber, int id) {

        zhiHuApi.getZhiContent(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getZhiMsg(Subscriber<ZhiMsgBean> subscriber, String date) {

        zhiHuApi.getZhiMsg(date).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getZhiThemeMsg(Subscriber<ZhiThemeBean> subscriber, int id) {

        zhiHuApi.getZhiThemeMsg(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


/*    public void getZhiCommentsLong(Subscriber<ZhiCommentsBean> subscriber, int id) {

        zhiHuApi.getZhiCommentsLong(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    public void getZhiCommentsShort(Subscriber<ZhiCommentsBean> subscriber, int id) {

        zhiHuApi.getZhiCommentsShort(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }*/

    public void getZhiComments(Subscriber<ZhiCommentsBean> subscriber, int id, String type) {

        zhiHuApi.getZhiComments(id, type).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getStoryExtra(Subscriber<StoryExtraBean> subscriber, int id) {

        zhiHuApi.getStoryExtra(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

//
//    public void getInCarToken(Subscriber<MeiZhi.Result<List<GankAndroidBean>>> subscriber, int count) {
//
//        meiZhi.getGankRxAndroids(count).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
//
//    }

}
