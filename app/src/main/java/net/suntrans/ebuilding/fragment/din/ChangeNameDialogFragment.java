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

    private String id;
    private TextView name;

    public static ChangeNameDialogFragment newInstance(String id){
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
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
        id = getArguments().getString("id");
        view.findViewById(R.id.queding).setOnClickListener(this);
         view.findViewById(R.id.qvxiao).setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
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

    private void update() {
        String name1 =name.getText().toString();
        if (TextUtils.isEmpty(name1)){
            UiUtils.showToast("请输入场景名字");
            return;
        }
        LogUtil.i(id);
        LogUtil.i(name1);
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name1);
        RetrofitHelper.getApi().updateScene(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SampleResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SampleResult result) {
                        UiUtils.showToast(result.getMsg());
                        if (result.getCode()==200){
                            dismiss();
                        }
                    }
                });
    }
}
