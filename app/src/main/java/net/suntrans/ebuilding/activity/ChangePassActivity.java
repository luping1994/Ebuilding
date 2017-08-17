package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ChangedPasswordEntity;
import net.suntrans.ebuilding.test.post;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;
import net.suntrans.ebuilding.views.LoadingDialog;

import retrofit2.http.POST;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.pgyersdk.c.a.d;


/**
 * Created by Looney on 2017/7/24.
 */

public class ChangePassActivity extends BasedActivity {


    EditView oldPass;
    EditView newPass;
    EditView rePass;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        oldPass = (EditView) findViewById(R.id.oldPass);
        newPass = (EditView) findViewById(R.id.newPass);
        rePass = (EditView) findViewById(R.id.repassword);
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView touxiang  = (ImageView) findViewById(R.id.img);
        TextView name = (TextView) findViewById(R.id.name);
        String name1 = App.getSharedPreferences().getString("nikename","TIT餐厅");
        name.setText(name1);
        String imgurl = App.getSharedPreferences().getString("touxiang","-1");
        Glide.with(this)
                .load("http://tit.suntrans-cloud.com"+imgurl)
                .crossFade()
                .override(UiUtils.dip2px(35),UiUtils.dip2px(35))
                .placeholder(R.drawable.user_white)
                .into(touxiang);
    }


    public void changePass(View view) {
        String oldpass = oldPass.getText().toString();
        String newpass = newPass.getText().toString();
        final String repass = rePass.getText().toString();
        if (TextUtils.isEmpty(oldpass)) {
            UiUtils.showToast("请输入旧密码");
            return;
        }

        if (TextUtils.isEmpty(newpass)) {
            UiUtils.showToast("请输入新密码");
            return;
        }
        if (newpass.length()<6){
            UiUtils.showToast("新密码长度必须为6位以上");
            return;
        }
        if (TextUtils.isEmpty(repass)) {
            UiUtils.showToast("请确认新密码");
            return;
        }

        if (!newpass.equals(repass)) {
            UiUtils.showToast("两次输入的密码不一致");
            return;
        }
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后...");
        }
        dialog.show();

        RetrofitHelper.getApi().changedPassword(oldpass, newpass)
                .compose(this.<ChangedPasswordEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangedPasswordEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        if (e != null)
                            UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ChangedPasswordEntity result) {
                        dialog.dismiss();
                        if (result!=null){

                            if (result.code==200){
                                new AlertDialog.Builder(ChangePassActivity.this)
                                        .setMessage("修改密码成功,您必须重新登录")
                                        .setCancelable(false)
                                        .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                App.getSharedPreferences().edit().clear().commit();
                                                killAll();
                                                startActivity(new Intent(ChangePassActivity.this, LoginActivity.class));
                                            }
                                        }).create().show();

                            }else {
                                UiUtils.showToast(result.msg);
                            }
                        }else
                        {
                            UiUtils.showToast(result.msg);
                        }
                    }
                });
    }
}
