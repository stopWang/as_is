package app.com.wj.view.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import app.com.wj.MySql.MyDB;
import app.com.wj.as_is.R;
import app.com.wj.item_one.Fragment_Lvshi;

/**
 * Created by Administrator on 2016/3/25.
 * 首页的第二个fragment
 */
public class Fragment_Main_Tow extends Fragment implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private View v;
    private ViewPager vp;
    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private ImageView line;
    private TextView[] tv = new TextView[5];
    private int width;
    private int previousPosition;
    private BitmapUtils bitmapUtils = null;
    private ImageView img;
    private Fragment_Main_One.Closemenu closeleftmenu;
    private MyDB mydb = null;
    private Cursor c = null;
    private String img_URL = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_tow,container,false);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniView();
        vp.setAdapter(mAdapter);
        for(int i =0;i < tv.length;i++)
        {
            tv[i].setOnClickListener(this);
        }
        img.setOnClickListener(this);
        vp.setOnPageChangeListener(this);

        mydb = new MyDB(getActivity());
        c = mydb.query();
        if(c.moveToFirst())             //判断是否有数据  有数据则遍历
        {
            c.close();
            c = mydb.query();
            while (c.moveToNext())
            {
                img_URL = c.getString(4);
            }
        }
        else
        {
            c.close();
        }

        if(!img_URL.equals("")) {
            bitmapUtils.configDefaultLoadingImage(R.mipmap.logo);//设置默认图片
            bitmapUtils.display(img, img_URL);
            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.logo);//设置加载失败的图片
        }
    }



    private void iniView()
    {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;//宽度
        vp = (ViewPager) v.findViewById(R.id.vp);
        line = (ImageView) v.findViewById(R.id.line);
        tv[0] = (TextView) v.findViewById(R.id.tv1);
        tv[1] = (TextView) v.findViewById(R.id.tv2);
        tv[2] = (TextView) v.findViewById(R.id.tv3);
        tv[3] = (TextView) v.findViewById(R.id.tv4);
        tv[4] = (TextView) v.findViewById(R.id.tv5);
        img = (ImageView) v.findViewById(R.id.img);
        bitmapUtils = new BitmapUtils(getActivity());
        mFragments.clear();
        mFragments.add(new Fragment_Lvshi());
        mFragments.add(new Fragment_Lvshi());
        mFragments.add(new Fragment_Lvshi());
        mFragments.add(new Fragment_Lvshi());
        mFragments.add(new Fragment_Lvshi());
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                vp.setCurrentItem(0);
                break;
            case R.id.tv2:
                vp.setCurrentItem(1);
                break;
            case R.id.tv3:
                vp.setCurrentItem(2);
                break;
            case R.id.tv4:
                vp.setCurrentItem(3);
                break;
            case R.id.tv5:
                vp.setCurrentItem(4);
                break;
            case R.id.img:
                closeleftmenu.closeLeft();
                break;
            default:
                break;
        }
    }


    /**
     * 关联时调用  fragment关联到activity时被调用
     */
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            closeleftmenu=(Fragment_Main_One.Closemenu) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i = 0; i < tv.length; i ++){
            tv[i].setTextColor(Color.parseColor("#CCCCCC"));
        }
        if(position<5) {
            tv[position].setTextColor(Color.parseColor("#d8251f"));
        }

        TranslateAnimation ani = new TranslateAnimation(
                previousPosition * width/5,
                position * width/5, 0, 0);
        ani.setDuration(300);
        ani.setFillAfter(true);
        ani.setInterpolator(new AccelerateDecelerateInterpolator());

        //播放动画
        line.startAnimation(ani);
        //将这一次的位置保存起来，作为下一次移动的起始位置
        previousPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
