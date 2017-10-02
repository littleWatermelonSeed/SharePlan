package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table;

import org.litepal.crud.DataSupport;

/**
 * Created by 123 on 2017/9/22.
 */

public class TableImagePath extends DataSupport {

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
