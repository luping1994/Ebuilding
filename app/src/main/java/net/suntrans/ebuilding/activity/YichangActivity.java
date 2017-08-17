package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.bean.YichangEntity;

import java.util.List;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangActivity extends BasedActivity{
    private List<YichangEntity> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang);
        initToolBar();
        init();
    }

    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MyAdapter adapter = new MyAdapter(R.layout.item_yicahng,datas);
        recyclerView.setAdapter(adapter);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("异常提示");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    class MyAdapter extends BaseQuickAdapter<YichangEntity,BaseViewHolder>{

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<YichangEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, YichangEntity item) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    private void getdata() {
    }
}
