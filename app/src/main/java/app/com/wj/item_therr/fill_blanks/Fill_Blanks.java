package app.com.wj.item_therr.fill_blanks;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.com.wj.as_is.R;
import app.com.wj.tool.SwipeBackActivity;

/**
 * Created by Administrator on 2016/5/3.
 * 记忆填空
 */
public class Fill_Blanks extends SwipeBackActivity{
    private String context1 = "日暮苍山远，天寒白屋贫。\n柴门闻犬吠，风雪夜归人。";
    private String context2 = "为楼高百尺，手可摘星辰。\n不敢高声语，恐惊天上人。";
    private String answer = "";       //用户答案
    private String answer_true = "逢雪宿芙蓉山主人";       //正确答案
    private TextView text;
    private TextView title;
    private TextView btn_ok;
    private EditText Eanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_titlel);
        iniView();
        text.setText(context1);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = Eanswer.getText().toString();
                if(answer.equals(answer_true))
                {
                    Toast.makeText(Fill_Blanks.this,"恭喜您答对了",Toast.LENGTH_SHORT).show();
                    title.setText(answer);
                    Eanswer.setText("");
                }
                else
                {
                    Toast.makeText(Fill_Blanks.this,"你忘记我了吗",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iniView() {
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        btn_ok = (TextView) findViewById(R.id.btn_ok);
        Eanswer = (EditText) findViewById(R.id.answer);

    }

}
