package fewwind.com.myzhihu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.NetUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.Utils.imageloader.ImageLoaderFactory;
import fewwind.com.myzhihu.bean.StoryMain;
import fewwind.com.myzhihu.net.ZhiHuApi;


/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<StoryMain.TopStoriesEntity> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context,int position, StoryMain.TopStoriesEntity data) {
        imageView.setImageResource(R.drawable.ic_placeholder);
        //            如果是无图模式，仅仅在无线的条件加载图片
        if ((Boolean) SPUtils.get(context, ZhiHuApi.SET_IMAGE, true)) {

            if (NetUtil.getNetWorkType(context)==NetUtil.NETWORKTYPE_WIFI) {
                ImageLoaderFactory.getLoader().displayImage(imageView,data.getImage(),null);
            }
        } else {
            ImageLoaderFactory.getLoader().displayImage(imageView,data.getImage(),null);
        }


    }
}
