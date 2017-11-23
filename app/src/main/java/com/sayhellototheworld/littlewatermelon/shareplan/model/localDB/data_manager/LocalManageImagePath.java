package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableImagePath;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/9/23.
 */

public class LocalManageImagePath {

    public List<TableImagePath> saveImagePath(List<String> imagePaths){
        List<TableImagePath> imagePathList = new ArrayList<>();
        for (String s:imagePaths){
            TableImagePath tableImagePath = new TableImagePath();
            tableImagePath.setImagePath(s);
            imagePathList.add(tableImagePath);
        }
        DataSupport.saveAll(imagePathList);
        return imagePathList;
    }

}
