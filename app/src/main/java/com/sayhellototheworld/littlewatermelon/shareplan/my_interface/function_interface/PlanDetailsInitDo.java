package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;

import java.util.List;

/**
 * Created by 123 on 2017/10/1.
 */

public interface PlanDetailsInitDo {

    void initShow(TablePlan tablePlan);
    void showComment();
    void showImage(List<String> imageUrls);

}
