package fewwind.com.myzhihu.ui.view;


import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import fewwind.com.myzhihu.R;
import fewwind.com.myzhihu.Utils.DateUtil;
import fewwind.com.myzhihu.Utils.NetUtil;
import fewwind.com.myzhihu.Utils.SPUtils;
import fewwind.com.myzhihu.Utils.imageloader.ImageLoaderFactory;
import fewwind.com.myzhihu.bean.StoryExtraBean;
import fewwind.com.myzhihu.bean.ZhiCommentsBean;
import fewwind.com.myzhihu.net.HttpMethods;
import fewwind.com.myzhihu.net.ZhiHuApi;
import fewwind.com.myzhihu.ui.view.progress.CircularProgressView;
import rx.Subscriber;

public class BottomSheetDialogView {

    CommonAdapter commonAdapter;
    List<ZhiCommentsBean.CommentsBean> mDatas = new ArrayList<>();
    Context mContext;

    TextView mTvShort;
    TextView mTvLong;
    TextView mTvCommentAll;
    RecyclerView recyclerView;

    CircularProgressView mPb;
    private int mId;

    /**
     * remember to call setLocalNightMode for dialog
     *
     * @param context
     */
    public BottomSheetDialogView(final Context context, int id) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        mContext = context;
        mId = id;
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_recycler_view, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.id_bottom_sheet_list_view);
        mTvShort = (TextView) view.findViewById(R.id.id_comment_tv_short);
        mTvLong = (TextView) view.findViewById(R.id.id_comment_tv_long);
        mTvCommentAll = (TextView) view.findViewById(R.id.id_comment_tv_like_all);
        mPb = (CircularProgressView) view.findViewById(R.id.id_comment_pb);

        mTvShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComments(mId, ZhiHuApi.URL_COMMENT_SHORT);

                mTvShort.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                mTvLong.setTextColor(mContext.getResources().getColor(R.color.color_unread));

                mPb.setVisibility(View.VISIBLE);
            }
        });

        mTvLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComments(mId, ZhiHuApi.URL_COMMENT_LONG);

                mTvLong.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                mTvShort.setTextColor(mContext.getResources().getColor(R.color.color_unread));
                mPb.setVisibility(View.VISIBLE);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        dialog.setContentView(view);
        dialog.show();


        getComments(id, ZhiHuApi.URL_COMMENT_SHORT);

        getStoryExtraInfo(id);

    }

    public void updateList(List<ZhiCommentsBean.CommentsBean> datasNew) {
        mDatas = datasNew;
        Logger.v(mDatas.toString());
        if (mDatas.toString()==null) Toast.makeText(mContext, "还没有人评论呢", Toast.LENGTH_SHORT).show();
        commonAdapter.notifyDataSetChanged();
    }


    public void getComments(int id, String type) {
        HttpMethods.getInstance().getZhiComments(new Subscriber<ZhiCommentsBean>() {
            @Override
            public void onCompleted() {
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.toString());
                mPb.setVisibility(View.GONE);
                Toast.makeText(mContext, "请求失败.......", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ZhiCommentsBean zhiCommentsBean) {
                if (zhiCommentsBean == null) {

                    return;
                }
                mDatas.clear();
                mDatas = zhiCommentsBean.getComments();

                if (commonAdapter != null) {

                    updateList(zhiCommentsBean.getComments());
                }
                {
                    recyclerView.setAdapter(commonAdapter = new CommonAdapter<ZhiCommentsBean.CommentsBean>(mContext, R.layout.item_commten_list, mDatas) {
                        @Override
                        public void convert(ViewHolder holder, ZhiCommentsBean.CommentsBean commentsBean) {
                            holder.setText(R.id.id_comment_tv_name, commentsBean.getAuthor());
                            holder.setText(R.id.id_comment_tv_content, commentsBean.getContent());
                            holder.setText(R.id.id_comment_tv_like, commentsBean.getLikes() + "");
                            holder.setText(R.id.id_comment_tv_time, DateUtil.formatDateDT(commentsBean.getTime() * 1000L));

//            如果是无图模式，仅仅在无线的条件加载图片
                            if ((Boolean) SPUtils.get(mContext, ZhiHuApi.SET_IMAGE, true)) {
                                if (NetUtil.getNetWorkType(mContext)==NetUtil.NETWORKTYPE_WIFI) {
                                    ImageLoaderFactory.getLoader().displayImage((CircleImageView) holder.getView(R.id.id_comment_iv_avatar), commentsBean.getAvatar(), null);
                                }
                            } else {
                                ImageLoaderFactory.getLoader().displayImage((CircleImageView) holder.getView(R.id.id_comment_iv_avatar), commentsBean.getAvatar(), null);
                            }

                        }

                    });
                }


            }
        }, id, type);
    }


    public void getStoryExtraInfo(int id){
        HttpMethods.getInstance().getStoryExtra(new Subscriber<StoryExtraBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(StoryExtraBean storyExtraBean) {
                mTvShort.setText("短评论"+"("+storyExtraBean.getShort_comments()+")");
                mTvLong.setText("长评论"+"("+storyExtraBean.getLong_comments()+")");
                mTvCommentAll.setText(storyExtraBean.getPopularity()+"");
            }
        },id);
    }

}
