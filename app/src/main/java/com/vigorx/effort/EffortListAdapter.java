package com.vigorx.effort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vigorx.effort.entity.EffortInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffortListAdapter extends BaseAdapter {
    private static final String WIDGET_IMAGE = "image";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<EffortInfo> mData;

    public EffortListAdapter(Context context, List<EffortInfo> data) {
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
    public EffortInfo getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.effort_list_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.gridView = (GridView) convertView.findViewById(R.id.gridView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EffortInfo itemInfo = mData.get(position);
        String title = (position + 1) + mContext.getResources().getString(R.string.period) + itemInfo.getTitle();
        viewHolder.title.setText(title);

        String[] key = {WIDGET_IMAGE};
        int[] id = {R.id.image};
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < EffortInfo.EFFORT_SIZE; i++) {
            Map<String, Object> map = new HashMap<>();
            if (1 == itemInfo.getPunches()[i].getComplete()) {
                map.put(WIDGET_IMAGE, R.drawable.on);
            } else {
                map.put(WIDGET_IMAGE, R.drawable.off);
            }
            dataList.add(map);
        }

        SimpleAdapter imageAdapter = new SimpleAdapter(mContext, dataList, R.layout.on_off_item, key, id);
        viewHolder.gridView.setAdapter(imageAdapter);
        viewHolder.gridView.setClickable(false);
        viewHolder.gridView.setPressed(false);
        viewHolder.gridView.setEnabled(false);
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        GridView gridView;
    }

    public void notifyGridViewDataChanged() {

    }

}
