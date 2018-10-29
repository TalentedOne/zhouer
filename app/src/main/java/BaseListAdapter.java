package com.mobile.liguanjian.liguanjian;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mobile.liguanjian.liguanjian.app.MyApplication;

import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> dataList;

    public BaseListAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    public void refresh(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMediumView(position, convertView, parent);
    }

    protected View findV(View view, int id) {
        return view.findViewById(id);
    }

    protected TextView findTv(View view, int id) {
        return (TextView) view.findViewById(id);
    }

    protected EditText findEdit(View view, int id) {
        return (EditText) view.findViewById(id);
    }

    protected Button findBtn(View view, int id) {
        return (Button) view.findViewById(id);
    }

    protected ImageView findImg(View view, int id) {
        return (ImageView) view.findViewById(id);
    }

    protected ImageButton findIbtn(View view, int id) {
        return (ImageButton) view.findViewById(id);
    }

    protected ToggleButton findTog(View view, int id) {
        return (ToggleButton) view.findViewById(id);
    }

    protected ViewGroup findVg(View view, int id) {
        return (ViewGroup) view.findViewById(id);
    }

    protected LinearLayout findLin(View view, int id) {
        return (LinearLayout) view.findViewById(id);
    }

    protected View inflate(int layoutId) {
        return LayoutInflater.from(MyApplication.getMContext()).inflate(layoutId, null);
    }

    protected Resources getResources() {
        return MyApplication.getMContext().getResources();
    }

    protected String getString(int id) {
        return MyApplication.getMContext().getResources().getString(id);
    }

    public abstract View getMediumView(int position, View convertView, ViewGroup parent);

    protected class BaseListAdapterListener {
        protected int position;

        protected BaseListAdapterListener(int position) {
            this.position = position;
        }
    }
}
