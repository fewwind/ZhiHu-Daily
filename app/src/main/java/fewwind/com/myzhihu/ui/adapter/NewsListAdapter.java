package fewwind.com.myzhihu.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.DateUtil;
import fewwind.com.myzhihu.Utils.NetUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.Utils.imageloader.ImageLoaderFactory;
import fewwind.com.myzhihu.bean.StoriesEntityBean;
import fewwind.com.myzhihu.net.ZhiHuApi;

/**
 * Created by laucherish on 16/3/16.
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_NEWS = 0;
    public static final int ITEM_NEWS_DATE = 1;
    public static final int ITEM_NEWS_HEADER = -1;

    private Context mContext;
    private List<StoriesEntityBean> mNewsList;
    private long lastPos = -1;
    private boolean isAnim = true;

    private View headerView;
    OnItemClickListener itemClickListener;

    public NewsListAdapter(Context context, List<StoriesEntityBean> newsList) {
        this.mContext = context;
        this.mNewsList = newsList;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemChanged(0);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, StoriesEntityBean bean, int pos);
    }

    public void setOnItemListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && mNewsList.get(position).getType() == -1) {
            return ITEM_NEWS_HEADER;
        } else if (mNewsList.get(position).getType() == 0) {
            return ITEM_NEWS;
        } else if (mNewsList.get(position).getType() == 1) {
            return ITEM_NEWS_DATE;
        }
        return -100;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NEWS_HEADER) {
            return new NewsHeaderViewHolder(headerView);
        } else if (viewType == ITEM_NEWS_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list_date, parent, false);
            return new NewsDateViewHolder(view);
        } else if (viewType == ITEM_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list, parent, false);
            return new NewsViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final StoriesEntityBean news = mNewsList.get(position);


        if (news == null) {
            return;
        }

        if ( (Boolean) SPUtils.get(mContext,ZhiHuApi.SET_ANIM,true)) {
            startAnimator(holder.itemView, position);
        }

        if (holder instanceof NewsDateViewHolder) {
            NewsDateViewHolder dateHolder = (NewsDateViewHolder) holder;
            String dateFormat = null;
            dateFormat = DateUtil.formatDate(news.getDate());
            dateHolder.mTvNewsDate.setText(dateFormat);

        } else if (holder instanceof NewsHeaderViewHolder) {
//            bindViewHolder(holder, position, news);
        } else if (holder instanceof NewsViewHolder) {
            bindViewHolder((NewsViewHolder) holder, position, news);
        }
    }

    private void bindViewHolder(final NewsViewHolder holder, final int position, final StoriesEntityBean news) {

        holder.mTvTitle.setText(news.getTitle());
        if (!news.isRead()) {
            holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.textColorFirst_Day));
        } else {
            holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.textColorThird_Day));
        }
        List<String> images = news.getImages();

        if (news.isMultipic()){
            holder.mLabaView.setVisibility(View.VISIBLE);
        } else holder.mLabaView.setVisibility(View.GONE);


        if (images != null && images.size() > 0) {
//            如果是无图模式，仅仅在无线的条件加载图片
            if ((Boolean) SPUtils.get(mContext, ZhiHuApi.SET_IMAGE, true)) {

                if (NetUtil.getNetWorkType(mContext)==NetUtil.NETWORKTYPE_WIFI) {
                    ImageLoaderFactory.getLoader().displayImage(holder.mIvNews, images.get(0), options);
                }
            } else {
                ImageLoaderFactory.getLoader().displayImage(holder.mIvNews, images.get(0), options);
            }


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, mNewsList.get(position), position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    private void startAnimator(View view, long position) {
        if (position > lastPos) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.item_bottom_in));
            lastPos = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder != null && holder instanceof NewsViewHolder && ((NewsViewHolder) holder).mCvItem != null)
            ((NewsViewHolder) holder).mCvItem.clearAnimation();
    }

    public void changeData(List<StoriesEntityBean> newsList) {
        mNewsList = newsList;
        notifyDataSetChanged();
    }

    public void addData(List<StoriesEntityBean> newsList) {
        if (mNewsList == null) {
            changeData(newsList);
        } else {
            mNewsList.addAll(newsList);
            notifyDataSetChanged();
        }
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    public void setmNewsList(List<StoriesEntityBean> mNewsList) {
        this.mNewsList = mNewsList;
    }

    public List<StoriesEntityBean> getmNewsList() {
        return mNewsList;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView mCvItem;

        ImageView mIvNews;

        TextView mTvTitle;
        LabelView mLabaView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            mCvItem = (CardView) itemView.findViewById(R.id.cv_item);
            mIvNews = (ImageView) itemView.findViewById(R.id.iv_news);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mLabaView = (LabelView) itemView.findViewById(R.id.id_labeview);
        }
    }

    class NewsDateViewHolder extends RecyclerView.ViewHolder {
        TextView mTvNewsDate;

        public NewsDateViewHolder(View itemView) {
            super(itemView);
            mTvNewsDate = (TextView) itemView.findViewById(R.id.tv_news_date);
        }
    }

    class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        public NewsHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_placeholder)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(10))
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
}
