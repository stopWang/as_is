package app.com.wj.item_four;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.as_is.Poetry_Error;
import app.com.wj.as_is.R;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;
import app.com.wj.tool.SwipeBackActivity;

/**
 * Created by Administrator on 2016/5/10.
 * 发表自己的诗
 */
public class Publish_Poetry extends SwipeBackActivity implements Analytic_interface{
    private TextView tv_title;
    private TextView tv_text;
    private TextView tv_author;

    private String url = "";
    private TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_poetry);
        iniView();

    }

    private void iniView() {
        tv_title = (TextView) findViewById(R.id.title);
        tv_text = (TextView) findViewById(R.id.text);
        tv_author = (TextView) findViewById(R.id.author);
        error = (TextView) findViewById(R.id.error);
    }


    //数据请求 使用xurls框架
    public void HttpRequest() {

        //url = Public_Resources.URL+Public_Resources.Details+"id="+id;
        System.out.println(url);
        HttpUtils httpUtils = new HttpUtils(10000);
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

                        onAnalysisJson(r);

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(Publish_Poetry.this, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onAnalysisJson(String json) {

    }
}
