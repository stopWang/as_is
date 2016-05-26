package app.com.wj.tool;

/**
 * Created by Administrator on 2016/3/31.
 */
public class user1 extends Analytic{
    private user2 data;

    public user1() {
    }

    public user2 getData() {
        return data;
    }

    public void setData(user2 data) {
        this.data = data;
    }

    public user1(user2 data) {

        this.data = data;
    }
}
