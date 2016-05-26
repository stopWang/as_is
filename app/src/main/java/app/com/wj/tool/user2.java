package app.com.wj.tool;

/**
 * Created by Administrator on 2016/3/31.
 */
public class user2 {
    private String id;
    private String userName;
    private String password;
    private String iLogo;
    private String type;
    private String identity;
    private int isSeal;
    private String name;
    private String signature;
    private String qq;
    private String weixin;
    private String gender;
    private String age;
    private String birthday;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getiLogo() {
        return iLogo;
    }
    public void setiLogo(String iLogo) {
        this.iLogo = iLogo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getIdentity() {
        return identity;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    public int getIsSeal() {
        return isSeal;
    }
    public void setIsSeal(int isSeal) {
        this.isSeal = isSeal;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getWeixin() {
        return weixin;
    }
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public user2(String id, String userName, String password, String iLogo,
                String type, String identity, int isSeal, String name,
                String signature, String qq, String weixin, String gender,
                String age, String birthday) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.iLogo = iLogo;
        this.type = type;
        this.identity = identity;
        this.isSeal = isSeal;
        this.name = name;
        this.signature = signature;
        this.qq = qq;
        this.weixin = weixin;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
    }
    public user2() {
        super();
    }
}
