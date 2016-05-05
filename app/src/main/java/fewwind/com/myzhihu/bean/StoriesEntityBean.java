package fewwind.com.myzhihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fewwind on 2016/4/8.
 */

public class StoriesEntityBean implements Serializable {
    private int type;
    private int id;
    private String ga_prefix;
    private String title;
    private List<String> images;

    private String date;
    private boolean isRead;
    private boolean multipic;

    @Override
    public String toString() {
        return "StoriesEntity{" +
                "type=" + type +
                ", id=" + id +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                '}';
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public StoriesEntityBean(int type, int id, String ga_prefix, String title, List<String> images, String date, boolean isRead) {
        this.type = type;
        this.id = id;
        this.ga_prefix = ga_prefix;
        this.title = title;
        this.images = images;
        this.date = date;
        this.isRead = isRead;
    }

    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
    }
}
