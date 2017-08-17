package net.suntrans.ebuilding.fragment.din;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import net.suntrans.ebuilding.App;
import net.suntrans.ebuilding.MainActivity;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.activity.SceneDetailActivity;
import net.suntrans.ebuilding.api.RetrofitHelper;
import net.suntrans.ebuilding.bean.SampleResult;
import net.suntrans.ebuilding.bean.SceneEntity;
import net.suntrans.ebuilding.bean.UserInfo;
import net.suntrans.ebuilding.utils.LogUtil;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.LoadingDialog;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.suntrans.ebuilding.utils.UiUtils.getContext;

/**
 * Created by Looney on 2017/7/20.
 */

public class SceneFragment extends RxFragment {

    private RecyclerView recyclerView;
    private List<SceneEntity.Scene> datas;
    private SceneAdapter adapter;
    private Observable<SceneEntity> getDataOb;
    private StateView stateView;
    private String[] items2 = {"更改名称", "更换头像"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        stateView = StateView.inject(view,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getSceneData();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SceneAdapter(R.layout.item_scene, datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
                intent.putExtra("title", datas.get(position).name);
                intent.putExtra("imgurl", datas.get(position).img_url);
                intent.putExtra("id", datas.get(position).id);
                startActivity(intent);
            }
        });
    }




    private class SceneAdapter extends BaseQuickAdapter<SceneEntity.Scene, BaseViewHolder> {

        public SceneAdapter(@LayoutRes int layoutResId, @Nullable List<SceneEntity.Scene> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SceneEntity.Scene item) {
            helper.setText(R.id.sceneName,item.name)
                    .setText(R.id.sceneNameEn,item.name_en);
            ImageView imageView = helper.getView(R.id.sceneBg);
            Glide.with(getActivity())
                    .load(item.img_url)
                    .centerCrop()
                    .placeholder(R.drawable.bg_scene)
                    .into(imageView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSceneData();
    }

    private void getSceneData() {
        stateView.showLoading();
        if (getDataOb == null)
            getDataOb = RetrofitHelper.getApi().getHomeScene()
                    .compose(this.<SceneEntity>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        getDataOb.subscribe(new Subscriber<SceneEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                stateView.showRetry();
            }

            @Override
            public void onNext(SceneEntity result) {
                if (result.data.lists==null||result.data.lists.size()==0){
                    stateView.showEmpty();
                    return;
                }
                stateView.showContent();
                datas.clear();
                datas.addAll(result.data.lists);
                adapter.notifyDataSetChanged();
            }
        });
    }




}
