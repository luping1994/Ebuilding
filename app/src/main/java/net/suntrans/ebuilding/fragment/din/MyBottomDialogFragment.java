package net.suntrans.ebuilding.fragment.din;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.ebuilding.R;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MyBottomDialogFragment extends BottomSheetDialogFragment {
    public static MyBottomDialogFragment newInstance(){
        MyBottomDialogFragment dialogFragment = new MyBottomDialogFragment();
        return dialogFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_bottom_sheet,container,false);
    }
}
