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
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.ChangedPasswordEntity;
import net.suntrans.ebuilding.rx.BaseSubscriber;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;
import net.suntrans.ebuilding.views.GlideRoundTransform;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.security.PublicKey;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/24.
 */

public class QuestionActivity extends BasedActivity {


    EditView oldPass;
    EditView newPass;
    EditView rePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queston);
        oldPass = (EditView) findViewById(R.id.oldPass);
//        newPass = (EditView) findViewById(R.id.newPass);
//        rePass = (EditView) findViewById(R.id.repassword);
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
                .transform(new GlideRoundTransform(this,UiUtils.dip2px(16)))
                .crossFade()
                .override(UiUtils.dip2px(35),UiUtils.dip2px(35))
                .placeholder(R.drawable.user_white)
                .into(touxiang);
    }

    public void changePass(View view) {
        String contents = oldPass.getText().toString();
        contents = contents.replace(" ", "");
        if (TextUtils.isEmpty(contents)) {
            UiUtils.showToast("请您输入具体内容");
            return;
        }

        commit(contents);


    }

    private LoadingDialog dialog;

    private void commit(String contents) {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.setWaitText("请稍后...");
        }
        dialog.show();
        RetrofitHelper.getApi().commitGusetBook(contents)
                .compose(this.<ChangedPasswordEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ChangedPasswordEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        e.printStackTrace();
                        dialog.dismiss();
//                        if (e != null)
//                            UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(ChangedPasswordEntity result) {
                        dialog.dismiss();
                        if (result == null) {
                            UiUtils.showToast(result.msg);
                        } else {
                            new AlertDialog.Builder(QuestionActivity.this)
                                    .setMessage("已收到您的反馈,我们会认真查看,并尽快修复及完善,感谢您的支持!")
                                    .setCancelable(false)
                                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).create().show();
                        }
                    }
                });
    }
}
