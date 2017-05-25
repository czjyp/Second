package com.tt.czj.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tt.czj.di.component.AppComponent;
import com.tt.czj.di.component.DaggerAppComponent;
import com.tt.czj.di.modules.AppModule;
import com.tt.czj.utils.GlobalDefineValues;

import c.b.BP;
import cn.bmob.v3.Bmob;

/**
 * Created by czj on 2017/13 0013.
 */
public class MyApp extends Application {
    /**
     * The constant TAG.
     */
    public static final String TAG = "MyApp";
    private AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initBmob();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);
        initBaiDuMap();
        initImageLoader();
    }

    /**
     * Init bmob.
     */
    public void initBmob(){
        Bmob.initialize(this, GlobalDefineValues.BmobApplicationID);
        BP.init(GlobalDefineValues.BmobApplicationID);
    }

    /**
     * Get app component app component.
     *
     * @return the app component
     */
    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    /**
     * Gets application.
     *
     * @param context the context
     * @return the application
     */
    public static MyApp getApplication(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    /**
     * Init bai du map.
     */
    public void initBaiDuMap(){
        SDKInitializer.initialize(getApplicationContext());
    }

    /**
     * Init image loader.
     */
    public void initImageLoader(){
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs() //打印log信息
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }
}
