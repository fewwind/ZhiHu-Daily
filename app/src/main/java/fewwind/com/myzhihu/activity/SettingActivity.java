package fewwind.com.myzhihu.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.fragment.SettingFragment;

public class SettingActivity extends BaseActivity {


    SettingFragment setFrag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        initToolBar("设置");

        if (savedInstanceState == null) {
            setFrag =  SettingFragment.newInstance();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.id_layout_setting_container, setFrag);
            fragmentTransaction.commit();


        }
    }


    public static void startSettingActivity(Context context) {
        Intent intent
                = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
