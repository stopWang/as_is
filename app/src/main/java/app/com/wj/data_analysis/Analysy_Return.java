package app.com.wj.data_analysis;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Analysy_Return {
    private int errorcode;
    private String message;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Analysy_Return() {

    }

    public Analysy_Return(int errorcode, String message) {

        this.errorcode = errorcode;
        this.message = message;
    }
}
