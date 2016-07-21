package app.com.wj.model;

import java.util.List;

import app.com.wj.bean.ADInfo;

/**
 * Created by Administrator on 2016/7/18.
 */
public interface IHomeOneModel {
    void getListData(int page);
    List<ADInfo> getAdData();
}
