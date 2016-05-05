package fewwind.com.myzhihu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;

	public CommonAdapter(Context context, List<T> datas, int layoutId)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}

	@Override
	public int getCount()
	{
		return mDatas.size()>0?mDatas.size():0;
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public void updateDatas(List<T> datas){
		if (datas==null) {
			return ;
		}
		mDatas.addAll(0, datas);
//		mDatas.addAll(datas);
		this.notifyDataSetChanged();
	}
	
	public void updateClearDatas(List<T> datas){
		mDatas.clear();
		mDatas.addAll(datas);
		this.notifyDataSetChanged();
	}

	public abstract void convert(ViewHolder holder, T t);

}
