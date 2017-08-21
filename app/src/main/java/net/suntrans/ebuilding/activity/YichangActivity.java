package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.YichangEntity;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

import static net.suntrans.ebuilding.R.id.recyclerView;

/**
 * Created by Looney on 2017/8/17.
 */

public class YichangActivity extends BasedActivity{
    private List<YichangEntity.DataBean.ListsBean> datas;
    private MyAdapter adapter;
    private StateView stateView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yichang);
        stateView = StateView.inject(findViewById(R.id.content));
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getdata();
            }
        });
        initToolBar();
        init();
    }

    private void init() {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyAdapter(R.layout.item_yicahng,datas);
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

    class MyAdapter extends BaseQuickAdapter<YichangEntity.DataBean.ListsBean,BaseViewHolder>{

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<YichangEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, YichangEntity.DataBean.ListsBean item) {
            helper.setText(R.id.msg,""+item.getName()+",异常类型:"+item.getMessage())
                    .setText(R.id.time,item.getCreated_at());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    private void getdata() {
        recyclerView.setVisibility(View.INVISIBLE);
        stateView.showLoading();
        addSubscription(RetrofitHelper.getApi().getYichang(), new Subscriber<YichangEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                    e.printStackTrace();
                UiUtils.showToast("服务器错误");
                recyclerView.setVisibility(View.INVISIBLE);
                stateView.showRetry();
            }

            @Override
            public void onNext(YichangEntity o) {
                if (o.getCode()==200){
                    List<YichangEntity.DataBean.ListsBean> lists = o.getData().getLists();
                    if (lists==null||lists.size()==0){
                        stateView.showEmpty();
                        recyclerView.setVisibility(View.INVISIBLE);

                    }else {
                        stateView.showContent();
                        recyclerView.setVisibility(View.VISIBLE);
                        datas.clear();
                        datas.addAll(lists);
                        adapter.notifyDataSetChanged();
                    }

                }else {
                    UiUtils.showToast("获取数据失败");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        App.getSharedPreferences().edit().putInt("yichangCount",datas.size()).commit();
        super.onStop();
    }
}
