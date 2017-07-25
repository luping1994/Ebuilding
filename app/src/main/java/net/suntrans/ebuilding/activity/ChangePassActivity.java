package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;

import static com.pgyersdk.conf.a.f;

/**
 * Created by Looney on 2017/7/24.
 */

public class ChangePassActivity extends BasedActivity {


    EditView oldPass;
    EditView newPass;
    EditView rePass;

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
    }

    public void changePass(View view) {
        String oldpass = oldPass.getText().toString();
        String newpass = newPass.getText().toString();
        String repass = rePass.getText().toString();
        if (TextUtils.isEmpty(oldpass)) {
            UiUtils.showToast("请输入旧密码");
            return;
        }

        if (TextUtils.isEmpty(newpass)) {
            UiUtils.showToast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(repass)) {
            UiUtils.showToast("请确认新密码");
            return;
        }

        if (!newpass.equals(repass)){
            UiUtils.showToast("两次输入的密码不一致");
            return;
        }
    }
}
