package app.com.wj.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import app.com.wj.as_is.R;
import app.com.wj.view.fragment.Fragment_Main_Four;
import app.com.wj.view.fragment.Fragment_Main_One;
import app.com.wj.view.fragment.Fragment_Main_Three;
import app.com.wj.view.fragment.Fragment_Main_Tow;
import app.com.wj.view.fragment.Fragment_left;

public class MainActivity extends SlidingFragmentActivity implements Fragment_Main_One.Closemenu{
    private int[] drawables = {
            R.drawable.matter,
            R.drawable.people,
            R.drawable.stay,
            R.drawable.by
            };
    private String[] labels = {
            "事",
            "在",
            "人",
            "为"
    };
    private Fragment[] fragments = {
            new Fragment_Main_One(),
            new Fragment_Main_Tow(),
            new Fragment_Main_Three(),
            new Fragment_Main_Four()
    };
    private static FragmentTabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // 初始化SlideMenu
        initRightMenu();
        //获得TabHost
        tabHost =
                (FragmentTabHost) this.findViewById(
                        android.R.id.tabhost);
        //关联真正的正文区
        tabHost.setup(this, getSupportFragmentManager(),
                R.id.real_content);
        //添加选项卡
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec(labels[i]);
            tab.setIndicator(getItem(
                    drawables[i]));
            tabHost.addTab(tab,
                    fragments[i].getClass(), null);
        }
        //去掉按钮之间的分割线
        tabHost.getTabWidget().setDividerDrawable(null);

    }

    private View getItem(int prc){

        /**
         * 创建一个相对布局下边对齐
         */
	   /*RelativeLayout rr = new RelativeLayout(Tabhost.this);
	   //rr.addView(v);

	   RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	   param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	   rr.addView(icon,param);*/

        View v = this.getLayoutInflater().inflate(R.layout.item,
                null);
        //找到ImageView
        ImageView img = (ImageView) v.findViewById(R.id.icon);
        img.setImageResource(prc);
        return v;
    }





    private void initRightMenu()
    {
        /**
         * 单左侧边栏
         */
		/*Fragment leftMenuFragment = new MenuLeftFragment();
		setBehindContentView(R.layout.left_menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_left_menu_frame, leftMenuFragment).commit();		//把fragment放入FrameLayout
		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);		//滑出侧边栏左边界阴影的宽度
		menu.setShadowDrawable(R.drawable.shadow);			//滑出侧边栏左边界阴影
		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//menu.setBehindWidth()
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);*/


        /**
         * 左右两边侧边栏
         */
        Fragment_left leftMenuFragment = new Fragment_left();
        setBehindContentView(R.layout.left_menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_left_menu_frame, leftMenuFragment).commit();
        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);		//滑出侧边栏左边界阴影的宽度
        menu.setShadowDrawable(R.drawable.shadow);			//滑出侧边栏左边界阴影
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		menu.setBehindWidth()
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        // menu.setBehindScrollScale(1.0f);
        menu.setSecondaryShadowDrawable(R.drawable.shadow2);
        //设置右边（二级）侧滑菜单
        menu.setSecondaryMenu(R.layout.right_menu_frame);
        //Fragment rightMenuFragment = new MenuRightFragment();
        Fragment rightMenuFragment = new Fragment_Main_Four();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_right_menu_frame, rightMenuFragment).commit();
    }




    /**
     * 个人信息
     * @param
     */
    public void showRightMenu()
    {
        getSlidingMenu().showSecondaryMenu();
    }


    /**
     * 左侧边栏
     * @param
     */
    @Override
    public void closeLeft() {
        getSlidingMenu().showMenu();
    }
}
