package fewwind.com.myzhihu.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.AppContextUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.net.ZhiHuApi;


/**
 */
public class SettingFragment extends PreferenceFragment {


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_set);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(android.R.color.transparent);
        return view;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        //如果“保存个人信息”这个按钮被选中，将进行括号里面的操作

        if ("set_cache".equals(preference.getKey())) {
            boolean set_cache = preference.getSharedPreferences().getBoolean("set_cache", false);
            SPUtils.put(getActivity(), ZhiHuApi.SET_CACHE, set_cache);

        } else if ("set_image".equals(preference.getKey())) {
            boolean set_image = preference.getSharedPreferences().getBoolean("set_image", false);
            SPUtils.put(getActivity(), ZhiHuApi.SET_IMAGE, set_image);
        } else if ("set_anim".equals(preference.getKey())) {
            boolean set_anim = preference.getSharedPreferences().getBoolean("set_anim", false);
            SPUtils.put(getActivity(), ZhiHuApi.SET_ANIM, set_anim);
        } else if ("set_about".equals(preference.getKey())) {
            initAbout();
        } else if ("set_like".equals(preference.getKey())) {
            givePraise();
        } else if ("set_money".equals(preference.getKey())) {
            giveMoney();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }


    public void initAbout() {

        new AlertDialog.Builder(getActivity())
                .setTitle("当前版本号：" + AppContextUtil.getAppVersion(getActivity()))
                .setMessage("本程序用到了多个开源框架，仅供学习交流使用 ，稍后会开源。\n 我的git：\n  https://github.com/fewwind \n 我的邮箱：\n  fewwind@126.com")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        copyFile(srcFile, exportFile);
                    }
                })
               /* .setNegativeButton(R.string.dialog_now_watch, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        browseFile(exportFile.getParentFile());
                    }
                })*/
                .show();
    }

    public void givePraise() {

        AppContextUtil.launchAppDetail(getActivity().getPackageName(), "com.coolapk.market", getActivity());

    }


    public void giveMoney() {

        ImageView iv = new ImageView(getActivity());
        iv.setImageResource(R.drawable.weiixn);
        new AlertDialog.Builder(getActivity())
                .setTitle("微信扫一扫")
                .setPositiveButton("没钱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(iv).show();
    }

}
