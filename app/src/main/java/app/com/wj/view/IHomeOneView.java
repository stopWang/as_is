package app.com.wj.view;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.bean.ADInfo;
import app.com.wj.model.Poetry;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface IHomeOneView {
    /**
     * 设置广告
     */
    void setAdvertisement(ArrayList<ADInfo> infos);
    /**
     * 设置首页诗词
     */
    void setListViewData(List<Poetry> data);
    /**
     * 刷新
     */
    void Refresh(boolean isSuccess);
    /**
     * 刷新
     */
    void Load(boolean isSuccess);
}
