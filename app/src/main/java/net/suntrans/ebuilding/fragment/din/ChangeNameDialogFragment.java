package net.suntrans.ebuilding.fragment.din;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/16.
 */

public class ChangeNameDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private String title;
    private TextView name;
    private TextView txTitle;

    public static ChangeNameDialogFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        ChangeNameDialogFragment dialogFragment = new ChangeNameDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_change_name,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        title = getArguments().getString("title");
        view.findViewById(R.id.queding).setOnClickListener(this);
         view.findViewById(R.id.qvxiao).setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
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
        void changeName(String name);
    }
    private void update() {
        String name1 =name.getText().toString();
        if (TextUtils.isEmpty(name1)){
            UiUtils.showToast("请输入场景名字");
            return;
        }
        LogUtil.i(name1);

        if (listener!=null){
            listener.changeName(name1);
        }
        dismiss();
    }
}
