package net.suntrans.ebuilding.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.AboutActivity;
import net.suntrans.ebuilding.activity.ChangePassActivity;
import net.suntrans.ebuilding.activity.DeviceManagerActivity;
import net.suntrans.ebuilding.activity.LoginActivity;
import net.suntrans.ebuilding.activity.QuestionActivity;
import net.suntrans.ebuilding.utils.UiUtils;

/**
 * Created by Looney on 2017/7/20.
 */

public class PerCenFragment extends RxFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_percen, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setListener(view);
    }

    private void setListener(View view) {
        view.findViewById(R.id.RLAbout).setOnClickListener(this);
        view.findViewById(R.id.RLDevice).setOnClickListener(this);
        view.findViewById(R.id.RLHelp).setOnClickListener(this);
        view.findViewById(R.id.RLModify).setOnClickListener(this);
        view.findViewById(R.id.RLQues).setOnClickListener(this);
        view.findViewById(R.id.loginOut).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RLQues:
                startActivity(new Intent(getActivity(), QuestionActivity.class));

                break;
            case R.id.RLModify:
                startActivity(new Intent(getActivity(), ChangePassActivity.class));

                break;
            case R.id.RLDevice:
                startActivity(new Intent(getActivity(), DeviceManagerActivity.class));
                break;
            case R.id.RLHelp:
                break;
            case R.id.RLAbout:
                startActivity(new Intent(getActivity(), AboutActivity.class));

                break;
            case R.id.loginOut:
                new AlertDialog.Builder(getContext())
                        .setMessage("是否退出当前账号?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getSharedPreferences().edit().clear().commit();
                                ((MainActivity)getActivity()).killAll();
                                UiUtils.showToast("已退出当前账号");
                                startActivity(new Intent(getActivity(), LoginActivity.class));

                            }
                        }).setNegativeButton("取消",null).create().show();
                break;
        }
    }
}
