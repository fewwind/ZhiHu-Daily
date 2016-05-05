package fewwind.com.myzhihu.fragment.fragmentutil;

import android.support.v4.app.Fragment;

import fewwind.com.myzhihu.fragment.ColumnFragment;
import fewwind.com.myzhihu.fragment.FavFragment;
import fewwind.com.myzhihu.fragment.MainFragment;

/**
 * https://github.com/Aspsine/FragmentNavigator
 * Created by aspsine on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"热门", "专栏", "收藏","设置"};

    @Override
    public Fragment onCreateFragment(int position) {
        switch (position) {
            case 0:
                return MainFragment.newInstance(position);
            case 1:
                return ColumnFragment.newInstance();
            case 2:
                return FavFragment.newInstance(0);
            case 3:
                return FavFragment.newInstance(1);
            default:
                return null;
        }
    }

    @Override
    public String getTag(int position) {
        return TABS[position];
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
