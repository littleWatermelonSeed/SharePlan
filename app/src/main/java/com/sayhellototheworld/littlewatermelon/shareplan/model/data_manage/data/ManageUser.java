package com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.shareplan.model.thread_manager.JoinToThreadPool;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.DownLoadFileDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.QueryUserDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.ResetPasswordBySmsDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserGetKeyCodeDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserLoginDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserRegisterDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserUpdateDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.VerifySmsCodeDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 123 on 2017/9/6.
 */

public class ManageUser {

    private Context mContext;
    private Runnable mRunnable;
    private MySharedPreferences mPreferences;

    public final static String SMS_TEMPLATE_REGISTER = "register";
    public final static String SMS_TEMPLATE_CHANGEPASSWORD = "changePS";

    public ManageUser(Context context) {
        mContext = context;
        mPreferences = MySharedPreferences.getInstance();
    }

    public void asynUpdateWhitHeadPic(final MyUserBean userBean, final BmobFile headPicFile, final UserUpdateDo done) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                updateWhitHeadPic(userBean, headPicFile, done);
            }
        };
        JoinToThreadPool.joinToCache(mRunnable);
    }

    public void asynUpdateWhitoutHeadPic(final MyUserBean userBean, final UserUpdateDo done) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                updateWhitoutHeadPic(userBean, done);
            }
        };
        JoinToThreadPool.joinToCache(mRunnable);
    }

    public void updateWhitHeadPic(final MyUserBean userBean, final BmobFile headPicFile, final UserUpdateDo done) {

        headPicFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e == null) {
                            userBean.setHeadPortrait(headPicFile);
                            ManageFile.saveHeadPortrait(userBean.getHeadPortrait().getLocalFile(),
                                    PictureUtil.getPicNameFromUrl(userBean.getHeadPortrait().getUrl()));
                            userBean.update(getCurrentUser().getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        done.updateSuccess();
                                    } else {
                                        done.updateFail(e);
                                    }
                                }
                            });
                        } else {
                            done.updateFail(e);
                        }
                    }
                });
            }
        });

    }

    private void updateWhitoutHeadPic(MyUserBean userBean, final UserUpdateDo done) {
        userBean.update(getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e == null) {
                            done.updateSuccess();
                        } else {
                            done.updateFail(e);
                        }
                    }
                });
            }
        });
    }

    public void query(String columnName, String values, final QueryUserDo done) {
        BmobQuery<MyUserBean> query = new BmobQuery<MyUserBean>();
        query.addWhereEqualTo(columnName, values);
        query.findObjects(new FindListener<MyUserBean>() {
            @Override
            public void done(final List<MyUserBean> object, final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e == null) {
                            done.querySuccess(object);
                        } else {
                            done.queryFail(e);
                        }
                    }
                });
            }
        });
    }

    public void loginAndSyncUser(String userName, String password, final UserLoginDo done) {
        login(userName, password, new UserLoginDo() {
            @Override
            public void loginSuccess(final MyUserBean myUserBean) {
                if ((myUserBean.getHeadPortrait() == null || myUserBean.getHeadPortrait().getUrl() == null) &&
                        (myUserBean.getSkin() == null || myUserBean.getSkin().getUrl() == null)) {
                    done.loginSuccess(myUserBean);
                    return;
                } else {
                    downPic(myUserBean,done);
                }
            }

            @Override
            public void loginFail(BmobException ex) {
                done.loginFail(ex);
                BmobUser.logOut();
            }

        });
    }

    public void login(String userName, String password, final UserLoginDo done) {
        MyUserBean bu2 = new MyUserBean();
        bu2.setUsername(userName);
        bu2.setPassword(password);
        bu2.login(new SaveListener<MyUserBean>() {
            @Override
            public void done(final MyUserBean myUserBean, final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e == null) {
                            done.loginSuccess(myUserBean);
                        } else {
                            done.loginFail(e);
                        }
                    }
                });
            }
        });
    }

    private void downPic(final MyUserBean myUserBean, final UserLoginDo done) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                downLoadHeadPortrait(myUserBean,done);
            }
        };
        JoinToThreadPool.joinToCache(runnable);
    }

    private void downLoadHeadPortrait(final MyUserBean myUserBean, final UserLoginDo done) {
        if (myUserBean.getHeadPortrait() == null || myUserBean.getHeadPortrait().getUrl() == null ||
                ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(myUserBean.getHeadPortrait().getUrl())) != null){
            Log.i("niyuanjie","没有头像或已有本地头像");
            downLoadSkin(myUserBean,done);
            return;
        }
        Log.i("niyuanjie","有头像且没有本地头像 开始下载头像");
        final BmobFile headPic = new BmobFile(PictureUtil.getPicNameFromUrl(myUserBean.getHeadPortrait().getUrl()) + ".png",
                "", myUserBean.getHeadPortrait().getUrl());
        File saveFile = new File(GetFile.getNewInternalHeadImageFile(), headPic.getFilename());
        downloadFile(headPic, saveFile, new DownLoadFileDo() {
            @Override
            public void onStart() {

            }

            @Override
            public void downLoadSuccess(String savePath) {
                downLoadSkin(myUserBean,done);
            }

            @Override
            public void downLoadFail(final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        done.loginFail(e);
                        BmobUser.logOut();
                    }
                });
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {

            }
        });
    }

    private void downLoadSkin(final MyUserBean myUserBean, final UserLoginDo done) {
        if (myUserBean.getSkin() == null || myUserBean.getSkin().getUrl() == null ||
                ManageFile.getSelfBackground(PictureUtil.getPicNameFromUrl(myUserBean.getSkin().getUrl())) != null){
            Log.i("niyuanjie","没有皮肤或已有本地皮肤");
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    done.loginSuccess(myUserBean);
                }
            });
            return;
        }
        Log.i("niyuanjie","有头像且没有有本地皮肤 开始下载皮肤");
        final BmobFile skin = new BmobFile(PictureUtil.getPicNameFromUrl(myUserBean.getSkin().getUrl()) + ".png",
                "", myUserBean.getSkin().getUrl());
        File saveFile = new File(GetFile.getNewInternalSkinImageFile(), skin.getFilename());
        downloadFile(skin, saveFile, new DownLoadFileDo() {
            @Override
            public void onStart() {

            }

            @Override
            public void downLoadSuccess(String savePath) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        done.loginSuccess(myUserBean);
                    }
                });
            }

            @Override
            public void downLoadFail(final BmobException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        done.loginFail(e);
                        BmobUser.logOut();
                    }
                });
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {

            }
        });
    }

    public void downloadFile(BmobFile file, final File saveFile, final DownLoadFileDo loadFileDo) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                loadFileDo.onStart();
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
                    loadFileDo.downLoadSuccess(savePath);
                } else {
                    loadFileDo.downLoadFail(e);
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                loadFileDo.onProgress(value, newworkSpeed);
            }

        });
    }

    public void resetPasswordBySMSCode(String smsCode, String newPS, final ResetPasswordBySmsDo done) {
        BmobUser.resetPasswordBySMSCode(smsCode, newPS, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    done.resetSuccess();
                } else {
                    done.resetFail(e);
                }
            }
        });
    }

    public void getKeyCode(String phoneNum, final UserGetKeyCodeDo done, String template) {

        BmobSMS.requestSMSCode(phoneNum, template, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    done.sendNoteSuccess();
                } else {
                    done.sendNoteFail(e);
                }
            }
        });
    }

    public void verifySmsCode(MyUserBean userBean, String keyCode, final VerifySmsCodeDo done) {
        BmobSMS.verifySmsCode(userBean.getMobilePhoneNumber(), keyCode, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    done.verifySuccess();
                } else {
                    done.verifyFail(ex);
                }
            }
        });
    }

    public void registerCommit(final MyUserBean userBean,
                               final String keyCode,
                               final UserRegisterDo done) {
        BmobSMS.verifySmsCode(userBean.getMobilePhoneNumber(), keyCode, new UpdateListener() {
            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    registerAfterVerifySms(userBean, done);
                } else {
                    done.registerFail(ex);
                }
            }
        });

    }

    private void registerAfterVerifySms(MyUserBean userBean, final UserRegisterDo done) {
        userBean.signUp(new SaveListener<MyUserBean>() {
            @Override
            public void done(MyUserBean userBean, BmobException e) {
                if (e == null) {
                    loginAfterRegister(userBean);
                    done.registerSuccess();
                } else {
                    done.registerFail(e);
                }
            }
        });
    }

    private void loginAfterRegister(MyUserBean userBean) {
        userBean.login(new SaveListener<MyUserBean>() {
            @Override
            public void done(MyUserBean userBean, BmobException e) {
                if (e == null) {

                } else {
                    if (e.getErrorCode() == 9018)
                        return;
                    BmobExceptionUtil.dealWithException(mContext, e, "loginAfterRegister");
                }
            }
        });
    }

    public MyUserBean getCurrentUser() {
        MyUserBean userBean = BmobUser.getCurrentUser(MyUserBean.class);
        if (userBean != null) {
            return userBean;
        } else {
            return null;
        }
    }

}
