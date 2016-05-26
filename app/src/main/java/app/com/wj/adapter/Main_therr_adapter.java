package app.com.wj.adapter;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.com.wj.as_is.R;

/**
 * 我乐个趣的适配器
 * @author Administrator
 *
 */
public class Main_therr_adapter extends BaseAdapter {
	private List<String> list = new ArrayList<String>();
	private Context context;


	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Main_therr_adapter(Context context) {
		this.context = context;
		list.add("作者标题");
		list.add("文言填空");
		list.add("过目不忘");
		//list.add(new Sidebar_class(R.drawable.update, "检测更新"));
	}

	class ViewHolder
	{
		private TextView tv_title;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.main_therr_item,arg2, false);

			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}

		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(arg0));
		return convertView;
	}

}
