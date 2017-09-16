package com.sayhellototheworld.littlewatermelon.shareplan.model.local_file;

import java.io.File;

/**
 * Created by 123 on 2017/9/7.
 */

public class GetFile {

    public static File getInternalImageFile(){
        File file = new File(FilePath.getInternalCacheDirectory("myCache"),"internalImage");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalCacheImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"cache");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalTemporaryImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"temporary");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalCacheDataFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"cache");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"data");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalUserImageFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"user");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"image");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

    public static File getExternalUserDataFile(){
        File fileDir = new File(FilePath.getExternalCacheDirectory("sharePlan"),"user");
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,"data");
        if (!file.exists()){
            file.mkdir();
        }
        return file;
    }

}
