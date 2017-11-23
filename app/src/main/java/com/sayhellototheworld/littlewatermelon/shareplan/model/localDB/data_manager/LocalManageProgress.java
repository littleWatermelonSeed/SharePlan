package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlanProgress;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 123 on 2017/10/5.
 */

public class LocalManageProgress {

    public boolean savaLocalProgress(TablePlanProgress progress){
        return progress.save();
    }

    public boolean existProgress(String objectID){
        return DataSupport.isExist(TablePlanProgress.class,"objectID = ?",objectID);
    }

    public TablePlanProgress queryLocalProgressByObjectID(String objectID){
        List<TablePlanProgress> progresses = DataSupport
                .where("objectID = ?",objectID)
                .find(TablePlanProgress.class);
        return progresses.get(0);
    }

}
