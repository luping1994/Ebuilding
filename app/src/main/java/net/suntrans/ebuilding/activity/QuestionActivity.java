package net.suntrans.ebuilding.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.EditView;

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
    }

    public void changePass(View view) {
        String oldpass = oldPass.getText().toString();
        oldpass = oldpass.replace(" ","");
        if (TextUtils.isEmpty(oldpass)) {
            UiUtils.showToast("请您输入具体内容");
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage("已收到您的建议!")
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();


    }
}
