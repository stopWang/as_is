package app.com.wj.data_analysis;

import java.util.List;

import app.com.wj.bean.ADInfo;

/**
 * Created by Administrator on 2016/7/19.
 * ½âÎöÂÖ²¥Í¼
 */
public class ADinfo_Analysy extends Analysy_Return{
    private List<ADInfo> data;

    public ADinfo_Analysy(int errorcode, String message, List<ADInfo> data) {
        super(errorcode, message);
        this.data = data;
    }

    public List<ADInfo> getData() {
        return data;
    }

    public void setData(List<ADInfo> data) {
        this.data = data;
    }

    public ADinfo_Analysy() {

    }
}
