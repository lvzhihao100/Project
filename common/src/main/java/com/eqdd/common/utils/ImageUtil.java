package com.eqdd.common.utils;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.eqdd.common.R;
import com.eqdd.common.base.App;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vzhihao on 2016/7/29.
 */
public class ImageUtil {

    private Bitmap bitmap;
    private ImageView imageView;
    private static Animator mCurrentAnimator;
    private static int mShortAnimationDuration = App.INSTANCE.getResources().getInteger(
            android.R.integer.config_shortAnimTime);

    public static void setCircleImage(String uri, ImageView imageView) {
        if (TextUtils.isEmpty(uri) || uri.equals("0") || uri.equals("无")) {
            setImage(R.mipmap.ic_launcher, imageView);
        } else {
            Picasso.with(App.INSTANCE)
                    .load(uri.replace("\\", "/"))//加载中显示图片
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)//错误显示图片
                    .transform(new CircleTransform())
                    .into(imageView);
        }
    }

    public static void setCircleImage(String uri, ImageView imageView, int defout) {
        if (TextUtils.isEmpty(uri) || uri.equals("0") || uri.equals("无")) {
            setImage(defout, imageView);
        } else {
            Picasso.with(App.INSTANCE)
                    .load(uri.replace("\\", "/"))//加载中显示图片
                    .placeholder(defout)
                    .error(defout)//错误显示图片
                    .transform(new CircleTransform())
                    .into(imageView);
        }
    }

    public static void setCircleImage(int resId, ImageView imageView) {
        Picasso.with(App.INSTANCE)
                .load(resId)//加载中显示图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)//错误显示图片
                .transform(new CircleTransform())
                .into(imageView);

    }

    public static void setImage(String uri, ImageView imageView) {
        Picasso.with(App.INSTANCE)
                .load(uri.replace("\\", "/"))//加载中显示图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)//错误显示图片
                .into(imageView);
    }

    public static void setImage(int resId, ImageView imageView) {
        Picasso.with(App.INSTANCE)
                .load(resId)//加载中显示图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)//错误显示图片
                .into(imageView);
    }
    public static void setImage(File file, ImageView imageView) {
        Picasso.with(App.INSTANCE)
                .load(file)//加载中显示图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)//错误显示图片
                .into(imageView);
    }

    public static RequestCreator getCircleImageBuilder(String uri) {

        return Picasso.with(App.INSTANCE)
                .load(uri.replace("\\", "/"))
                .placeholder(R.mipmap.ic_launcher)//加载中显示图片
                .error(R.mipmap.ic_launcher)//错误显示图片
                .transform(new CircleTransform());

    }

    public static RequestCreator getImageBuilder(String uri) {

        return Picasso.with(App.INSTANCE)
                .load(uri.replace("\\", "/"))
                .placeholder(R.mipmap.ic_launcher)//加载中显示图片
                .error(R.mipmap.ic_launcher);//错误显示图片
    }


    /**
     * 裁剪圆形图片
     *
     * @param source
     * @param recycleSource 裁剪成功后销毁原图
     * @return
     */
    public static Bitmap cut2Circular(Bitmap source, boolean recycleSource) {
        if (source == null) {
            System.out.println("null");
        } else {
            int width = source.getWidth();
            int height = source.getHeight();
            int diameter = Math.min(width, height);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Bitmap result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
            if (result != null) {
                Canvas canvas = new Canvas(result);
                canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(source, (diameter - width) / 2, (diameter - height) / 2, paint);
                if (recycleSource) {
                    source.recycle();
                    source = null;
                }
            } else {
                result = source;
            }
            return result;
        }
        return null;

    }

    /**
     * 根据图片Uri得到bitmap图片
     *
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
//3.设置位图颜色显示优化方式
//ALPHA_8：每个像素占用1byte内存（8位）
//ARGB_4444:每个像素占用2byte内存（16位）
//ARGB_8888:每个像素占用4byte内存（32位）
//RGB_565:每个像素占用2byte内存（16位）
//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
//5.设置位图缩放比例
//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
            opts.inSampleSize = 4;
//6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(App.INSTANCE.getContentResolver().openInputStream(uri), null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
     *
     * @param f
     * @param requiredSize
     * @return
     */
    private Bitmap decodeFile(File f, int requiredSize) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
//3.设置位图颜色显示优化方式
//ALPHA_8：每个像素占用1byte内存（8位）
//ARGB_4444:每个像素占用2byte内存（16位）
//ARGB_8888:每个像素占用4byte内存（32位）
//RGB_565:每个像素占用2byte内存（16位）
//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
//5.设置位图缩放比例
//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
            opts.inSampleSize = 4;
//6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, opts);

        } catch (FileNotFoundException e) {
        }
        return null;
    }

    Handler rHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap(bitmap);
        }
    };

    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
        }

        return bitmap;
    }


    public static String bitmaptoString(Bitmap bitmap) {
//将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("getDiskBitmap");
            System.out.println(e.toString());
        }


        return bitmap;
    }


}
