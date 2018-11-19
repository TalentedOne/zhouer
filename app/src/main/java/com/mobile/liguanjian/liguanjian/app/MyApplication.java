package com.mobile.liguanjian.liguanjian.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.mobile.liguanjian.liguanjian.bean.City;
import com.mobile.liguanjian.liguanjian.util.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    public static final String TAG = "MyAPP";
    public static final String CITY = "CITY";
    public static List<Activity> activities = new ArrayList<>();
    public static MyApplication myApplication;
    private List<City> mCityList;

    private CityDB mCityDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->Oncreate");

        myApplication = this;
        mCityDB = openCityDB();

        initCityList();
    }

    public static SharedPreferences getSharedMasterReference() {
        return getInstance().getSharedPreferences(TAG, Activity.MODE_PRIVATE);
    }

    public static void setDefaultPreference(String cityCode) {
        SharedPreferences.Editor editor = MyApplication.getSharedMasterReference().edit();
        editor.putString(CITY, cityCode);
        editor.apply();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public static Context getMContext() {
        return getInstance().getApplicationContext();
    }

    private CityDB openCityDB() {
        String path = "/data" + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator + CityDB.CITY_DB_NAME;

        File db = new File(path);

        Log.d(TAG, path);
        if (!db.exists()) {
            String pathfolder = "/data" + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName() + File.separator + "databases1" + File.separator;

            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();
        int i = 0;
        for (City city : mCityList) {
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG, cityCode + ":" + cityName);
        }
        Log.d(TAG, "i=" + i);
        return true;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public static void finishAll() {
        for (int i = activities.size() - 1; i >= 0; i--)
            activities.get(i).finish();
    }
}