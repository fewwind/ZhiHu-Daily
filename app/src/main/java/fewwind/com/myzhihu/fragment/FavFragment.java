package fewwind.com.myzhihu.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.activity.NewsContentActivity;
import fewwind.com.myzhihu.bean.NewSendBean;
import fewwind.com.myzhihu.bean.StoriesEntityBean;
import fewwind.com.myzhihu.bean.ZhiMsgBean;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.net.ZhiHuApi;
import fewwind.com.myzhihu.ui.adapter.NewsListAdapter;
import rx.Subscriber;

public class FavFragment extends BaseFragment implements NewsListAdapter.OnItemClickListener {

    private RecyclerView mRv;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsListAdapter mNewsListAdapter;
    List<StoriesEntityBean> storiesLocal = new ArrayList<>();
    boolean isPreper;
    List<StoriesEntityBean> stories;
    public static String TYPE_FAV = "is_fav_fragment";

    FloatingActionButton mFabDate;
    //根据传过来的参数，如果为0就是收藏页面，为1就是日期选择页面
    int type = 0;

    public static FavFragment newInstance(int type) {
        FavFragment fragment = new FavFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_FAV, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE_FAV);
        }
    }


    @Override
    protected void fragmentVisible() {

        Logger.v("收藏页面变得可见");
        updateFavNews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fav;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        mRv = (RecyclerView) mRootView.findViewById(R.id.id_fav_rv);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLinearLayoutManager);
        mRv.setHasFixedSize(true);
        mRv.setItemAnimator(new DefaultItemAnimator());
        mFabDate = (FloatingActionButton) mRootView.findViewById(R.id.id_fab_date);


        mNewsListAdapter = new NewsListAdapter(getActivity(), storiesLocal);

        mNewsListAdapter.setOnItemListener(FavFragment.this);
        mRv.setAdapter(mNewsListAdapter);
        if (type == 0) {

            mFabDate.setVisibility(View.GONE);
            //因为服务器返回的type 不固定，所以转化一下，专栏列表的type应该都是0 ，都是新闻列表
            stories = App.cacheHelper.getAsSerializable(ZhiHuApi.KEY_FAV_NEWS);
            if (stories == null) {
                Snackbar.make(mRv, "还没收藏过呢········", Snackbar.LENGTH_SHORT).show();

                return;
            }

            storiesLocal.clear();
            for (StoriesEntityBean bean : stories) {
                bean.setType(0);
                storiesLocal.add(bean);
            }
        } else {

            mFabDate.setVisibility(View.VISIBLE);
            Date d = new Date();
            SimpleDateFormat ss = new SimpleDateFormat("yyyyMMdd");
            String today = ss.format(d);

            loadNewsByDate(today);

        }


        mFabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectDateLoadNews();
            }
        });


        isPreper = true;
    }

    /**
     * 根据用户选择的日期加载当天的日报
     */
    private void selectDateLoadNews() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String data = new StringBuilder().append(year)
                        .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))

                        .append((day < 10) ? "0" + day : day).toString();
                Logger.v(data);
                /**
                 * 如果没缓存就去请求网络
                 */
                if (!loadBeforeNewsCache(data)) {
                    loadNewsByDate(data);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public void updateFavNews() {
        if (!isPreper) return;
        //因为服务器返回的type 不固定，所以转化一下，专栏列表的type应该都是0 ，都是新闻列表
        List<StoriesEntityBean> stories = App.cacheHelper.getAsSerializable(ZhiHuApi.KEY_FAV_NEWS);
        if (stories == null) {
            Snackbar.make(mRv, "还没收藏过呢········", Snackbar.LENGTH_SHORT).show();
            return;
        }
        storiesLocal.clear();
        for (StoriesEntityBean bean : stories) {
            bean.setType(0);
            storiesLocal.add(bean);
        }
        mNewsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, StoriesEntityBean bean, int pos) {
        Bundle bundle = new Bundle();
        Logger.d(bean.toString());
        NewSendBean sendBean = new NewSendBean(bean.getId(), bean.getTitle(), bean.getImages() == null ? "" : bean.getImages().get(0));
        bundle.putSerializable(NewsContentActivity.NEW_INFO, sendBean);
        bundle.putSerializable(NewsContentActivity.STORY_BEAN, bean);
        bean.setIsRead(true);
        storiesLocal.set(pos, bean);
        mNewsListAdapter.notifyItemChanged(pos);
        NewsContentActivity.startActivity(getActivity(), bundle);
    }

    // 从网络加载每天的新闻列表，根据上一条信息返回的日期，最后保存到缓存里
    private void loadNewsByDate(final String data) {
        HttpMethods.getInstance().getZhiMsg(new Subscriber<ZhiMsgBean>() {
            @Override
            public void onCompleted() {
            }


            @Override
            public void onError(Throwable e) {

                Toast.makeText(getActivity(), "那年知乎还没成立", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ZhiMsgBean zhiMsgBean) {
                //注意：：：：：：：：：：
                // 服务器返回的日期  是 后一天的，当前日期随时在变
                Logger.d("服务器成功并且保存==" + data);
                App.cacheHelper.put(data, zhiMsgBean);
                stories = zhiMsgBean.getStories();

                storiesLocal.clear();
                for (StoriesEntityBean bean : stories) {
                    bean.setType(0);
                    storiesLocal.add(bean);
                }
                mNewsListAdapter.notifyDataSetChanged();


            }
        }, data);
    }

    /**
     * 加载本地缓存，根据key就是 每条新闻的日期
     *
     * @param key 每条新闻的日期
     * @return false 代表没有缓存
     */
    private boolean loadBeforeNewsCache(final String key) {

        ZhiMsgBean zhiMsgBean = App.cacheHelper.getAsSerializable(key);
//        isLoadingMore = false;
        if (zhiMsgBean == null) return false;
        stories = zhiMsgBean.getStories();

        storiesLocal.clear();
        for (StoriesEntityBean bean : stories) {
            bean.setType(0);
            storiesLocal.add(bean);
        }
        mNewsListAdapter.notifyDataSetChanged();
        return true;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }
}
