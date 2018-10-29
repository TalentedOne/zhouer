package com.mobile.liguanjian.liguanjian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.liguanjian.liguanjian.app.MyApplication;
import com.mobile.liguanjian.liguanjian.bean.City;
import com.mobile.liguanjian.liguanjian.util.ListFilter;

import java.util.ArrayList;
import java.util.List;

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private ClearEditText mClearEditText;
    private CheckBox checkBox;

    private ListView mList;
    private List<City> filterDataList;
    private List<City> cityList;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city_2);

        initViews();

        ((TextView) findViewById(R.id.title_name)).setText(getDefaultCity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void initViews() {
        mBackBtn = findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        checkBox = findViewById(R.id.cbox_regex);
        mClearEditText = (ClearEditText) findViewById(R.id.search_city);
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(""))
                    filterDataList = cityList;
                else {
                    filterDataList = new ArrayList<>();

                    filterDataList = ListFilter.filterList(checkBox.isChecked(), s.toString(), cityList,
                            new ListFilter.ToStringCallback<City>() {
                                @Override
                                public String getFilterText(City param) {
                                    return param.getProvince();
                                }
                            });

                    List<City> cityList1 = ListFilter.filterList(checkBox.isChecked(), s.toString(), cityList,
                            new ListFilter.ToStringCallback<City>() {
                                @Override
                                public String getFilterText(City param) {
                                    return param.getNumber();
                                }
                            });
                    for (City city : cityList1)
                        if (!filterDataList.contains(city))
                            filterDataList.add(city);

                    List<City> cityList2 = ListFilter.filterList(checkBox.isChecked(), s.toString(), cityList,
                            new ListFilter.ToStringCallback<City>() {
                                @Override
                                public String getFilterText(City param) {
                                    return param.getCity();
                                }
                            });
                    for (City city : cityList2)
                        if (!filterDataList.contains(city))
                            filterDataList.add(city);
                }

                myAdapter.refresh(filterDataList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();

        filterDataList = new ArrayList<>();
        filterDataList.addAll(cityList);
        myAdapter = new MyAdapter(cityList, new MyAdapter.Listener() {
            @Override
            public void onCityId(String id) {
                chooseCity(id);
            }
        });

        mList.setAdapter(myAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = filterDataList.get(position);
                chooseCity(city.getNumber());
            }
        });
    }

    private void chooseCity(String num) {
        Intent i = new Intent();
        i.putExtra("cityCode", num);
        MyApplication.setDefaultPreference(num);

        setResult(RESULT_OK, i);
        finish();
    }

    public String getDefaultCity() {
        String cityCode = MyApplication.getSharedMasterReference().getString(MyApplication.CITY, "101010100");

        for (City city : cityList)
            if (city.getNumber().equals(cityCode))
                return "当前城市：" + city.getCity();
        return "当前城市：北京";
    }
}