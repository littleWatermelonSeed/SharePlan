package com.sayhellototheworld.littlewatermelon.shareplan.model.local_file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayhellototheworld.littlewatermelon.shareplan.SPApplication;
import com.sayhellototheworld.littlewatermelon.shareplan.util.SysUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 123 on 2017/9/7.
 */

public class ManageFile {

    private static final Context context = SPApplication.getAppContext();

    public final static String FILENAME_USER_HEADPORTRAIL = "user_portrail";
    public final static String FILENAME_USER_BACKGROUND_DEFAULT = "user_background_default";
    public final static String FILENAME_USER_BACKGROUND_SELF = "user_background_self";

    public static void saveheadPortrait(int resourceID,String userID) {
        final File headPortrait = new File(GetFile.getInternalHeadImageFile(), FILENAME_USER_HEADPORTRAIL + userID + ".png");
        saveFile(resourceID, context, headPortrait);
    }

    public static void saveHeadPortrait(File headPic,String heapPicUrl) {
        final File headPortrait = new File(GetFile.getNewInternalHeadImageFile(), heapPicUrl + ".png");
        saveImage(headPic, headPortrait);
    }

    public static void saveSelfBackground(File background,String skinPicUrl) {
        final File selfBackground = new File(GetFile.getNewInternalSkinImageFile(), skinPicUrl + ".png");
        saveImage(background, selfBackground);
    }

    public static File getHeadPortrait(String heapPicUrl){
        File file = new File(GetFile.getInternalHeadImageFile(), heapPicUrl + ".png");
        if (!file.exists()){
            return null;
        }
        return file;
    }

    public static File getSelfBackground(String skinPicUrl){
        File file = new File(GetFile.getInternalSkinImageFile(), skinPicUrl + ".png");
        if (!file.exists()){
            return null;
        }
        return file;
    }

    private static void saveImage(File headPic, File headPortrait) {
        if (headPortrait.exists()){
            headPortrait.delete();
        }
        try {
            headPortrait.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            inputStream = new FileInputStream(headPic);
            outputStream = new FileOutputStream(headPortrait);
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveFile(int resourceID, Context context, File save) {
        if (save.exists()) {
            save.delete();
        }
        try {
            save.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File saveFile = save;
        Glide.with(context)
                .load(resourceID)
                .dontAnimate()
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (resource != null) {
                            Bitmap bitmap = drawableToBitmap(resource);
                            OutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream(saveFile);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                                outputStream.close();
                                bitmap.recycle();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(SysUtil.getDisplayWidth(),SysUtil.getDisplayHeight());
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
