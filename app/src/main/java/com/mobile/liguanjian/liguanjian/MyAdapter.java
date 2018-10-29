package com.mobile.liguanjian.liguanjian;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.liguanjian.liguanjian.bean.City;

import java.util.List;

public class MyAdapter extends com.mobile.liguanjian.liguanjian.BaseListAdapter<City> {

    Listener listener;

    public MyAdapter(List<City> dataList, Listener listener) {
        super(dataList);
        this.listener = listener;
    }

    @Override
    public View getMediumView(final int position, View convertView, ViewGroup parent) {
        MediumViewHolder holder;

        if (convertView == null) {
            holder = new MediumViewHolder();
            convertView = inflate(R.layout.adapter);
            holder.tv_name = convertView.findViewById(R.id.name);
            holder.num = convertView.findViewById(R.id.num);
            convertView.setTag(holder);
        } else
            holder = (MediumViewHolder) convertView.getTag();

        City city = getItem(position);
        holder.tv_name.setText(city.getProvince() + "\t" + city.getCity());
        holder.num.setText(getItem(position).getNumber());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCityId(getItem(position).getNumber());
            }
        };

        holder.tv_name.setOnClickListener(onClickListener);
        holder.num.setOnClickListener(onClickListener);

        return convertView;
    }

    private final class MediumViewHolder {
        public TextView tv_name;
        public TextView num;
    }

    public interface Listener {
        void onCityId(String id);
    }
}
