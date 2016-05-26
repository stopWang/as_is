package app.com.wj.MySql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyDB extends SQLiteOpenHelper {
    private String TABLE_USER_NAME = Public_Resources.TABLE_USER_NAME;
    private String CREATE_BOOK = "create table "+TABLE_USER_NAME+"(_id integer primary key autoincrement, sid,userName,passWord,iLogo,type,identity,isSeal,name,signature,qq,weixin,gender,age,birthday);";      //创建用户表
    private String CREATE_TEMP_BOOK = "alter table "+TABLE_USER_NAME+" rename to _temp_user_table";  		//把旧表改名成临时表
    private String INSERT_DATA = "insert into "+TABLE_USER_NAME+" select *,'' from _temp_user_table";  //把旧表数据存放到新表中
    private String DROP_BOOK = "drop table _temp_user_table";			//删除临时表
    private String sql;
    public MyDB(Context context) {
        super(context, Public_Resources.DB_NAME, null, Public_Resources.DATABASE_VERSION);          //创建数据库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }
    /**
     * 数据库版本号改变时更新表
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion)
        {
            db.execSQL(CREATE_TEMP_BOOK);
            db.execSQL(CREATE_BOOK);
            db.execSQL(INSERT_DATA);
            db.execSQL(DROP_BOOK);
        }
    }
    /**
     *插入数据
     */
    public void insertUser(String sid,String userName,String passWord,String iLogo,String type,String identity,String isSeal,String name,String signature,String qq,String weixin,String gender,String age,String birthday){
        sql = "insert into "+TABLE_USER_NAME+"(sid,userName,passWord,iLogo,type,identity,isSeal,name,signature,qq,weixin,gender,age,birthday) " +
                "values('" + sid + "', '"
                + userName + "', '" + passWord + "','"+iLogo+"','"+type+"','"+identity+"','"+isSeal+"','"+name+"','"+signature+"','"+qq+"','"+weixin+"','"+gender+"','"+age+"','"+birthday+"')";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }
    /**
     * 查询
     * @return
     */
    public Cursor query(){
        String sql = "select _id,sid,userName,passWord,iLogo,type,identity,isSeal,name,signature,qq,weixin,gender,age,birthday from "+TABLE_USER_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    /**
     * 删除表中数据
     */
    public void clean_table()
    {
        sql = "delete from "+TABLE_USER_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }
    /**
     * 修改头像
     */
    public void reiLogo(String id,String iLodo)
    {
            String sql = "UPDATE '"+TABLE_USER_NAME+"' set iLogo = '"+iLodo+"' where sid = '"+id+"'";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);
            db.close();
    }
}
