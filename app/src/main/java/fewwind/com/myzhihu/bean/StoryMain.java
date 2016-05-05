package fewwind.com.myzhihu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fewwind on 2016/4/7.
 */
public class StoryMain implements Serializable {

    /**
     * date : 20160407
     * stories : [{"images":["http://pic1.zhimg.com/3c78356485e93214ea88e3804c74e7d0.jpg"],"type":0,"id":8118151,"ga_prefix":"040711","title":"遇到类似「酒店遇袭事件」，这些投诉渠道很有用"},{"images":["http://pic3.zhimg.com/ded0b2c2ae94304173ddd1ad61668a86.jpg"],"type":0,"id":8117193,"ga_prefix":"040710","title":"感染艾滋病是一种怎样的经历？"},{"images":["http://pic3.zhimg.com/df47bd5e8f111885c2402e6f8aee8bca.jpg"],"type":0,"id":8114135,"ga_prefix":"040709","title":"社保费率又下调，以后靠社保还养得起老吗？"},{"images":["http://pic1.zhimg.com/fe43c150bdbd01e2b259fb3fd9e952a8.jpg"],"type":0,"id":8115731,"ga_prefix":"040708","title":"年轻人得了老人的「痴呆症」，是有可能传染的"},{"images":["http://pic3.zhimg.com/aa5fca9d8a601bc3f293ac00001f24e6.jpg"],"type":0,"id":8116375,"ga_prefix":"040707","title":"研究表明，想让农民起义少点儿，多吃红薯吧"},{"images":["http://pic3.zhimg.com/60fa11e8669df8660e32b0f6f7d2b63a.jpg"],"type":0,"id":8112423,"ga_prefix":"040707","title":"我知道你想上天，所以我先上去帮你看看"},{"images":["http://pic3.zhimg.com/ac37a7ab4eb15df00ea44d677f884b5a.jpg"],"type":0,"id":8115590,"ga_prefix":"040707","title":"这些讨厌的寄生虫，到底有什么存在的意义\u2026\u2026"},{"images":["http://pic1.zhimg.com/bce922b4d7bf2582093e512bc89d6a6c.jpg"],"type":0,"id":8116282,"ga_prefix":"040707","title":"读读日报 24 小时热门 TOP 5 · 陌生人强行拖拽你，如何有效呼救"},{"images":["http://pic4.zhimg.com/255832d68abb6a72e0e047c32c435317.jpg"],"type":0,"id":8114806,"ga_prefix":"040706","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic3.zhimg.com/343294d8fd4d6eda2e707f4b2dd49b4a.jpg","type":0,"id":8118151,"ga_prefix":"040711","title":"遇到类似「酒店遇袭事件」，这些投诉渠道很有用"},{"image":"http://pic2.zhimg.com/b85c9d1b59fe8353875fe19579a9d979.jpg","type":0,"id":8117193,"ga_prefix":"040710","title":"感染艾滋病是一种怎样的经历？"},{"image":"http://pic2.zhimg.com/1f109117d853c4d7b2f8e36a9e1a1e85.jpg","type":0,"id":8116282,"ga_prefix":"040707","title":"读读日报 24 小时热门 TOP 5 · 陌生人强行拖拽你，如何有效呼救"},{"image":"http://pic1.zhimg.com/069f3fc852a2cf6ce844c6f768740178.jpg","type":0,"id":8114135,"ga_prefix":"040709","title":"社保费率又下调，以后靠社保还养得起老吗？"},{"image":"http://pic1.zhimg.com/69f77402231cc07d2fcaf232f9e601dc.jpg","type":0,"id":7814996,"ga_prefix":"040621","title":"暴力、搞笑、B 级片，黑帮的故事充满了江湖气息"}]
     */

    private String date;
    /**
     * images : ["http://pic1.zhimg.com/3c78356485e93214ea88e3804c74e7d0.jpg"]
     * type : 0
     * id : 8118151
     * ga_prefix : 040711
     * title : 遇到类似「酒店遇袭事件」，这些投诉渠道很有用
     */

    private List<StoriesEntityBean> stories;
    /**
     * image : http://pic3.zhimg.com/343294d8fd4d6eda2e707f4b2dd49b4a.jpg
     * type : 0
     * id : 8118151
     * ga_prefix : 040711
     * title : 遇到类似「酒店遇袭事件」，这些投诉渠道很有用
     */

    private List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntityBean> stories) {
        this.stories = stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    @Override
    public String toString() {
        return "StoryMain{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntityBean> getStories() {
        return stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return top_stories;
    }



    public static class TopStoriesEntity implements Serializable {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public void setImage(String image) {
            this.image = image;
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

        public String getImage() {
            return image;
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
    }
}
