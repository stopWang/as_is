package app.com.wj.presenter;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.analysis.Poetry_analysis;
import app.com.wj.application.MainApplication;
import app.com.wj.bean.ADInfo;
import app.com.wj.data_analysis.ADinfo_Analysy;
import app.com.wj.impl.HomeOneModel;
import app.com.wj.data_analysis.OnRefreshData;
import app.com.wj.view.IHomeOneView;

/**
 * Created by Administrator on 2016/7/18.
 * 首页第一项的presenter
 */
public class Home_One_Presenter implements OnRefreshData{
    private IHomeOneView view;
    private HomeOneModel model;
    private Context context;
    private  int page;
    private List<ADInfo> adList = new ArrayList<>();
    public Home_One_Presenter(IHomeOneView view) {
        this.view = view;
        this.model = new HomeOneModel();
        model.setReData(this);
        context = MainApplication.getContext();
    }
    public void pullListData(int page)
    {
        this.page = page;
        model.getListData(this.page);
    }
    public void pullAdtData()
    {
        model.getAdData();
    }

    @Override
    public void setReData(String type,Object object) {
        //区分轮播图数据和列表数据
        if(type==null)
        {
            //Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show();
        }
        else if(type!=null&&type.equals("listview")) {
            if (object != null) {
                if (object.toString().indexOf("errorcode") > 0) {
                    Poetry_analysis texts = JSON.parseObject(object.toString(), Poetry_analysis.class);
                    if (texts.getErrorcode() == 0) {
                        isFail(true);
                        view.setListViewData(texts.getData());
                    } else {
                        isFail(false);
                        Toast.makeText(context, texts.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isFail(false);
                    Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            } else {
                isFail(false);
            }
        }
        else if(type.equals("adview"))
        {
            if (object != null) {
                if (object.toString().indexOf("errorcode") > 0) {
                    ADinfo_Analysy texts = JSON.parseObject(object.toString(), ADinfo_Analysy.class);
                    if (texts.getErrorcode() == 0) {
                        for(int i = 0;i < texts.getData().size();i++) {
                            adList.add(texts.getData().get(i));
                        }
                        view.setAdvertisement((ArrayList<ADInfo>) adList);
                    } else {
                        isFail(false);
                        Toast.makeText(context, texts.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
    }
    //刷新或加载失败
    private void isFail(boolean isOk)
    {
        if(page==1)
        {
            view.Refresh(isOk);
        }
        else
        {
            view.Load(isOk);
        }
    }
}
