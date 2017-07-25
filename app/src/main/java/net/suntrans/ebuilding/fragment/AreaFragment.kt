package net.suntrans.ebuilding.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView

import com.trello.rxlifecycle.android.FragmentEvent
import com.trello.rxlifecycle.components.support.RxFragment

import net.suntrans.ebuilding.R
import net.suntrans.ebuilding.activity.AreaDetailActivity
import net.suntrans.ebuilding.adapter.AreaAdapter
import net.suntrans.ebuilding.api.RetrofitHelper
import net.suntrans.ebuilding.bean.AreaEntity
import net.suntrans.ebuilding.utils.LogUtil

import java.util.ArrayList

import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Looney on 2017/7/20.
 */

class AreaFragment : RxFragment() {
    private val refreshLayout: SwipeRefreshLayout? = null
    private var datas: MutableList<AreaEntity.AreaFloor>? = null
    private var adapter: AreaAdapter? = null
    private var expandableListView: ExpandableListView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_area, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        datas = ArrayList<AreaEntity.AreaFloor>()
        expandableListView = view!!.findViewById(R.id.recyclerview) as ExpandableListView
        adapter = AreaAdapter(datas, context)
        expandableListView!!.setAdapter(adapter)
        expandableListView!!.divider = null
        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val intent = Intent(activity, AreaDetailActivity::class.java)
            intent.putExtra("id", datas!![groupPosition].sub[childPosition].id.toString() + "")
            intent.putExtra("name", datas!![groupPosition].sub[childPosition].name)
            startActivity(intent)
            activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter, android.support.v7.appcompat.R.anim.abc_popup_exit)
            true
        }
    }

    private fun getAreaData() {
        RetrofitHelper.getApi().homeHouse
                .compose(this.bindUntilEvent<AreaEntity>(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<AreaEntity>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()

                    }

                    override fun onNext(homeSceneResult: AreaEntity?) {
                        LogUtil.i("房间获取成功！")
                        if (homeSceneResult != null) {
                            if (homeSceneResult.code == 200) {
                                datas!!.clear()
                                datas!!.addAll(homeSceneResult.data.lists)
                                adapter!!.notifyDataSetChanged()
                                expandableListView!!.expandGroup(0, true)
                            } else {

                            }
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getAreaData()
    }

}
