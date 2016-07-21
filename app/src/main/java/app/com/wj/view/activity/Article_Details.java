package app.com.wj.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.io.UnsupportedEncodingException;

import app.com.wj.MySql.MyDB;
import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.as_is.R;
import app.com.wj.data_analysis.OnRefreshData;
import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;
import app.com.wj.tool.SwipeBackActivity;

/**
 * Created by Administrator on 2016/5/10.
 * 诗词文章详情页
 */
public class Article_Details extends SwipeBackActivity implements OnRefreshData{
    private TextView edit_title;
    private TextView tv_title;
    private TextView edit_text;
    private TextView tv_text;
    private TextView tv_author;
    private EditText ed_author;
    private TextView tv_zan;
    private TextView tv_comment_numb;
    private ListView list;
    private String id = "";
    private String title = "";
    private String context = "";
    private String author = "";
    private String zan = "";
    private String comment = "";
    private String url = "";
    private TextView error;
    private TextView re_context;
    private MyDB mydb = null;
    private Cursor c = null;
    private String identity_type = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);       //默认不弹出软键盘
        iniView();
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            id = (String) b.get("id");
        }
        mydb = new MyDB(this);
        c = mydb.query();
        if(c.moveToFirst())             //判断是否有数据  有数据则遍历
        {
            c = mydb.query();
            while (c.moveToNext()) {
                identity_type = c.getString(5);
            }
            c.close();
        }
        else
        {
            c.close();
        }
        url = Public_Resources.URL+Public_Resources.Details+"id="+id;
        HttpRequest("push",url);
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Article_Details.this,Poetry_Error.class);
                Bundle b = new Bundle();
                b.putString("id",id);
                in.putExtras(b);
                startActivity(in);
                overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });
    }

    private void iniView() {
        tv_title = (TextView) findViewById(R.id.title);
        edit_title = (EditText) findViewById(R.id.edit_title);
        tv_text = (TextView) findViewById(R.id.text);
        edit_text = (TextView) findViewById(R.id.edit_text);
        tv_author = (TextView) findViewById(R.id.author);
        ed_author = (EditText) findViewById(R.id.ed_author);
        tv_zan = (TextView) findViewById(R.id.zan);
        tv_comment_numb = (TextView) findViewById(R.id.comment_numb);
        list = (ListView) findViewById(R.id.list);
        error = (TextView) findViewById(R.id.error);
        re_context = (TextView) findViewById(R.id.re_context);

        re_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    title = java.net.URLEncoder.encode(edit_title.getText().toString(), "utf-8");
                    context  = java.net.URLEncoder.encode(edit_text.getText().toString(), "utf-8");
                    author  = java.net.URLEncoder.encode(ed_author.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url = Public_Resources.URL+Public_Resources.ModifyPoetry+"id="+id+"&title="+title+"&context="+context+"&author="+author;
                HttpRequest("pull",url);
            }
        });
    }

    //数据请求 使用xurls框架
    public void HttpRequest(final String type,String url) {
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
                        setReData(type, r);

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(Article_Details.this, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void setReData(String type, Object object) {
        if (type.equals("push")) {
            Poetry_analysis texts = JSON.parseObject(object.toString(), Poetry_analysis.class);
            if (identity_type != null && identity_type.equals("0")) {
                edit_title.setText(texts.getData().get(0).getPoetry_title());
                edit_text.setText(texts.getData().get(0).getPoetry_context());
                ed_author.setText(texts.getData().get(0).getPoetry_author());
                tv_title.setVisibility(View.GONE);
                tv_text.setVisibility(View.GONE);
                tv_author.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                edit_title.setVisibility(View.VISIBLE);
                edit_text.setVisibility(View.VISIBLE);
                ed_author.setVisibility(View.VISIBLE);
                re_context.setVisibility(View.VISIBLE);
            } else {
                tv_title.setText(texts.getData().get(0).getPoetry_title());
                tv_text.setText(texts.getData().get(0).getPoetry_context());
                tv_author.setText(texts.getData().get(0).getPoetry_author());
            }
            tv_zan.setText(texts.getData().get(0).getPoetry_praise_numb());
            tv_comment_numb.setText(texts.getData().get(0).getPoetry_praise_comment());
        } else if (type.equals("pull")) {
                if(object.toString().indexOf("errorcode") > 0)
                {
                    Analytic text = JSON.parseObject(object.toString(), Analytic.class);
                        Toast.makeText(Article_Details.this, text.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Article_Details.this, "服务器异常", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
