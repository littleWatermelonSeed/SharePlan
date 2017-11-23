package com.sayhellototheworld.littlewatermelon.shareplan.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobMangeComment;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.SaveCommentDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseSlideBcakStatusActivity;

import cn.bmob.v3.exception.BmobException;

public class WriteCommentActivity extends BaseSlideBcakStatusActivity implements BaseActivityDo ,
        View.OnClickListener,SaveCommentDo{

    private TextView textView_back;
    private TextView textView_send;
    private EditText editText;

    private BmobMangeComment bmc;
    private BaseNiceDialog dialog;
    private Handler handler;
    private String planObjectID;
    private String replyUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        init();
    }

    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        textView_back = (TextView)findViewById(R.id.activity_write_comment_back);
        textView_back.setOnClickListener(this);
        textView_send = (TextView)findViewById(R.id.activity_write_comment_send);
        textView_send.setOnClickListener(this);
        editText = (EditText)findViewById(R.id.activity_write_comment_edit);
    }

    @Override
    public void initParam() {
        bmc = new BmobMangeComment(this);
        planObjectID = getIntent().getStringExtra("planObjectID");
        replyUserID = getIntent().getStringExtra("replyUserID");
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }else if(msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("评论发表成功");
                    finish();
                }
            }
        };
    }

    @Override
    public void initShow() {

    }

    public static void startWriteCommentActivity(Context context,String planObjectID){
        Intent intent = new Intent(context,WriteCommentActivity.class);
        intent.putExtra("planObjectID",planObjectID);
        context.startActivity(intent);
    }

    public static void startWriteCommentActivity(Context context,String planObjectID,String replyUserID){
        Intent intent = new Intent(context,WriteCommentActivity.class);
        intent.putExtra("planObjectID",planObjectID);
        intent.putExtra("replyUserID",replyUserID);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_comment_back:
                finish();
                break;
            case R.id.activity_write_comment_send:
                sendComment();
                break;
        }
    }

    private void sendComment(){
        String commentContent = editText.getText().toString();
        commentContent.replaceAll("\\s","");
        if (commentContent.equals("")){
            MyToastUtil.showToast("评论内容不能为空");
            return;
        }
        final CommentBean commentBean = new CommentBean();
        PlanBean planBean = new PlanBean();
        planBean.setObjectId(planObjectID);
        commentBean.setPlan(planBean);
        commentBean.setContent(commentContent);
        commentBean.setRead(false);
        commentBean.setReplyUserRead(false);
        commentBean.setUser(BmobManageUser.getCurrentUser());
        if (replyUserID != null){
            commentBean.setReplyUserID(replyUserID);
        }
        DialogLoading.showLoadingDialog(getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("发送中...");
                        bmc.saveComment(commentBean,WriteCommentActivity.this);
                    }
                });
    }

    @Override
    public void saveSuccess(String s) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"评论发表成功", DialogLoading.MSG_SUCCESS);
        finish();
    }

    @Override
    public void saveFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(this,e);
    }

}
