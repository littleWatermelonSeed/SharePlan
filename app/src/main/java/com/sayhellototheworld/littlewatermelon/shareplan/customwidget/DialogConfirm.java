package com.sayhellototheworld.littlewatermelon.shareplan.customwidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.shareplan.R;

/**
 * Created by 123 on 2017/9/6.
 */

public class DialogConfirm extends BaseNiceDialog {

    private String title;
    private String message;
    private CancleAndOkDo cb;

    public static DialogConfirm newInstance(String title, String message, CancleAndOkDo cb) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        DialogConfirm dialog = new DialogConfirm();
        dialog.setArguments(bundle);
        dialog.setcb(cb);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        title = bundle.getString("title");
        message = bundle.getString("message");
    }

    @Override
    public int intLayoutId() {
        return R.layout.nicedialog_remind_layout;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        holder.setText(R.id.nicedialog_remind_layout_title, title);
        holder.setText(R.id.nicedialog_remind_layout_message, message);
        holder.setOnClickListener(R.id.nicedialog_remind_layout_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cb.cancle();
            }
        });

        holder.setOnClickListener(R.id.nicedialog_remind_layout_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cb.ok();
            }
        });
    }

    public void setcb(CancleAndOkDo cb){
        this.cb = cb;
    }

    public interface CancleAndOkDo{
        void cancle();
        void ok();
    }

}
