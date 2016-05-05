package fewwind.com.myzhihu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import fewwind.com.myzhihu.bean.ThemesBean;

/**
 * Created by fewwind on 2015/10/13.
 */
public class FragmentListAdapter extends FragmentPagerAdapter {


    private List<ThemesBean.OthersEntity> mTitles;
    public FragmentListAdapter(FragmentManager fm, List<ThemesBean.OthersEntity> titles ) {
        super(fm);
        this.mTitles = titles;
    }



    @Override
    public Fragment getItem(int position) {
        return ColumnContentFragment.newInstance(mTitles.get(position).getId());
    }

    @Override
    public int getCount() {
        return mTitles==null?0:mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }
}
