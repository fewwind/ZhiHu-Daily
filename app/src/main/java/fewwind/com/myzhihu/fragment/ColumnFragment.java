package fewwind.com.myzhihu.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.umeng.analytics.MobclickAgent;

import fewwind.com.myzhihu.MainActivity;
import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.bean.ThemesBean;


public class ColumnFragment extends BaseFragment {

    private TabLayout mTablayout;
    private FragmentListAdapter mFragAdapter;
    ViewPager mViewPager;
    private ThemesBean themesBean;

    public ColumnFragment() {
        // Required empty public constructor
    }

    public static ColumnFragment newInstance() {
        ColumnFragment fragment = new ColumnFragment();
        return fragment;
    }


    @Override
    protected void fragmentVisible() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_column;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mTablayout = (TabLayout) mRootView.findViewById(R.id.id_fg_tab);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.id_fg_vp);

        themesBean = ((MainActivity) getActivity()).getColumnInfo();
        if(themesBean!=null)
        mFragAdapter = new FragmentListAdapter(getChildFragmentManager(), themesBean.getOthers());

        if (mViewPager != null) {
            mViewPager.setAdapter(mFragAdapter);
//            mViewPager.setOffscreenPageLimit(0);
        }

        mTablayout.setupWithViewPager(mViewPager);


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
