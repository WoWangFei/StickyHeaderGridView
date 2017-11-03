package com.example.stickyheadergridview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;

import com.example.stickyheadergridview.ImageScanner.ScanCompleteCallBack;

public class MainActivity extends Activity {
	private ProgressDialog mProgressDialog;
	private ImageScanner mScanner;
	private GridView mGridView;
	private List<GridItem> mGirdList = new ArrayList<GridItem>();
	private static int section = 1;
	private Map<String, Integer> sectionMap = new HashMap<String, Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGridView = (GridView) findViewById(R.id.asset_grid);
		mScanner = new ImageScanner(this);
		
		
		mScanner.scanImages(new ScanCompleteCallBack() {
			{
				mProgressDialog = ProgressDialog.show(MainActivity.this, null, "���ڼ���...");
			}
			
			@Override
			public void scanComplete(Cursor cursor) {
				// �رս�����
				mProgressDialog.dismiss();
				
				while (cursor.moveToNext()) {
					// ��ȡͼƬ��·��
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					long times = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
					
					GridItem mGridItem = new GridItem(path, paserTimeToYM(times));
					mGirdList.add(mGridItem);

				}
				cursor.close();
				Collections.sort(mGirdList, new YMComparator());
				
				for(ListIterator<GridItem> it = mGirdList.listIterator(); it.hasNext();){
					GridItem mGridItem = it.next();
					String ym = mGridItem.getTime();
					if(!sectionMap.containsKey(ym)){
						mGridItem.setSection(section);
						sectionMap.put(ym, section);
						section ++;
					}else{
						mGridItem.setSection(sectionMap.get(ym));
					}
				}
				
				mGridView.setAdapter(new StickyGridAdapter(MainActivity.this, mGirdList, mGridView));
				
			}
		});
	}

	
	public static String paserTimeToYM(long time) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��");
		return format.format(new Date(time * 1000L));
	}

}
