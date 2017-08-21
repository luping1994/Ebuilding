package net.suntrans.ebuilding.fragment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.trello.rxlifecycle.android.FragmentEvent
import net.suntrans.ebuilding.R
import net.suntrans.ebuilding.activity.AddAreaActivity
import net.suntrans.ebuilding.activity.AddFloorActivity
import net.suntrans.ebuilding.activity.AreaDetailActivity
import net.suntrans.ebuilding.adapter.AreaAdapter
import net.suntrans.ebuilding.api.RetrofitHelper
import net.suntrans.ebuilding.bean.AreaEntity
import net.suntrans.ebuilding.bean.SampleResult
import net.suntrans.ebuilding.fragment.base.BasedFragment
import net.suntrans.ebuilding.utils.LogUtil
import net.suntrans.ebuilding.utils.StatusBarCompat
import net.suntrans.ebuilding.utils.UiUtils
import net.suntrans.ebuilding.views.ScrollChildSwipeRefreshLayout
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by Looney on 2017/7/20.
 */

class AreaFragment : BasedFragment() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_area
    }

    private var datas: MutableList<AreaEntity.AreaFloor>? = null
    private var adapter: AreaAdapter? = null
    private var expandableListView: ExpandableListView? = null
    private var add: ImageView? = null
    private var refreshLayout: ScrollChildSwipeRefreshLayout? = null
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val statusBar = view!!.findViewById(R.id.statusbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val statusBarHeight = StatusBarCompat.getStatusBarHeight(context)
            val params = statusBar.layoutParams as LinearLayout.LayoutParams
            params.height = statusBarHeight
            statusBar.layoutParams = params
            statusBar.visibility = View.VISIBLE
        } else {
            statusBar.visibility = View.GONE

        }
        refreshLayout = view.findViewById(R.id.refreshlayout) as ScrollChildSwipeRefreshLayout
        refreshLayout?.setOnRefreshListener { getAreaData(1) }

        datas = ArrayList<AreaEntity.AreaFloor>()
        expandableListView = view!!.findViewById(R.id.recyclerview) as ExpandableListView
        adapter = AreaAdapter(datas, context)
        expandableListView!!.setAdapter(adapter)
        expandableListView!!.divider = null
        add = view!!.findViewById(R.id.add) as ImageView
        add!!.setOnClickListener { v -> showPopupMenu() }
        expandableListView!!.setOnItemLongClickListener { parent, view, position, id ->
            if (view.getTag(R.id.name) is AreaAdapter.GroupHolder){

                println("我被长按了,"+view.getTag(R.id.root))
                deleteFloor(datas!!.get(view.getTag(R.id.root) as Int).id)
            }

            true
        }
        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val intent = Intent(activity, AreaDetailActivity::class.java)
            intent.putExtra("id", datas!![groupPosition].sub[childPosition].id.toString() + "")
            intent.putExtra("name", datas!![groupPosition].sub[childPosition].name)
            startActivity(intent)
            activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter, android.support.v7.appcompat.R.anim.abc_popup_exit)
            true
        }
        stateView.setEmptyResource(R.layout.base_empty)
        stateView.setLoadingResource(R.layout.base_loading)
        stateView.setRetryResource(R.layout.base_retry)

        super.onViewCreated(view, savedInstanceState)
    }


    private fun getAreaData(a: Int) {
        if (a == 0)
            stateView.showLoading()
        RetrofitHelper.getApi().homeHouse
                .compose(this.bindUntilEvent<AreaEntity>(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<AreaEntity>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        stateView.showRetry()
                        refreshLayout?.isRefreshing = false

                    }

                    override fun onNext(homeSceneResult: AreaEntity?) {
                        LogUtil.i("房间获取成功！")
                        refreshLayout?.isRefreshing = false
                        if (homeSceneResult != null) {
                            if (homeSceneResult.code == 200) {
                                if (homeSceneResult.data.lists == null || homeSceneResult.data.lists.size == 0) {
                                    stateView.showEmpty()
                                    return
                                }
                                datas!!.clear()
                                datas!!.addAll(homeSceneResult.data.lists)
                                adapter!!.notifyDataSetChanged()
                                expandableListView!!.expandGroup(0, true)
                                if (a == 0)
                                    stateView.showContent()
                            } else {
                                stateView.showRetry()
                            }
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getAreaData(0)
    }

    override fun onFragmentFirstVisible() {

    }

    override fun onRetryClick() {
        getAreaData(0)
    }

    private var mPopupWindow: PopupWindow? = null
    private fun showPopupMenu() {
        if (mPopupWindow == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_area_menu, null)
            val ll = view.findViewById(R.id.content)
            ViewCompat.setElevation(ll, 20f)
            view.findViewById(R.id.addFloor).setOnClickListener {
                val intent = Intent(activity, AddFloorActivity::class.java)
                intent.putExtra("type", "addFloor")
                startActivity(intent)
                mPopupWindow!!.dismiss()
            }
            view.findViewById(R.id.addArea).setOnClickListener {
                val intent = Intent(activity, AddAreaActivity::class.java)
                intent.putExtra("type", "addFloor")
                startActivity(intent)
                mPopupWindow!!.dismiss()
            }
            mPopupWindow = PopupWindow(context)
            mPopupWindow!!.contentView = view
            mPopupWindow!!.height = UiUtils.dip2px(120)
            mPopupWindow!!.width = UiUtils.dip2px(155)
            mPopupWindow!!.animationStyle = R.style.TRM_ANIM_STYLE
            mPopupWindow!!.isFocusable = true
            mPopupWindow!!.isOutsideTouchable = true
            mPopupWindow!!.setBackgroundDrawable(ColorDrawable())
            mPopupWindow!!.setOnDismissListener {
                //                    setBackgroundAlpha(0.75f, 1f, 300);
            }
        }

        if (!mPopupWindow!!.isShowing) {
            val width = UiUtils.getDisplaySize(context)[0]
            mPopupWindow!!.showAtLocation(add, Gravity.NO_GRAVITY, width, UiUtils.dip2px(24))

        }

    }

    private fun deleteFloor(id: Int) {
        AlertDialog.Builder(context)
                .setMessage("是否删除该楼层?")
                .setPositiveButton("确定") { dialog, which -> delete(id) }.setNegativeButton("取消", null).create().show()

    }

    private fun delete(id: Int) {
        RetrofitHelper.getApi().deleteFloor(id.toString() + "")
                .compose(this.bindToLifecycle<SampleResult>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<SampleResult>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        UiUtils.showToast(e.message)
                    }

                    override fun onNext(result: SampleResult) {
                        if (result.code == 200) {
                            AlertDialog.Builder(context)
                                    .setPositiveButton("确定") { dialog, which -> getAreaData(1) }.setMessage("删除成功")
                                    .create().show()

                        } else {
                            UiUtils.showToast("删除失败")
                        }
                    }
                })
    }


}
