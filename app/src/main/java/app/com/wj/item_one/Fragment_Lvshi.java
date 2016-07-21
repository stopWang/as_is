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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xb.pulltorefresh.pullableview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.adapter.Lvshi_adapter;
import app.com.wj.view.activity.Article_Details;
import app.com.wj.as_is.R;
import app.com.wj.bean.ADInfo;
import app.com.wj.model.Poetry;
import app.com.wj.presenter.Home_One_Presenter;
import app.com.wj.tool.ImageCycleView;
import app.com.wj.view.IHomeOneView;

/**
 * Created by Administrator on 2016/3/25.
 * 律诗
 */
public class Fragment_Lvshi extends Fragment implements ImageCycleView.ImageCycleViewListener,IHomeOneView{
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
    private ImageCycleView adView = null;               //轮播图
    private BitmapUtils bitmapUtils = null;
    private Home_One_Presenter presenter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_one_lvshi,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniView();
        presenter.pullAdtData();
        presenter.pullListData(page);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                page = 1;
                presenter.pullListData(page);
                mHandler = null;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Refresh_SUCCEED:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);     //通知刷新成功
                                break;
                            case Refresh_FAIL:
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);        //通知刷新失败
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
                presenter.pullListData(page);
                mHandler = null;
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        switch (msg.what) {
                            case LoadMore_SUCCEED:
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);    //通知加载成功
                                break;
                            case LoadMore_FAIL:
                                if(page > 1 )
                                {
                                    page --;
                                }
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);       //通知加载失败
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
        adView = (ImageCycleView) v.findViewById(R.id.ad_view);
        bitmapUtils = new BitmapUtils(getActivity());
        presenter = new Home_One_Presenter(this);
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
    //轮播图显示
    @Override
    public void displayImage(String imageURL, ImageView imageView) {
        bitmapUtils.display(imageView, imageURL);
    }
    //轮播图点击事件
    @Override
    public void onImageClick(ADInfo info, int postion, View imageView) {
        if(info.getContent()!=null&&info.getContent().length()>0) {
            Toast.makeText(getActivity(),"点击了第"+postion+"项",Toast.LENGTH_LONG).show();
        }
        else
        {

        }
    }
    //轮播图数据
    @Override
    public void setAdvertisement(ArrayList<ADInfo> infos) {
        if(infos!=null&&infos.size()>0) {
            adView.setVisibility(View.VISIBLE);
            adView.setImageResources(infos, Fragment_Lvshi.this);
        }
        else
        {
            adView.setVisibility(View.GONE);
        }
    }
    //listview数据填充
    @Override
    public void setListViewData(List<Poetry> data) {

        if(page == 1)
        {
            adapter.getList().clear();
        }
            for(int i = 0;i<data.size();i++) {
                adapter.getList().add(data.get(i));
            }
            adapter.notifyDataSetChanged();
    }
    //是否刷新成功
    @Override
    public void Refresh(boolean isSuccess) {
        if(isSuccess)
        {
            sendMessage(Refresh_SUCCEED);
        }
        else
        {
            sendMessage(Refresh_FAIL);
        }
    }
    //是否加载成功
    @Override
    public void Load(boolean isSuccess) {
        if(isSuccess)
        {
            sendMessage(LoadMore_SUCCEED);
        }
        else
        {
            sendMessage(LoadMore_FAIL);
        }
    }
}
