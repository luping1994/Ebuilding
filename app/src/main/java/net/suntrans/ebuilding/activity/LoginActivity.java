package net.suntrans.ebuilding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.LoginResult;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;
import net.suntrans.ebuilding.views.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/7/21.
 */

public class LoginActivity extends BasedActivity implements View.OnClickListener {
    private EditView account;
    private EditView password;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = (EditView) findViewById(R.id.account);
        password = (EditView) findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        }
    }

    private void login() {
        String accounts = account.getText().toString();
        String passwords = password.getText().toString();
        if (TextUtils.isEmpty(accounts)) {
            UiUtils.showToast("请输入账号");

            return;
        }
        if (TextUtils.isEmpty(passwords)) {
            UiUtils.showToast("请输入密码");
            return;
        }
        if (dialog== null){
            dialog= new LoadingDialog(this);
            dialog.setWaitText("登录中...");
        }
        dialog.show();

        RetrofitHelper.getLoginApi().login("password", "2", "559eb687a4fcafdabe991c320172fcc9", accounts, passwords)
                .compose(this.<LoginResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        if (e instanceof HttpException) {
                            if (e.getMessage() != null) {
                                if (e.getMessage().equals("HTTP 401 Unauthorized")) {
                                    UiUtils.showToast("账号或密码错误");
                                }
                            } else {
                                UiUtils.showToast("登录失败,请检查网络连接");
                            }
                        }
                        if (e instanceof SocketTimeoutException){
                            UiUtils.showToast("连接超时");
                        }
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        dialog.dismiss();
                        if (loginResult != null) {
                            LogUtil.i(loginResult.toString());
                            if (loginResult.access_token != null) {
                                App.getSharedPreferences().edit().putString("access_token", loginResult.access_token)
                                        .putString("expires_in", loginResult.expires_in)
                                        .putLong("firsttime", System.currentTimeMillis())
                                        .commit();

                                UiUtils.showToast("登录成功");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            } else {
                                UiUtils.showToast("服务器错误!登录失败");

                            }

                        } else {

                            UiUtils.showToast("服务器错误!登录失败");

                        }
                    }
                });
    }
}
