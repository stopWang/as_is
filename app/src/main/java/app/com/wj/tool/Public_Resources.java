package app.com.wj.tool;

/**
 * Created by Administrator on 2016/3/28.
 * 公共资源
 */
public class Public_Resources {
    /**
     * 服务器地址
     */
    public static String URL = "http://192.168.1.222:8080/mytest/";
    /**
     * 注册接口
     */
    public final static String REGISTER = "register.jsp?";
    /**
     * 登陆接口
     */
    public final static String LAND = "land.jsp?";
    /**
     * 古诗词接口
     */
    public final static String POETRY = "servlet/item/GetPoetryServlet?";
    /**
     * 获取绝句列表
     */
    public final static String Rhyme = "servlet/item/GetRhyme?";
    /**
     * 获取律诗列表
     */
    public final static String LVSHI = "servlet/item/GetLvshi?";
    /**
     * 获取词列表
     */
    public final static String CI = "servlet/item/GetCi?";
    /**
     * 获取曲列表
     */
    public final static String QU = "servlet/item/GetQu?";
    /**
     * 获取词列表
     */
    public final static String GU = "servlet/item/GetGu?";

    /**
     * 获取详情
     */
    public final static String Details = "servlet/item/GetDetails?";

    /**
     * 诗词报错接口
     */
    public final static String ReportError = "servlet/item/ReportError?";

    /**
     * 数据库名称
     */
    public final static String DB_NAME = "My_DB";
    /**
     * 用户表名
     */
    public final static String TABLE_USER_NAME = "userName";
    /**
     * 数据库版本
     */
    public final static int DATABASE_VERSION = 1;
}
