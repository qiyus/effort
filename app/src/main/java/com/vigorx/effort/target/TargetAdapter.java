package com.vigorx.effort.target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vigorx.effort.DetailActivity;
import com.vigorx.effort.R;

public class TargetAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<TargetInfo> mData;

	public TargetAdapter(Context context, List<TargetInfo> data) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mData = data;
	}

	@Override
	public int getCount() {
		if (null != mData) {
			return mData.size();
		}
		return 0;
	}

	@Override
	public TargetInfo getItem(int position) {
		if (null != mData) {
			return mData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.target_list_item, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.gridView = (GridView) convertView.findViewById(R.id.gridView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TargetInfo itemInfo = mData.get(position);
		String title = (position + 1) + "、" + itemInfo.getTitle();
		viewHolder.title.setText(title);

		String[] key = { "image" };
		int[] id = { R.id.image };
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		itemInfo.getStatus()[6] = true;
		itemInfo.getStatus()[13] = true;
		for (int i = 0; i < 28; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (itemInfo.getStatus()[i]) {
				map.put("image", R.drawable.on);
			} else {
				map.put("image", R.drawable.off);
			}
			dataList.add(map);
		}

		SimpleAdapter imageAdapter = new SimpleAdapter(mContext, dataList, R.layout.on_off_item, key, id);
		viewHolder.gridView.setAdapter(imageAdapter);
		viewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mContext, DetailActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		GridView gridView;
	}

}
