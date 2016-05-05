package fewwind.com.myzhihu.Utils.imageloader;

//1、声明 ImageLoaderWrapper 接口，定义一些抽象的加载接口方法

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;

public interface ImageLoaderWrapper {

    /**
     * 显示 图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, File imageFile, DisplayOption option);

    /**
     * 显示图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageUrl  图片资源的URL
     * @param option    显示参数设置
     */
    public void displayImage(ImageView imageView, String imageUrl, DisplayImageOptions option);

    /**
     * 图片加载参数
     */
    public static class DisplayOption {
        /**
         * 加载中的资源id
         */
        public int loadingResId;
        /**
         * 加载失败的资源id
         */
        public int loadErrorResId;
    }
}