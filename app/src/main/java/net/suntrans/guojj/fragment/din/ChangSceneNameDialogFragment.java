package net.suntrans.guojj.fragment.din;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import net.suntrans.guojj.R;
import net.suntrans.guojj.utils.LogUtil;
import net.suntrans.guojj.utils.UiUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class ChangSceneNameDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private String title;
    private TextView name;
    private TextView txTitle;
    private TextView nameen;



    public static ChangSceneNameDialogFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        ChangSceneNameDialogFragment dialogFragment = new ChangSceneNameDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_changescebe_name,container,false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), getTheme());
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        dialog.getWindow().setAttributes(layoutParams);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title = getArguments().getString("title");
        view.findViewById(R.id.queding).setOnClickListener(this);
         view.findViewById(R.id.qvxiao).setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
        nameen = (TextView) view.findViewById(R.id.nameEn);
        txTitle = (TextView) view.findViewById(R.id.title);
        txTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.queding:
                update();
                break;
            case R.id.qvxiao:
                dismiss();
                break;
        }
    }

    private ChangeNameListener listener;

    public ChangeNameListener getListener() {
        return listener;
    }

    public void setListener(ChangeNameListener listener) {
        this.listener = listener;
    }

    public interface ChangeNameListener{
        void changeName(String name,String nameEn);
    }
    private void update() {
        String name1 =name.getText().toString();

        LogUtil.i(name1);
        String nameEn1 = nameen.getText().toString();

        if (TextUtils.isEmpty(name1)&&TextUtils.isEmpty(nameEn1)){
            UiUtils.showToast("请至少选择一项");
            return;
        }
        if (listener!=null){
            listener.changeName(name1,nameEn1);
        }
        dismiss();
    }
}
