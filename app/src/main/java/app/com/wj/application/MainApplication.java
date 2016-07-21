package app.com.wj.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/6/22.
 * 定义全局context
 */
public class MainApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
    /**获取Context.
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
