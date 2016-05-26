package app.com.wj.as_is;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;

import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/28.
 * 注册
 */
public class Register extends Activity implements Analytic_interface{
    private EditText userName;
    private EditText passWord;
    private EditText re_passWord;
    private String pw = "";
    private TextView btn_ok;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        iniView();
    }
    private void iniView()
    {
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        re_passWord = (EditText) findViewById(R.id.re_passWord);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().length()<5||userName.getText().toString().length()>15)
                {
                    Toast.makeText(Register.this, "账号格式不正确", Toast.LENGTH_SHORT).show();
                }
                else if(passWord.getText().toString().length()<5||passWord.getText().toString().length()>15)
                {
                    Toast.makeText(Register.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (passWord.getText().toString().equals(re_passWord.getText().toString())) {
                        btn_ok.setEnabled(false);
                        HttpRequest();
                    } else {
                        Toast.makeText(Register.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //数据请求 使用xurls框架
    public void HttpRequest()
    {
        /*密码转utf-8*/
        try {
            pw = java.net.URLEncoder.encode(passWord.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = Public_Resources.URL+Public_Resources.REGISTER+"userName="+userName.getText().toString()+"&password="+pw;
        HttpUtils httpUtils = new HttpUtils(1000);
        httpUtils.configCurrentHttpCacheExpiry(1);   //设置不缓存
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
                        btn_ok.setEnabled(true);
                        onAnalysisJson(r);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        btn_ok.setEnabled(true);
                        Toast.makeText(Register.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onAnalysisJson(String json) {
        //判断是否为服务器正常返回值
        if(json.indexOf("errorcode")>0) {
            Analytic texts = JSON.parseObject(json,Analytic.class);
            if(texts.getErrorcode()==0)
            {
                finish();
            }
            Toast.makeText(Register.this,texts.getMessage(),Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Register.this, "服务器异常", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 点击空白处关闭输入法
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}
