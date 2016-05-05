package fewwind.com.myzhihu.bean;

/**
 * Created by admin on 2016/5/5.
 */
public class VersionBean  {


    /**
     * status : 1
     * msg : 【更新】

     - 极大提升性能及稳定性
     - 部分用户无法使用新浪微博登录
     - 部分用户无图模式无法分享至微信及朋友圈
     * url : http://zhstatic.zhihu.com/pkg/store/daily/zhihu-daily-zhihu-2.6.0(744)-release.apk
     * latest : 2.6.0
     */

    private int status;
    private String msg;
    private String url;
    private String latest;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", latest='" + latest + '\'' +
                '}';
    }
}
