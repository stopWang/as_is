package app.com.wj.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.wj.MySql.MyDB;
import app.com.wj.adapter.Main_therr_adapter;
import app.com.wj.as_is.R;
import app.com.wj.item_therr.Author_Title;
import app.com.wj.item_therr.fill_blanks.Fill_Blanks;
import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/25.
 * 首页的第三个fragment
 */
public class Fragment_Main_Three extends Fragment {
    private View v;
    private ListView list;
    private Main_therr_adapter adapter;
    private Intent in;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_therr,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniView();
    }

    private void iniView() {
        list = (ListView) v.findViewById(R.id.list);
        adapter = new Main_therr_adapter(getActivity());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    in = new Intent(getActivity(), Author_Title.class);
                    startActivity(in);
                    getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                }
            }
        });
    }

}
