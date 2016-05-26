package app.com.wj.item_therr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.as_is.R;
import app.com.wj.model.Poetry;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;
import app.com.wj.tool.SwipeBackActivity;
import app.com.wj.tool.user1;

/**
 * Created by Administrator on 2016/5/3.
 * 作者标题
 */
public class Author_Title extends SwipeBackActivity implements Analytic_interface {
    private List<Poetry> contexts = new ArrayList<>();      //诗词文章集合
    private String answer = "";         //用户答案
    private String answer_true = "";    //正确答案
    private TextView text;              //内容
    private TextView title;             //标题
    private TextView author;            //作者
    private TextView type;              //猜标题或者作者
    private TextView btn_ok;            //提交答案按钮
    private EditText Eanswer;           //用户输入答案区域
    private String url = "";            //请求地址
    private int size = 0;               //表示当前是第几题
    private List<Integer> numb = new ArrayList<>();          //题目的随机顺序
    private int type_random;            //生成一个随机数随机生成猜题类型
    private int sort_random;            //排序随机数
    private boolean isCheat = false;    //查看答案为作弊
    private TextView tv_cheat;          //查看答案
    private int fenshu = 0;             //得分数
    private Random ra =new Random();        //生成随机数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_titlel);
        iniView();
        HttpRequest();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = Eanswer.getText().toString();
                if (answer.equals(answer_true)) {
                    btn_ok.setEnabled(false);
                    if(isCheat==false) {
                        Toast.makeText(Author_Title.this, "恭喜您答对了", Toast.LENGTH_SHORT).show();
                        fenshu++;
                    }
                    else
                    {
                        Toast.makeText(Author_Title.this, "记住哦", Toast.LENGTH_SHORT).show();
                        isCheat = false;
                    }

                    if (type_random == 0) {
                        title.setText(answer);
                    } else if (type_random == 1) {
                        author.setText(answer);
                    }
                    Eanswer.setText("");
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            getTimu();
                        }
                    }, 2000);

                } else {
                    Toast.makeText(Author_Title.this, "你忘记我了吗", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eanswer.setText(answer_true);
                isCheat = true;
            }
        });

    }

    private void iniView() {
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        author = (TextView) findViewById(R.id.author);
        type = (TextView) findViewById(R.id.type);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
        Eanswer = (EditText) findViewById(R.id.answer);
        tv_cheat = (TextView) findViewById(R.id.tv_cheat);
    }


    //数据请求 使用xurls框架
    public void HttpRequest() {

        url = Public_Resources.URL+Public_Resources.POETRY;
        HttpUtils httpUtils = new HttpUtils(1000);
        httpUtils.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        System.out.println("开始请求");
                        btn_ok.setEnabled(false);
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
                        Toast.makeText(Author_Title.this, "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(0,R.anim.base_slide_lower_out);
                    }
                });
    }


    @Override
    public void onAnalysisJson(String json) {

        if(json.indexOf("errorcode")>0) {

            Poetry_analysis texts = JSON.parseObject(json, Poetry_analysis.class);
            if(texts.getErrorcode() == 0)
            {
                contexts = texts.getData();
                for(int i = 0;i<contexts.size();i++) {
                    numb.add(i);
                }

                for(int i = 0;i < numb.size();i++)
                {
                    sort_random = ra.nextInt(numb.size()-i)+i;
                    int t = numb.get(i);
                    int l = numb.get(sort_random);
                    numb.remove(i);
                    numb.add(i, l);
                    numb.remove(sort_random);
                    numb.add(sort_random,t);
                }
                getTimu();
            }
            else
            {
                Toast.makeText(getApplication(), texts.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, R.anim.base_slide_lower_out);
            }
        }
        else
        {
            Toast.makeText(Author_Title.this, "服务器异常", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_lower_out);
        }

    }

    private void getTimu()
    {


        type_random = (int) (Math.random()*2);
        btn_ok.setEnabled(true);
        if(size!=contexts.size()) {

                //显示题目
                if (type_random == 0) {
                    title.setText("_________");
                    text.setText(contexts.get(numb.get(size)).getPoetry_context());
                    author.setText(contexts.get(numb.get(size)).getPoetry_author());
                    type.setText("标题记得吗：");
                    answer_true = contexts.get(numb.get(size)).getPoetry_title();
                } else if (type_random == 1) {
                    title.setText(contexts.get(numb.get(size)).getPoetry_title());
                    text.setText(contexts.get(numb.get(size)).getPoetry_context());
                    author.setText("_________");
                    type.setText("作者记得吗：");
                    answer_true = contexts.get(numb.get(size)).getPoetry_author();
                }
                size++;
        }
        else
        {
            Toast.makeText(Author_Title.this,fenshu+"分",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
