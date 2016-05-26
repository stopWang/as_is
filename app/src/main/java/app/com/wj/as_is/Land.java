package app.com.wj.as_is;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
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

import org.json.JSONObject;
import org.w3c.dom.Text;

import app.com.wj.MySql.MyDB;
import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;
import app.com.wj.tool.user1;

/**
 * Created by Administrator on 2016/3/25.
 * 登陆页面
 */
public class Land extends Activity implements Analytic_interface{
    private EditText userName;
    private EditText passWord;
    private TextView register;              //注册
    private TextView re_password;           //忘记密码
    private TextView btn_ok;
    private String username = null;
    private String passwprd = null;
    private String url = null;
    private SharedPreferences preferences;      //轻量级存储  判断是否第一次启动应用
    private MyDB mydb = null;
    private Cursor c = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land);
        iviView();

        preferences = getSharedPreferences("count",MODE_WORLD_READABLE);

        int count = preferences.getInt("count", 0);
        if(count == 0)
        {

        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入启动次数
        editor.putInt("count", ++count);
        //提交修改
        editor.commit();
        c = mydb.query();
        if(c.moveToFirst())             //判断是否有数据  有数据则遍历
        {
            c.close();
            Intent in = new Intent(this,MainActivity.class);
            startActivity(in);
            finish();
        }
        else
        {
            c.close();
        }
    }
    public void iviView()
    {
        
        mydb = new MyDB(this);
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        register = (TextView) findViewById(R.id.register);
        re_password = (TextView) findViewById(R.id.re_password);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals(""))
                {
                    Toast.makeText(Land.this,"请输入账号",Toast.LENGTH_SHORT).show();
                }
                else if(passWord.getText().toString().equals(""))
                {
                        Toast.makeText(Land.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    btn_ok.setEnabled(false);
                    HttpRequest();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Land.this,Register.class);
                startActivity(in);
                overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });


    }


    //数据请求 使用xurls框架
    public void HttpRequest() {
        username = userName.getText().toString();
        passwprd = passWord.getText().toString();
        url = Public_Resources.URL+Public_Resources.LAND+"userName="+username+"&password="+passwprd;
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
                        Toast.makeText(Land.this, "请求失败", Toast.LENGTH_SHORT).show();

                    }
                });
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

    @Override
    public void onAnalysisJson(String json) {
        //判断是否为服务器正常返回值
        if(json.indexOf("errorcode")>0) {
            user1 texts = JSON.parseObject(json, user1.class);
            if (texts.getErrorcode() == 0) {
                mydb.insertUser(texts.getData().getId(), texts.getData().getUserName(), texts.getData().getPassword(), texts.getData().getiLogo()
                        , texts.getData().getType(), texts.getData().getIdentity(), texts.getData().getIsSeal() + "", texts.getData().getName()
                        , texts.getData().getSignature(), texts.getData().getQq(), texts.getData().getWeixin(), texts.getData().getGender()
                        , texts.getData().getAge(), texts.getData().getBirthday());
                finish();
                Intent in = new Intent(Land.this, MainActivity.class);
                startActivity(in);
            }
            Toast.makeText(Land.this, texts.getMessage(), Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(Land.this, "服务器异常", Toast.LENGTH_SHORT).show();
            Toast.makeText(Land.this, "这波走起", Toast.LENGTH_SHORT).show();
            Toast.makeText(Land.this, "这波走起", Toast.LENGTH_SHORT).show();
        }
    }
}
