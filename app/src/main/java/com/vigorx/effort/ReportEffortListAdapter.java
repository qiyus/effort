package com.vigorx.effort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vigorx.effort.entity.EffortInfo;
import com.vigorx.effort.entity.PunchesInfo;

import java.util.List;

/**
 * Created by songlei on 16/7/20.
 */
public class ReportEffortListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<EffortInfo> mEfforts;

    public ReportEffortListAdapter(Context context, List<EffortInfo> efforts) {
        mInflater = LayoutInflater.from(context);
        mEfforts = efforts;
    }

    @Override
    public int getCount() {
        if (mEfforts != null) {
            return mEfforts.size();
        }
        return 0;
    }

    @Override
    public EffortInfo getItem(int position) {
        if (mEfforts != null) {
            return mEfforts.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.effort_list_report_item, null);
            viewHolder.mNumber = (TextView) convertView.findViewById(R.id.report_list_number);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.report_list_title);
            viewHolder.mBar = (ProgressBar) convertView.findViewById(R.id.report_list_progressBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EffortInfo itemInfo = mEfforts.get(position);
        String number = String.valueOf(position + 1);
        viewHolder.mNumber.setText(number);
        viewHolder.mTitle.setText(itemInfo.getTitle() + " (" + itemInfo.getStartDate() + ")");
        viewHolder.mBar.setMax(EffortInfo.EFFORT_SIZE);
        viewHolder.mBar.setProgress(getCompleteNumber(itemInfo));
        return convertView;
    }

    static class ViewHolder {
        TextView mNumber;
        TextView mTitle;
        ProgressBar mBar;
    }

    private int getCompleteNumber(EffortInfo effortInfo) {
        int number = 0;
        PunchesInfo[] infos = effortInfo.getPunches();
        for (int i = 0; i < EffortInfo.EFFORT_SIZE; i++) {
            if (1 == infos[i].getComplete()) {
                number++;
            }
        }
        return number;
    }
}
