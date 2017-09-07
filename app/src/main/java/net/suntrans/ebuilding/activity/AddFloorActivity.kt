package net.suntrans.ebuilding.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.trello.rxlifecycle.android.ActivityEvent
import net.suntrans.ebuilding.R
import net.suntrans.ebuilding.api.RetrofitHelper
import net.suntrans.ebuilding.bean.SampleResult
import net.suntrans.ebuilding.rx.BaseSubscriber
import net.suntrans.ebuilding.utils.UiUtils
import net.suntrans.ebuilding.views.LoadingDialog
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.HashMap

class AddFloorActivity : BasedActivity(), DialogInterface.OnDismissListener {
    override fun onDismiss(dialog: DialogInterface?) {
        subscribe?.unsubscribe()
    }

    private var dialog: LoadingDialog? = null
    private var subscribe: Subscription? = null
    private var name: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_area)
        setUpToolBar()
        name = findViewById(R.id.name) as TextView
    }

    private fun setUpToolBar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        var type = intent.getStringExtra("type")
        if (type.equals("addFloor"))
            toolbar.title = "添加楼层"
        else if (type.equals("addArea"))
            toolbar.title = "添加区域"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun addFloor(v: View) {
        val name1 = name!!.getText().toString()
        if (TextUtils.isEmpty(name1)) {
            UiUtils.showToast("请输入场景名称")
            return
        }

        if (dialog == null) {
            dialog = LoadingDialog(this)
            dialog?.setWaitText("请稍后")
            dialog?.setOnDismissListener(this)
        }
        dialog?.show()
        val map = HashMap<String, String>()
        map.put("name", name1.replace(" ",""))
        map.put("imgurl", "")
        map.put("show_sort", "1")
        subscribe = RetrofitHelper.getApi().addFloor(map)
                .compose(this.bindUntilEvent<SampleResult>(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : BaseSubscriber<SampleResult>(this) {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        dialog?.dismiss()
                        e.printStackTrace()
                    }

                    override fun onNext(addResult: SampleResult) {
                        dialog?.dismiss()
                        if (addResult.code == 200) {
                            UiUtils.showToast("添加成功")
                            finish()
                        } else {
                            UiUtils.showToast(addResult.msg)
                        }

                    }
                })
    }
}
