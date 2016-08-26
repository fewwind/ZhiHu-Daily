package fewwind.com.myzhihu.bean;

/**
 * Created by admin on 2016/5/25.
 */
public class request {

    /**
     * appId : 10048
     * lang : en
     * classId : 100
     * country : CN
     * type : 0
     */

    private int appId;
    private String lang;
    private int classId;
    private String country;
    private int type;

    public request(int appId, String lang, int classId, String country, int type) {
        this.appId = appId;
        this.lang = lang;
        this.classId = classId;
        this.country = country;
        this.type = type;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
