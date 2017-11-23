package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 123 on 2017/10/6.
 */

public class TableProgressImagePath extends DataSupport{

    private String imagePath;
    private TablePlanProgress mTablePlanProgress;

    public TablePlanProgress getTablePlanProgress() {
        return mTablePlanProgress;
    }

    public void setTablePlanProgress(TablePlanProgress tablePlanProgress) {
        mTablePlanProgress = tablePlanProgress;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
