package app.com.wj.as_is;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/3/25.
 * 平面减图
 */
public class Fragment_people extends Fragment {
    private View v;
    private ImageView img;
    private Bitmap bt;
    private Bitmap bmp1;
    private Bitmap bmp2;
    private TextView tv;
    private Paint mPaint;
    public static final int PHOTOZOOM = 0x2222; // 相册
    public static final int PHOTOTAKE = 0x1111; // 拍照
    private String pathUrl = Environment.getExternalStorageDirectory()+"/如是/";
    private String imageName = null;  			//用于获取时间做图片名称
    private String FileNmae = null;				//用于最后上传的图片名称
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.people,container,false);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        img = (ImageView) v.findViewById(R.id.img);
        tv = (TextView) v.findViewById(R.id.tv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Resources res = getResources();
        bmp1 = BitmapFactory.decodeResource(res, R.mipmap.lgx);
        bmp2 = BitmapFactory.decodeResource(res, R.mipmap.fj);
        bt = createBitmap(bmp1, bmp2);
        img.setImageBitmap(bt);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    /**
     * 图片合成
     * @param
     * @return
     */
    private Bitmap createBitmap( Bitmap src, Bitmap watermark ) {
        if( src == null ) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        int rw = w/ww;
        int rh = w/wh;
        int q1 = 1;
        int q2= 1;
        int q3 = 1;
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图
        //watermark = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图



        int[] pixels = new int[w*h];//保存所有的像素的数组，图片宽×高
        src.getPixels(pixels, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());

        Canvas cv = new Canvas( newb );

        //draw src into
        //cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src
        //draw watermark into
        //cv.drawBitmap( watermark, w - ww+5 , h - wh+5 , null );//在src的右下角画入水印
        //cv.drawBitmap( watermark, 0 , 0 , null );//在src的右下角画入水印
        /*for(int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            mPaint.setColor(clr);
            cv.drawPoint(i/w+1, i%w, mPaint);
        }*/

        tv.setText("rw\n"+rw+"rh\n"+rh+"\n"
        +"w1+\n"+w+"h1+\n"+h+"\n"
        + "w2+\n"+ww+"h2+\n"+wh);
        for(int i = 0;i<w;i++) {
            for(int l = 0;l<h;l++) {
                //int clr = pixels[i*l-1];
                int clr = src.getPixel(i,l);
                if(clr!=0) {
                    int clr2 = 0;
                    if (i < watermark.getWidth() && l < watermark.getHeight())
                    {
                        clr2  = watermark.getPixel(i, l);
                    }
                    else if(i>= watermark.getWidth())
                    {
                        clr2 = watermark.getPixel(i-watermark.getWidth(), l);
                    }
                    else if(l >= watermark.getHeight())
                    {
                        clr2 = watermark.getPixel(i, l-watermark.getHeight());
                    }
                    else if(i >= watermark.getWidth() && l >= watermark.getHeight())
                    {
                        clr2 = watermark.getPixel(i-watermark.getWidth(), l-watermark.getHeight());
                    }
                    mPaint.setColor(clr2);
                    cv.drawPoint(i, l, mPaint);

                }
                else
                {
                    mPaint.setColor(clr);
                    cv.drawPoint(i, l, mPaint);
                }
            }
        }
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG );//保存
        //store
        cv.restore();//存储
        return newb;
    }



    /**
     * 从下面弹出选择拍照和图库
     */
    private void showDialog() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.photo_choose_dialog,
                null);
        final Dialog dialog = new Dialog(getActivity(), R.style.transparentFrameWindowStyle);
        Button btn_pric = (Button) view.findViewById(R.id.btn_pric);
        Button btn_Photograph = (Button) view.findViewById(R.id.btn_Photograph);
        Button btn_no = (Button) view.findViewById(R.id.btn_no);
        /**
         * 拍照
         */
        btn_pric.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PHOTOTAKE);
            }
        });
        /**
         * 相册
         */
        btn_Photograph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                doPickPhotoFromGallery();
            }
        });

        /**
         * 取消
         */
        btn_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 打开相册
     */
    public void doPickPhotoFromGallery() {
        Toast.makeText(getActivity(), "相册获取", Toast.LENGTH_SHORT).show();
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
            startActivityForResult(i, PHOTOZOOM);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "程序出错",
                    Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode)
        {
            case PHOTOTAKE:		//照相
                /*String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    Log.v("TestFile",
                            "SD card is not avaiable/writeable right now.");
                    return;
                }
                Toast.makeText(getActivity(),"来了",1).show();
                setXzprc(readPictureDegree(FileNmae), FileNmae);
                postPrc();*/

                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                    Log.v("TestFile",
                            "SD card is not avaiable/writeable right now.");
                    return;
                }
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                //img.setImageBitmap(bitmap);
                FileOutputStream b = null;
                File files = new File("/sdcard/myImage/");
                files.mkdirs();// 创建文件夹
                FileNmae = "/sdcard/myImage/111.jpg";

                try {
                    b = new FileOutputStream(FileNmae);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bt = createBitmap(bmp2, bitmap);
                img.setImageBitmap(bitmap);
                break;
            case  PHOTOZOOM:

                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Log.e("uri", uri.toString());

                Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null, null);

                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                /*FileNmae = getImage(uri);
                setXzprc(readPictureDegree(FileNmae), FileNmae);
                postPrc();*/

                break;
            default:
                break;
        }

    }

}
