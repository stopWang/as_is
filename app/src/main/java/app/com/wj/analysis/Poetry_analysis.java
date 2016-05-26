package app.com.wj.analysis;

import java.util.List;

import app.com.wj.model.Poetry;
import app.com.wj.tool.Analytic;

/**
 * Created by Administrator on 2016/5/4.
 * ½âÎöÊ«´ÊÎÄÕÂ
 */
public class Poetry_analysis extends Analytic {
    private List<Poetry> data;

    public Poetry_analysis() {
    }

    public Poetry_analysis(List<Poetry> data) {

        this.data = data;
    }

    public List<Poetry> getData() {

        return data;
    }

    public void setData(List<Poetry> data) {
        this.data = data;
    }
}
