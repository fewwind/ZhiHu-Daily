package fewwind.com.myzhihu.net;

import fewwind.com.myzhihu.bean.StoryExtraBean;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.bean.ThemesBean;
import fewwind.com.myzhihu.bean.VersionBean;
import fewwind.com.myzhihu.bean.ZhiCommentsBean;
import fewwind.com.myzhihu.bean.ZhiContentBean;
import fewwind.com.myzhihu.bean.ZhiMsgBean;
import fewwind.com.myzhihu.bean.ZhiThemeBean;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by fewwind on 2016/4/7.
 */
public interface ZhiHuApi {

    String KEY_LATEST_NEWS = "latest_news";
    String KEY_COLUMN_TITLE= "column_title";
    String KEY_FAV_NEWS= "fav_news";

    String KEY_VERSION_LOCAL= "local_version";
    String KEY_IS_FIRST= "isfirstopen";


    String SET_CACHE= "cache";
    String SET_IMAGE= "image";
    String SET_ANIM= "anim";

    String URL_COMMENT_LONG = "long-comments";
     String URL_COMMENT_SHORT = "short-comments";


    @GET ("news/latest")
    Observable<StoryMain> getStoryMain();
    @GET ("themes")
    Observable<ThemesBean> getZhiThemes();
    @GET ("news/{id}")
    Observable<ZhiContentBean> getZhiContent(@Path("id") int id);
    @GET ("news/before/{data}")
    Observable<ZhiMsgBean> getZhiMsg(@Path("data") String data);
    @GET ("theme/{id}")
    Observable<ZhiThemeBean> getZhiThemeMsg(@Path("id") int id);
    //http://news-at.zhihu.com/api/4/story/4232852/long-comments
/*    @GET ("story/{id}/long-comments")
    Observable<ZhiCommentsBean> getZhiCommentsLong(@Path("id") int id);
    @GET ("story/{id}/short-comments")
    Observable<ZhiCommentsBean> getZhiCommentsShort(@Path("id") int id);*/

    @GET ("story/{id}/{type}")
    Observable<ZhiCommentsBean> getZhiComments(@Path("id") int id,@Path("type") String type);

    @GET ("story-extra/{id}")
    Observable<StoryExtraBean> getStoryExtra(@Path("id") int id);

    //https://raw.githubusercontent.com/fewwind/InCarMedia/master/version.json
    @GET("master/version.json")
    Call<VersionBean> getVersionInfo();
}
