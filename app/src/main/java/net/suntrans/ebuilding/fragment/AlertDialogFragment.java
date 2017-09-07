package net.suntrans.ebuilding.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import net.suntrans.ebuilding.activity.BasedActivity;
import net.suntrans.ebuilding.activity.LoginActivity;

/**
 * Created by Looney on 2017/9/6.
 */

public class AlertDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("您的账号已从别处登录")
                .setCancelable(false)
                .setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        ((BasedActivity) getActivity()).killAll();
                    }
                })
                .create();
        return dialog;

    }
}
