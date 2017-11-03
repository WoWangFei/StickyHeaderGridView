package com.example.stickyheadergridview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stickyheadergridview.MyImageView.OnMeasureListener;
import com.example.stickyheadergridview.NativeImageLoader.NativeImageCallBack;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class StickyGridAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter {

	private List<GridItem> list;
	private LayoutInflater mInflater;
	private GridView mGridView;
	private Point mPoint = new Point(0, 0);//������װImageView�Ŀ�͸ߵĶ��� 

	public StickyGridAdapter(Context context, List<GridItem> list,
			GridView mGridView) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		this.mGridView = mGridView;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_item, parent, false);
			mViewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.grid_item);
			convertView.setTag(mViewHolder);
			
			 //��������ImageView�Ŀ�͸�  
			mViewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {  
                  
                @Override  
                public void onMeasureSize(int width, int height) {  
                    mPoint.set(width, height);  
                }  
            }); 
			
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		String path = list.get(position).getPath();
		mViewHolder.mImageView.setTag(path);

		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint,
				new NativeImageCallBack() {

					@Override
					public void onImageLoader(Bitmap bitmap, String path) {
						ImageView mImageView = (ImageView) mGridView
								.findViewWithTag(path);
						if (bitmap != null && mImageView != null) {
							mImageView.setImageBitmap(bitmap);
						}
					}
				});

		if (bitmap != null) {
			mViewHolder.mImageView.setImageBitmap(bitmap);
		} else {
			mViewHolder.mImageView
					.setImageResource(R.drawable.friends_sends_pictures_no);
		}

		return convertView;
	}
	

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder mHeaderHolder;
		if (convertView == null) {
			mHeaderHolder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			mHeaderHolder.mTextView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(mHeaderHolder);
		} else {
			mHeaderHolder = (HeaderViewHolder) convertView.getTag();
		}

		mHeaderHolder.mTextView.setText(list.get(position).getTime());

		return convertView;
	}

	public static class ViewHolder {
		public MyImageView mImageView;
	}

	public static class HeaderViewHolder {
		public TextView mTextView;
	}

	@Override
	public long getHeaderId(int position) {
		return list.get(position).getSection();
	}

}
