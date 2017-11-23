package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlanProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableProgressImagePath;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/10/6.
 */

public class LocalManageProgressImagePath {

    public List<TableProgressImagePath> saveImagePath(List<String> imagePaths){
        List<TableProgressImagePath> imagePathList = new ArrayList<>();
        for (String s:imagePaths){
            TableProgressImagePath tableImagePath = new TableProgressImagePath();
            tableImagePath.setImagePath(s);
            imagePathList.add(tableImagePath);
        }
        DataSupport.saveAll(imagePathList);
        return imagePathList;
    }

    public List<TableProgressImagePath> saveImagePath(List<String> imagePaths, TablePlanProgress tablePlanProgress){
        List<TableProgressImagePath> imagePathList = new ArrayList<>();
        for (String s:imagePaths){
            TableProgressImagePath tableImagePath = new TableProgressImagePath();
            tableImagePath.setImagePath(s);
            tableImagePath.setTablePlanProgress(tablePlanProgress);
            imagePathList.add(tableImagePath);
        }
        DataSupport.saveAll(imagePathList);
        return imagePathList;
    }

}
