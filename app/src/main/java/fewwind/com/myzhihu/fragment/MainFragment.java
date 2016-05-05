package fewwind.com.myzhihu.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.MainActivity;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.activity.NewsContentActivity;
import fewwind.com.myzhihu.bean.NewSendBean;
import fewwind.com.myzhihu.bean.StoriesEntityBean;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.bean.ZhiMsgBean;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.ui.adapter.NetworkImageHolderView;
import fewwind.com.myzhihu.ui.adapter.NewsListAdapter;
import rx.Subscriber;


public class MainFragment extends BaseFragment implements NewsListAdapter.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final long REFRESH_DELAY = 1000;

    // TODO: Rename and change types of parameters
    private int pos;
    private ConvenientBanner mBanner;
    private View mHeaderView;

    boolean isLoadingMore = false;
    private StoryMain mStoryMain;
    private RecyclerView mRcvNewsList;
    private NewsListAdapter mNewsListAdapter;
    private PullToRefreshView mPullToRefreshView;

    private List<StoriesEntityBean> mNewsData = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    public MainFragment() {
        // Required empty public constructor
    }


    private String curDate;

    public static MainFragment newInstance(int pos) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mStoryMain = ((MainActivity) getActivity()).getLastInfo();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRcvNewsList = (RecyclerView) view.findViewById(R.id.id_main_rv);
        //配置RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRcvNewsList.setLayoutManager(mLinearLayoutManager);
        mRcvNewsList.setHasFixedSize(true);
        mRcvNewsList.setItemAnimator(new DefaultItemAnimator());

        mNewsListAdapter = new NewsListAdapter(getActivity(), mNewsData);
        mRcvNewsList.setAdapter(mNewsListAdapter);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(mRcvNewsList, "刷新完成·······", Snackbar.LENGTH_SHORT).show();

                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.view_header, mRcvNewsList, false);
        mBanner = (ConvenientBanner) mHeaderView.findViewById(R.id.id_banner_first);


        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, mStoryMain.getTop_stories()).setPageIndicator(new int[]{R.drawable.point_nomal, R.drawable.point_focured})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        curDate = mStoryMain.getDate();
        StoriesEntityBean date = new StoriesEntityBean(-1, 0, null, null, null, "20150206", false);
        StoriesEntityBean date1 = new StoriesEntityBean(1, 0, null, null, null, mStoryMain.getDate(), false);
        mNewsData.add(0, date);
        mNewsData.add(1, date1);

        mNewsData.addAll(mStoryMain.getStories());
        mNewsListAdapter.setHeaderView(mHeaderView);
        mNewsListAdapter.notifyDataSetChanged();
        mNewsListAdapter.setOnItemListener(this);


        mBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StoryMain.TopStoriesEntity bean = mStoryMain.getTop_stories().get(position);
                Bundle bundle = new Bundle();
                NewSendBean sendBean = new NewSendBean(bean.getId(), bean.getTitle(), bean.getImage());
                bundle.putSerializable(NewsContentActivity.NEW_INFO, sendBean);
                NewsContentActivity.startActivity(getActivity(), bundle);
            }
        });


        mRcvNewsList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastVisibleItem = ((LinearLayoutManager) mLinearLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mNewsListAdapter.getItemCount();
                //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载，各位自由选择

                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {

//                    Logger.v(lastVisibleItem+"==开始上拉加载++"+totalItemCount+"+++"+isLoadingMore );
                    if (isLoadingMore) {
                    } else {
                        Logger.v("开始上拉加载");
                        isLoadingMore = true;
                        if (!loadBeforeNewsCache(curDate)) {
                            loadBeforeNews(curDate);
                        }
//                        loadBeforeNews(curDate);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return view;
    }

    @Override
    protected void fragmentVisible() {

    }

    public void scrollTop() {
        mRcvNewsList.scrollToPosition(0);
    }

    // 从网络加载每天的新闻列表，根据上一条信息返回的日期，最后保存到缓存里
    private void loadBeforeNews(final String data) {
        HttpMethods.getInstance().getZhiMsg(new Subscriber<ZhiMsgBean>() {
            @Override
            public void onCompleted() {
                isLoadingMore = false;
            }


            @Override
            public void onError(Throwable e) {
                isLoadingMore = false;
            }

            @Override
            public void onNext(ZhiMsgBean zhiMsgBean) {
                //注意：：：：：：：：：：
                // 服务器返回的日期  是 后一天的，当前日期随时在变
                Logger.d("服务器成功并且保存==" + data);
                App.cacheHelper.put(data, zhiMsgBean);
                StoriesEntityBean date = new StoriesEntityBean(1, 0, null, null, null, zhiMsgBean.getDate(), false);
                curDate = zhiMsgBean.getDate();
                Logger.d("变了之后" + curDate);
                List<StoriesEntityBean> stories = zhiMsgBean.getStories();
                stories.add(0, date);
                mNewsData.addAll(stories);
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
        if (zhiMsgBean == null) return false;
        StoriesEntityBean date = new StoriesEntityBean(1, 0, null, null, null, zhiMsgBean.getDate(), false);
        curDate = zhiMsgBean.getDate();
        List<StoriesEntityBean> stories = zhiMsgBean.getStories();
        stories.add(0, date);
        mNewsData.addAll(stories);
        mNewsListAdapter.notifyDataSetChanged();
        isLoadingMore = false;
        return true;
    }


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startTurning(2000);

    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopTurning();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(View view, StoriesEntityBean bean, int pos) {
        Bundle bundle = new Bundle();
        //之所以转化一下是因为，banner 的轮播图信息和消息列表不是一个类型，为了兼容，但是发现收藏功能必须使用原始数据
        NewSendBean sendBean = new NewSendBean(bean.getId(), bean.getTitle(), bean.getImages().get(0));
        bundle.putSerializable(NewsContentActivity.NEW_INFO, sendBean);
        bundle.putSerializable(NewsContentActivity.STORY_BEAN, bean);
        bean.setIsRead(true);
        mNewsData.set(pos, bean);
        mNewsListAdapter.notifyItemChanged(pos);
        NewsContentActivity.startActivity(getActivity(), bundle);

/*        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        intent.putExtras(bundle);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                new Pair<View, String>(view.findViewById(R.id.iv_news),
                        NewsContentActivity.VIEW_NAME_HEADER_IMAGE));

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());*/


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
