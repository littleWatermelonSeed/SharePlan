package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/9/23.
 */

public class LocalManagePlan {

    public boolean savePlan(TablePlan tablePlan){
        return tablePlan.save();
    }

    public List<TablePlan> queryAllObjectID(int offset,String userID){
        List<TablePlan> tablePlen = DataSupport.select("objectID")
                .where("userID = ?",userID)
                .order("createTime desc")
                .limit(10)
                .offset(offset)
                .find(TablePlan.class);
        return tablePlen;
    }

    public List<TablePlan> queryPlanByObjectID(String[] objectID){
        List<TablePlan> tablePlens = new ArrayList<>();
        for (String s:objectID){
            List<TablePlan> tablePlan = DataSupport
                    .where("objectID = ?",s)
                    .find(TablePlan.class);
            tablePlens.addAll(tablePlan);
        }
        return tablePlens;
    }

    public TablePlan queryPlanByObjectID(String objectID){
            List<TablePlan> tablePlan = DataSupport
                    .where("objectID = ?",objectID)
                    .find(TablePlan.class);
        return tablePlan.get(0);
    }

    public boolean existPlan(String objectID){
        return DataSupport.isExist(TablePlan.class,"objectID = ?",objectID);
    }

}
