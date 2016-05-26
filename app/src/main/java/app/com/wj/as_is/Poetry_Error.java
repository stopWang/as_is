package app.com.wj.as_is;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import app.com.wj.MySql.MyDB;
import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;
import app.com.wj.tool.SwipeBackActivity;

/**
 * Created by Administrator on 2016/5/10.
 * 报错
 */
public class Poetry_Error extends SwipeBackActivity implements Analytic_interface{

    private String poetry_id = "";
    private String user_id = "";
    private String context = "";
    private String url = "";
    private MyDB mydb = null;
    private Cursor c = null;
    private EditText text;
    private Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poetry_error);
        iniView();
        mydb = new MyDB(this);
        c = mydb.query();
        if(c.moveToFirst())             //判断是否有数据  有数据则遍历
        {
            c.close();
            c = mydb.query();
            while (c.moveToNext())
            {
                user_id = c.getString(1);
            }
        }
        else
        {
            c.close();
        }

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            poetry_id = (String) b.get("id");
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpRequest();
            }
        });


        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            //文本框改变时调用
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() == 0) {
                    text.setGravity(Gravity.CENTER);
                } else {
                    text.setGravity(Gravity.TOP | Gravity.LEFT);
                }
            }
        });


    }

    private void iniView() {
        text = (EditText) findViewById(R.id.text);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }


    //数据请求 使用xurls框架
    public void HttpRequest() {
        context = text.getText().toString();
        url = Public_Resources.URL+Public_Resources.ReportError+"poetry_id="+poetry_id+"&error_context="+context+"&user_id="+user_id;
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
                        Toast.makeText(Poetry_Error.this, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAnalysisJson(String json) {
        Analytic texts = JSON.parseObject(json, Analytic.class);
        Toast.makeText(Poetry_Error.this,texts.getMessage(),Toast.LENGTH_SHORT).show();
        if(texts.getErrorcode()==0)
        {
            finish();
            overridePendingTransition(0,R.anim.base_slide_right_out);
        }
    }
}
