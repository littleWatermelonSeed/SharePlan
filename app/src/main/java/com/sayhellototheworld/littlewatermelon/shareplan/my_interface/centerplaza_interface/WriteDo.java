package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;

import java.util.List;

/**
 * Created by 123 on 2017/8/25.
 */

public interface WriteDo {

    void submitPlan(TablePlan tablePlan, List<String> imagePaths);

}
