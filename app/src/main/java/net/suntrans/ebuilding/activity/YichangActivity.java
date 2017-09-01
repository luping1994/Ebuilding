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
    private List<YichangEntity.DataBeanX.ListsBean.DataBean> datas;
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
                getdata(fristLoad);
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
        final ItemTouchHelper touchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
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
                delete(datas.get(pos).log_id);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (currentPage>totalPage){
                    adapter.loadMoreEnd();
                    return;
                }
                getdata(loadMore);
            }
        }, recyclerView);
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
                if (o.getCode() == 200) {
                    UiUtils.showToast("删除成功!");
                } else {
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

    class MyAdapter extends BaseItemDraggableAdapter<YichangEntity.DataBeanX.ListsBean.DataBean, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<YichangEntity.DataBeanX.ListsBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, YichangEntity.DataBeanX.ListsBean.DataBean item) {
            helper.setText(R.id.msg, "" + item.name + ",异常类型:" + item.message)
                    .setText(R.id.time, item.updated_at);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata(fristLoad);
    }

    private int currentPage = 1;
    private int fristLoad = 0;
    private int loadMore = 2;

    private int totalPage=0;
    private void getdata(final int loadtype) {
        if (loadtype == fristLoad){
            recyclerView.setVisibility(View.INVISIBLE);
            stateView.showLoading();
        }
        addSubscription(RetrofitHelper.getApi().getYichang(currentPage + ""), new Subscriber<YichangEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                UiUtils.showToast("服务器错误");

                if (loadtype == loadMore)
                    adapter.loadMoreFail();
                else{
                    recyclerView.setVisibility(View.INVISIBLE);
                    stateView.showRetry();
                }
            }

            @Override
            public void onNext(YichangEntity o) {
                if (o.code == 200) {
                    List<YichangEntity.DataBeanX.ListsBean.DataBean> lists = o.data.lists.data;
                    if (lists == null || lists.size() == 0) {
                        if (loadtype==fristLoad){
                            stateView.showEmpty();
                            recyclerView.setVisibility(View.INVISIBLE);
                        }else {
                            adapter.loadMoreFail();

                        }

                    } else {
                        if (loadtype == loadMore) {
                            adapter.loadMoreComplete();
                        } else {

                        }
                        totalPage  =o.data.lists.total/o.data.lists.per_page+1;
                        currentPage++;
                        stateView.showContent();
                        recyclerView.setVisibility(View.VISIBLE);
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
