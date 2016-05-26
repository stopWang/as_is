package app.com.wj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.as_is.R;
import app.com.wj.model.Lvshi_item;
import app.com.wj.model.Poetry;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Lvshi_adapter extends BaseAdapter{
    private List<Poetry> list = new ArrayList<>();
    private Context context;

    public List<Poetry> getList() {
        return list;
    }

    public void setList(List<Poetry> list) {
        this.list = list;
    }

    public Lvshi_adapter(Context context) {
        this.context = context;

    }

    class ViewHolder
    {
        private TextView title;
        private TextView text;
        private TextView author;
        private TextView zan;
        private TextView comment_numb;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(v==null) {
            viewHolder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.lvshi_item, parent, false);
            viewHolder.title = (TextView) v.findViewById(R.id.title);
            viewHolder.text = (TextView) v.findViewById(R.id.text);
            viewHolder.author = (TextView) v.findViewById(R.id.author);
            viewHolder.zan = (TextView) v.findViewById(R.id.zan);
            viewHolder.comment_numb = (TextView) v.findViewById(R.id.comment_numb);
            v.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.title.setText(list.get(position).getPoetry_title());
        viewHolder.text.setText(list.get(position).getPoetry_context());
        viewHolder.author.setText(list.get(position).getPoetry_author());
        viewHolder.zan.setText(list.get(position).getPoetry_praise_numb());
        viewHolder.comment_numb.setText(list.get(position).getPoetry_praise_comment());
        return v;
    }
}
