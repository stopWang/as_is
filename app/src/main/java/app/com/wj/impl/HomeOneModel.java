package app.com.wj.impl;

import android.content.Context;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.application.MainApplication;
import app.com.wj.bean.ADInfo;
import app.com.wj.model.IHomeOneModel;
import app.com.wj.data_analysis.OnRefreshData;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/7/18.
 */
public class HomeOneModel implements IHomeOneModel {
    private Context context = MainApplication.getContext();
    private OnRefreshData reData;
    private int page;
    private List<ADInfo> list = new ArrayList<ADInfo>();
    public OnRefreshData getReData() {
        return reData;
    }

    public void setReData(OnRefreshData reData) {
        this.reData = reData;
    }

    @Override
    public void getListData(int page) {
        this.page = page;
        HttpRequest("listview",Public_Resources.URL+Public_Resources.LVSHI+"page="+page);
    }

    @Override
    public List<ADInfo> getAdData() {
        HttpRequest("adview",Public_Resources.URL+Public_Resources.CarouselFigure);
        //list.add(new ADInfo("id","http://192.168.1.222:8080/mytest/upload/img_2016-07-18-16-44-46-718.jpg","内容","类型"));
        //list.add(new ADInfo("id","http://192.168.1.222:8080/mytest/upload/img_2016-07-18-16-44-15-764.jpg","内容","类型"));
        //list.add(new ADInfo("id","http://192.168.1.222:8080/mytest/upload/img_2016-07-18-16-43-49-313.jpg","内容","类型"));
        //list.add(new ADInfo("id","http://192.168.1.222:8080/mytest/upload/img_2016-07-18-16-42-46-138.jpg","内容","类型"));
        //list.add(new ADInfo("id","http://192.168.1.222:8080/mytest/upload/img_2016-07-18-16-41-41-670.png","内容","类型"));
        return list;
    }

    //数据请求 使用xurls框架
    public void HttpRequest(final String type,String url) {

        System.out.println(url);
        HttpUtils httpUtils = new HttpUtils(10000);
        httpUtils.configCurrentHttpCacheExpiry(10*60*1000);   //设置缓存10分钟
        httpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        System.out.println("开始请求");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        System.out.println("正在加载：共" + total + "个字节，当前：" + current);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                        String r = stringResponseInfo.result;
                        System.out.println(r);
                        reData.setReData(type,r);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(context, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                        reData.setReData(null,null);
                    }
                });
    }

}
