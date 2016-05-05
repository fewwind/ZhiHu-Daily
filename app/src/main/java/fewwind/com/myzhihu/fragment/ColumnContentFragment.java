package fewwind.com.myzhihu.fragment;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import fewwind.com.myzhihu.App;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.activity.NewsContentActivity;
import fewwind.com.myzhihu.bean.NewSendBean;
import fewwind.com.myzhihu.bean.StoriesEntityBean;
import fewwind.com.myzhihu.bean.ZhiThemeBean;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.ui.adapter.NewsListAdapter;
import rx.Subscriber;


public class ColumnContentFragment extends BaseFragment implements NewsListAdapter.OnItemClickListener {

    private int mColumnId;
    private static String KEY_CULUMNID = "columnId";

    private RecyclerView mRv;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsListAdapter mNewsListAdapter;
    List<StoriesEntityBean> storiesLocal = new ArrayList<>();
    public ColumnContentFragment() {
        // Required empty public constructor
    }

    public static ColumnContentFragment newInstance(int columnId) {
        ColumnContentFragment fragment = new ColumnContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_CULUMNID, columnId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void fragmentVisible() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_column_content;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mRv = (RecyclerView) mRootView.findViewById(R.id.id_column_rv);


        mColumnId = getArguments().getInt(KEY_CULUMNID);
        ZhiThemeBean storyMain = App.cacheHelper.getAsSerializable(""+mColumnId);
        if (storyMain!=null){
            handleResultData(storyMain);
            return;
        }

        HttpMethods.getInstance().getZhiThemeMsg(new Subscriber<ZhiThemeBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.w(e.toString());
            }

            @Override
            public void onNext(ZhiThemeBean storyMain) {
                Logger.d(storyMain.toString());

                App.cacheHelper.put(""+mColumnId,storyMain);
                handleResultData(storyMain);


            }
        }, mColumnId);
    }

    private void handleResultData(ZhiThemeBean storyMain) {
        //配置RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLinearLayoutManager);
        mRv.setHasFixedSize(true);
        mRv.setItemAnimator(new DefaultItemAnimator());
        //因为服务器返回的type 不固定，所以转化一下，专栏列表的type应该都是0 ，都是新闻列表
        List<StoriesEntityBean> stories = storyMain.getStories();

        for (StoriesEntityBean bean:stories) {
            bean.setType(0);
            storiesLocal.add(bean);
        }
        mNewsListAdapter = new NewsListAdapter(getActivity(), storiesLocal);
        mNewsListAdapter.setOnItemListener(ColumnContentFragment.this);
        mRv.setAdapter(mNewsListAdapter);
    }


    @Override
    public void onItemClick(View view, StoriesEntityBean bean, int pos) {
        Bundle bundle = new Bundle();
        Logger.d(bean.toString());
        NewSendBean sendBean = new NewSendBean(bean.getId(), bean.getTitle(), bean.getImages()==null?"":bean.getImages().get(0));
        bundle.putSerializable(NewsContentActivity.NEW_INFO, sendBean);
        bundle.putSerializable(NewsContentActivity.STORY_BEAN, bean);
        bean.setIsRead(true);
        storiesLocal.set(pos, bean);
        mNewsListAdapter.notifyItemChanged(pos);
        NewsContentActivity.startActivity(getActivity(), bundle);
    }
}
