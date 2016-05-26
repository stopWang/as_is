package app.com.wj.as_is;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import app.com.wj.tool.Analytic;
import app.com.wj.tool.Analytic_interface;
import app.com.wj.tool.Public_Resources;

/**
 * Created by Administrator on 2016/3/25.
 * 首页的第一个fragment
 */
public class Fragment_left extends Fragment implements Analytic_interface {
    private View v;
    private TextView btn_ok;
    private ImageView img;
    public static final int PHOTOZOOM = 0x2222; // 相册
    public static final int PHOTOTAKE = 0x1111; // 拍照
    private String pathUrl = Environment.getExternalStorageDirectory()+"/如是/";
    private String imageName = null;  			//用于获取时间做图片名称
    private String FileNmae = null;				//用于最后上传的图片名称
    private List Delect_filenames = new ArrayList<String>();		//由于图片旋转如果生成了删图片统一加进集合关闭界面时删除
    private Uri uri = null;                     //拍照时保存的地址
    private BitmapUtils bitmapUtils = null;
    private MyDB mydb = null;
    private Cursor c = null;
    private String img_URL = null;
    private String sid = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.left_cbl,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_ok = (TextView) v.findViewById(R.id.btn_ok);
        img = (ImageView) v.findViewById(R.id.img);
        mydb = new MyDB(getActivity());
        c = mydb.query();
        if(c.moveToFirst())             //判断是否有数据  有数据则遍历
        {
            c.close();
            c = mydb.query();
            while (c.moveToNext())
            {
                sid = c.getString(1);
                img_URL = c.getString(4);
            }
        }
        else
        {
            c.close();
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.clean_table();
                Intent in = new Intent(getActivity(), Land.class);
                getActivity().startActivity(in);
                getActivity().finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        bitmapUtils = new BitmapUtils(getActivity());

        if(!img_URL.equals("")) {
            bitmapUtils.configDefaultLoadingImage(R.mipmap.logo);//设置默认图片
            bitmapUtils.display(img, img_URL);
            bitmapUtils.configDefaultLoadFailedImage(R.mipmap.logo);//设置加载失败的图片
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
                FileNmae = "/sdcard/myImage/img_logo.jpg";

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
                postPrc();
                break;
            case  PHOTOZOOM:

                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Log.e("uri", uri.toString());

                Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null, null);

                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                FileNmae = getImage(uri);
                setXzprc(readPictureDegree(FileNmae), FileNmae);
                postPrc();

                break;
            default:
                break;
        }

    }


    /**
     * 上传图片
     */
    public void postPrc()
    {
        if(FileNmae != null)
        {
            //上传图片
            //1、定义参数
            RequestParams params = new RequestParams();
            //将图片设置到参数中

            params.addBodyParameter("id", sid);

            params.addBodyParameter("upload", new File(FileNmae));

            String url = Public_Resources.URL+"servlet/item/FileUploadServlet";

            //2、上传文件
            HttpUtils httpUtils = new HttpUtils(10000);
            httpUtils.send(HttpRequest.HttpMethod.POST,
                    url,
                    params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            System.out.println("开始请求");
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            System.out.println("正在加载：共" + total + "个字节，当前：" + current);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                            System.out.println("上传成功");
                            String r = objectResponseInfo.result;
                            System.out.println(r);

                            onAnalysisJson(r);

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            System.out.println("上传失败");
                            Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(getActivity(), "上传类容不能为空", Toast.LENGTH_SHORT).show();
        }
    }




    /**
     * 旋转图片
     * @param
     */
    public void setXzprc(int jd,String filename)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(filename,opts);//根据Path读取资源图片  并且压缩
        if (jd != 0) {
            // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
            Matrix m = new Matrix();

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(jd); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    m, false);// 从新生成图片
            try {
                saveFile(bitmap);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {

        }
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
     * 用当前时间给文件命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
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




    /**
     * 获取相册图片路径
     * @param mImageCaptureUri
     * @return
     */
    private String getImage(Uri mImageCaptureUri) {

        // 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
        // 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看
        String filePath = null;
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
            filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
            String orientation = cursor.getString(cursor
                    .getColumnIndex("orientation"));// 获取旋转的角度
            cursor.close();
        }
        return filePath;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path
     *            照片路径
     * @return角度  获取从相册中选中图片的角度
     */
    public static int readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
        }
        return degree;
    }

    /**
     * bitmap转存文件
     * @param bm
     * @param
     * @throws IOException
     */
    public void saveFile(Bitmap bm) throws IOException {
        imageName = getPhotoFileName();
        FileNmae = pathUrl + imageName;
        File myCaptureFile = new File(FileNmae);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        bos.flush();
        bos.close();
        Delect_filenames.add(FileNmae);
    }

    /**
     * 关闭时删除新图片
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        for(int i = 0;i < Delect_filenames.size();i++)
        {
            File file = null;
            if(Delect_filenames.get(i)!=null)
            {
                file = new File((String) Delect_filenames.get(i));
            }
            // 判断是否因为需要旋转生成了新片
            // 判断文件是否存在
            // 判断是否是文件
            if(file.exists()&&file.isFile())
            {
                file.delete(); // 删除
            }
        }
    }

    @Override
    public void onAnalysisJson(String json) {
        if(json.indexOf("errorcode")>0) {
            Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
            Analytic texts = JSON.parseObject(json, Analytic.class);
            bitmapUtils.display(img, texts.getMessage());
            mydb.reiLogo(sid, texts.getMessage());
        }
        else
        {
            Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
        }
    }

}