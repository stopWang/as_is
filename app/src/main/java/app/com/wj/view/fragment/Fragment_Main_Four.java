package app.com.wj.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.com.wj.as_is.R;
import app.com.wj.item_four.Publish_Poetry;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/25.
 * 为
 */
public class Fragment_Main_Four extends Fragment implements Analytic_interface{
    String url = "";
    private View v;
    private TextView tv;
    private Button btn_ok;
    private Intent in = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.by,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) v.findViewById(R.id.tv);
        btn_ok = (Button) v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in = new Intent(getActivity(), Publish_Poetry.class);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });
        //HttpRequest();
    }


    //数据请求 使用xurls框架
    public void HttpRequest() {
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
                        Toast.makeText(getActivity(), "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAnalysisJson(String json) {
        tv.setText(json);
    }
}
