package com.sayhellototheworld.littlewatermelon.shareplan;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mCircleImageView;
    private Button button_shangchuan;
    private Button button_xiazai;

    private String objectID;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        mCircleImageView = (CircleImageView) findViewById(R.id.activity_test_headPortrait);
        button_shangchuan = (Button) findViewById(R.id.activity_test_shangchuan);
        button_shangchuan.setOnClickListener(this);
        button_xiazai = (Button) findViewById(R.id.activity_test_xiazai);
        button_xiazai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_test_shangchuan:
                shangchuan2();
                break;
            case R.id.activity_test_xiazai:
                xiazai2();
                break;
        }
    }

    private void shangchuan2(){
        ManageUser manageUser = new ManageUser(this);
        MyUserBean myUserBean = manageUser.getCurrentUser();
        if(myUserBean.getHeadPortrait().getUrl() !=null){
            Log.i("niyuanjie","头像url = " + myUserBean.getHeadPortrait().getUrl());
            Log.i("niyuanjie","头像name = " + PictureUtil.getPicNameFromUrl(myUserBean.getHeadPortrait().getUrl()));
        }else {
            Log.i("niyuanjie","头像url不存在");
        }
    }

    private void xiazai2(){

    }

    private void shangchuan() {
        TestFile testFile = new TestFile();
        File headImg = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/headimg.png");
        final BmobFile bmobFile = new BmobFile("testPic1.png","",headImg.toString());
        testFile.setTestPic(bmobFile);
        testFile.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("niyuanjie", "测试文件上传成功 objectID = " + s + "  url = " + bmobFile.getFileUrl());
                } else {
                    Log.i("niyuanjie", "测试文件上传失败 objectID = " + s);
                    BmobExceptionUtil.dealWithException(TestActivity.this, e);
                }
                objectID = s;
            }
        });
    }

    private void xiazai() {
        BmobQuery<TestFile> bmobQuery = new BmobQuery<TestFile>();
        bmobQuery.getObject(objectID, new QueryListener<TestFile>() {
            @Override
            public void done(TestFile testFile, BmobException e) {
                if (e == null) {
                    if (testFile.getTestPic() != null) {
                        Log.i("niyuanjie", "文件查询成功，存在图片文件： ");
                        testFile.getTestPic().download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    Log.i("niyuanjie", "文件下载成功 文件路径为： " + s);
                                    Glide.with(TestActivity.this)
                                            .load(new File(s))
                                            .dontAnimate()
                                            .into(mCircleImageView);
                                }else {
                                    Log.i("niyuanjie", "文件下载失败 文件路径为： " + s);
                                    BmobExceptionUtil.dealWithException(TestActivity.this,e);
                                }

                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    } else {
                        Log.i("niyuanjie", "数据查询成功，不存在图片文件");
                    }
                } else {
                    BmobExceptionUtil.dealWithException(TestActivity.this, e);
                }
            }
        });
    }

    class TestFile extends BmobObject {

        private BmobFile testPic;

        public BmobFile getTestPic() {
            return testPic;
        }

        public void setTestPic(BmobFile testPic) {
            this.testPic = testPic;
        }
    }

}
