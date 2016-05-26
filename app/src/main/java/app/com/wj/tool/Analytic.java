package app.com.wj.tool;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Analytic {
    private int errorcode;
    private String message;

    public Analytic() {
        super();
    }

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
}
