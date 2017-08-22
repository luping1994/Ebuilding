package net.suntrans.ebuilding.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.api.Api;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
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

public class YichangActivity extends BasedActivity {
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
        adapter = new MyAdapter(R.layout.item_yicahng, datas);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                delete(datas.get(pos).getLog_id());
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void delete(int id) {
//        UiUtils.showToast("已经删除的条目id="+id);
        addSubscription(RetrofitHelper.getApi().deleteLog(id + ""), new Subscriber<SampleResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(SampleResult o) {
                if (o.getCode()==200){
                    UiUtils.showToast("删除成功!");
                }else {
                    UiUtils.showToast("删除失败");
                }
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("异常提示");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    class MyAdapter extends BaseItemDraggableAdapter<YichangEntity.DataBean.ListsBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<YichangEntity.DataBean.ListsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, YichangEntity.DataBean.ListsBean item) {
            helper.setText(R.id.msg, "" + item.getName() + ",异常类型:" + item.getMessage())
                    .setText(R.id.time, item.getUpdated_at());
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
                if (o.getCode() == 200) {
                    List<YichangEntity.DataBean.ListsBean> lists = o.getData().getLists();
                    if (lists == null || lists.size() == 0) {
                        stateView.showEmpty();
                        recyclerView.setVisibility(View.INVISIBLE);

                    } else {
                        stateView.showContent();
                        recyclerView.setVisibility(View.VISIBLE);
                        datas.clear();
                        datas.addAll(lists);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    UiUtils.showToast("获取数据失败");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        App.getSharedPreferences().edit().putInt("yichangCount", datas.size()).commit();
        super.onStop();
    }
}
