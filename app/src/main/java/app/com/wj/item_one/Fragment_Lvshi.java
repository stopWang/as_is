package app.com.wj.item_one;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xb.pulltorefresh.pullableview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import app.com.wj.adapter.Lvshi_adapter;
import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.as_is.Article_Details;
import app.com.wj.as_is.R;
import app.com.wj.model.Lvshi_item;
import app.com.wj.model.Poetry;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/25.
 * 律诗
 */
public class Fragment_Lvshi extends Fragment implements Analytic_interface{
    private View v;
    private ListView list;
    private Lvshi_adapter adapter;
    private String url = "";            //请求地址
    private List<Poetry> contexts = new ArrayList<>();      //诗词集合
    private int page = 1;
    private PullToRefreshLayout ptrl;
    private final int Refresh_SUCCEED = 0x1111;			//下拉刷新成功
    private final int Refresh_FAIL = 0x1112;			//下拉刷新失败
    private final int LoadMore_SUCCEED = 0x1113;		//上拉加载成功
    private final int LoadMore_FAIL = 0x1114;			//上拉加载失败
    private Handler mHandler = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.listview,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniView();
        HttpRequest();

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                page = 1;
                HttpRequest();
                mHandler = null;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Refresh_SUCCEED:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case Refresh_FAIL:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                break;
                            default:
                                break;
                        }
                    }
                };
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 上拉加载操作
                page++;
                HttpRequest();
                mHandler = null;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        switch (msg.what) {
                            case LoadMore_SUCCEED:
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case LoadMore_FAIL:
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(), Article_Details.class);
                Bundle b = new Bundle();
                b.putString("id",adapter.getList().get(position).getPoetry_id());
                in.putExtras(b);
                startActivity(in);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });


    }

    private void iniView() {
        page = 1;
        list = (ListView) v.findViewById(R.id.list);
        adapter = new Lvshi_adapter(getActivity());
        list.setAdapter(adapter);
        ptrl = (PullToRefreshLayout) v.findViewById(R.id.refresh_view);
    }

    //数据请求 使用xurls框架
    public void HttpRequest() {

        url = Public_Resources.URL+Public_Resources.LVSHI+"page="+page;
        System.out.println(url);
        HttpUtils httpUtils = new HttpUtils(10000);
        httpUtils.configCurrentHttpCacheExpiry(10*60*1000);   //设置缓存10分钟
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

                        if(page == 1)
                        {
                            sendMessage(Refresh_SUCCEED);
                        }
                        else
                        {
                            sendMessage(LoadMore_SUCCEED);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(getActivity(), "请求失败请检查网络", Toast.LENGTH_SHORT).show();
                        if(page == 1)
                        {
                            sendMessage(Refresh_FAIL);
                        }
                        else
                        {
                            if(page > 1 )
                            {
                                page --;
                            }
                            sendMessage(LoadMore_FAIL);
                        }
                    }
                });
    }


    @Override
    public void onAnalysisJson(String json) {
        if(json.indexOf("errorcode")>0) {

            if(page == 1)
            {
                adapter.getList().clear();
            }

            Poetry_analysis texts = JSON.parseObject(json, Poetry_analysis.class);
            if(texts.getErrorcode() == 0)
            {
                contexts = texts.getData();
                for(int i = 0;i<contexts.size();i++) {
                    adapter.getList().add(contexts.get(i));
                }
                adapter.notifyDataSetChanged();
            }
            else
            {
                if(page > 1 )
                {
                    page --;
                }
                Toast.makeText(getActivity(), texts.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(page > 1 )
            {
                page --;
            }
            Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 刷新或加载完向handler发送消息
     */
    public void sendMessage(int id){
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }

}
