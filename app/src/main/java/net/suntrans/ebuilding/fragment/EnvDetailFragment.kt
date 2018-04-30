package net.suntrans.ebuilding.fragment

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import net.suntrans.ebuilding.BasedFragment2
import net.suntrans.ebuilding.R
import net.suntrans.ebuilding.activity.EnvHisActivity
import net.suntrans.ebuilding.bean.EnvDetailEntity
import net.suntrans.ebuilding.bean.SensusEntity
import net.suntrans.ebuilding.databinding.FragmentEnvDetailBinding
import net.suntrans.ebuilding.rx.BaseSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by Looney on 2018/4/2.
 * Des:
 */
class EnvDetailFragment : BasedFragment2(), View.OnClickListener {
    override fun onClick(v: View?) {
        val field = v!!.getTag(v.id) as HashMap<String, String>

        val intent = Intent(activity, EnvHisActivity::class.java)
        intent.putExtra("field", field["field"])
        intent.putExtra("name", field["name"])
        intent.putExtra("unit", field["unit"])
        intent.putExtra("house_id", house_id)
        startActivity(intent)
    }

    companion object {

        fun newInstance(house_id: String): EnvDetailFragment {

            val args = Bundle()
            args.putString("house_id", house_id)
            val fragment = EnvDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var binding: FragmentEnvDetailBinding? = null
    private var Pwidth: Int = 0
    private val displayMetrics = DisplayMetrics()
    private var house_id: String = "0"
    private var dev_id = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_env_detail, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)//获取屏幕大小的信息
        Pwidth = displayMetrics.widthPixels   //屏幕宽度,先锋的宽度是800px，小米2a的宽度是720px
        initView(null)
        house_id = arguments.getString("house_id")
        binding!!.refreshlayout.setColorSchemeColors(activity.resources.getColor(R.color.colorPrimary))
        binding!!.refreshlayout.setOnRefreshListener {
            getData(house_id)
            handler.postDelayed({ binding!!.refreshlayout.isRefreshing = false }, 2000)
        }

        initData()
    }


    private fun initData() {
        val map = HashMap<String, String>()
        map["name"] = "温度"
        map["field"] = "wendu"
        map["unit"] = "℃"
        binding!!.rootLL.getChildAt(2).setTag(R.id.r1, map)

        val map2 = HashMap<String, String>()
        map2["name"] = "湿度"
        map2["field"] = "shidu"
        map2["unit"] = "%RH"
        binding!!.rootLL.getChildAt(3).setTag(R.id.r2, map2)

        val map5 = HashMap<String, String>()
        map5["name"] = "烟雾"
        map5["field"] = "yanwu"
        map5["unit"] = "ug/m³"
        binding!!.rootLL.getChildAt(7).setTag(R.id.r5, map5)

        val map6 = HashMap<String, String>()
        map6["name"] = "甲醛"
        map6["field"] = "jiaquan"
        map6["unit"] = "ug/m³"
        binding!!.rootLL.getChildAt(8).setTag(R.id.r6, map6)

        val map7 = HashMap<String, String>()
        map7["name"] = "PM1"
        map7["field"] = "pm1"
        map7["unit"] = "ug/m³"
        binding!!.rootLL.getChildAt(9).setTag(R.id.r7, map7)

        val map8 = HashMap<String, String>()
        map8["name"] = "PM2.5"
        map8["field"] = "pm25"
        map8["unit"] = "ug/m³"
        binding!!.rootLL.getChildAt(10).setTag(R.id.r8, map8)

        val map9 = HashMap<String, String>()
        map9["name"] = "PM10"
        map9["field"] = "pm10"
        map9["unit"] = "ug/m³"
        binding!!.rootLL.getChildAt(11).setTag(R.id.r9, map9)

        val map10 = HashMap<String, String>()
        map10["name"] = "建筑姿态"
        map10["field"] = "z_zhou"
        map10["unit"] = ""
        binding!!.rootLL.getChildAt(13).setTag(R.id.r10, map10)


        binding!!.rootLL.getChildAt(2).setOnClickListener(this)
//        binding!!.r3.setOnClickListener(this)
//        binding!!.r4.setOnClickListener(this)
        binding!!.rootLL.getChildAt(3).setOnClickListener(this)
        binding!!.rootLL.getChildAt(7).setOnClickListener(this)
        binding!!.rootLL.getChildAt(8).setOnClickListener(this)
        binding!!.rootLL.getChildAt(9).setOnClickListener(this)
        binding!!.rootLL.getChildAt(10).setOnClickListener(this)
        binding!!.rootLL.getChildAt(11).setOnClickListener(this)
        binding!!.rootLL.getChildAt(13).setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        getData(house_id)
    }

    private fun getData(house_id: String) {
        if (TextUtils.isEmpty(house_id)){
            return
        }
        mCompositeSubscription.add(api.getEnvDetail(house_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<EnvDetailEntity>(activity) {
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        e.printStackTrace();
                        binding!!.refreshlayout?.isRefreshing = false
                    }

                    override fun onNext(info: EnvDetailEntity) {
                        if (info?.data != null)
                            info.data.list.setEva()

                        initView(info.data.list)

                        dev_id = info.data.list.dev_id

                        binding!!.refreshlayout?.isRefreshing = false
                    }
                }))

    }

    public fun getDevID(): String {
        return dev_id
    }

    private fun initView(data: SensusEntity.SixDetailData?) {
        if (data != null) {
            binding!!.time.text = data.updated_at
        }
        for (i in 0 until binding!!.rootLL.childCount) {
            if (i == 0 || i == 1 || i == 6 || i == 12)
                continue
            initView(i, data)
        }

    }


    private fun initView(position: Int, data: SensusEntity.SixDetailData?) {
        val nameTx = binding!!.rootLL.getChildAt(position).findViewById(R.id.name) as TextView
        val evaluateTx = binding!!.rootLL.getChildAt(position).findViewById(R.id.evaluate) as TextView
        val valueTx = binding!!.rootLL.getChildAt(position).findViewById(R.id.value) as TextView
        val layout_arrow = binding!!.rootLL.getChildAt(position).findViewById(R.id.layout_arrow) as LinearLayout
        val standard = binding!!.rootLL.getChildAt(position).findViewById(R.id.standard) as ImageView   //等级划分条
        val arrow = binding!!.rootLL.getChildAt(position).findViewById(R.id.arrow) as ImageView   //箭头

        when (position) {
            2 -> {
                nameTx.text = "温度"
                standard.setImageResource(R.drawable.ic_wendu_progress)
                if (data != null) {
                    valueTx.text = data.wendu + "℃"
                    evaluateTx.text = data.wenduEva
                    setPading(data.wenduPro, layout_arrow, valueTx)
                }
            }
            3 -> {
                standard.setImageResource(R.drawable.ic_shidu_progress)
                nameTx.text = "湿度"
                if (data != null) {
                    valueTx.text = data.shidu + "%RH"
                    evaluateTx.text = data.shiduEva
                    setPading(data.shiduPro, layout_arrow, valueTx)
                }
            }
            4 -> {
                nameTx.text = "大气压"
                standard.setImageResource(R.drawable.ic_daqiya_progress)
                if (data != null) {
                    valueTx.text = data.daqiya + "kPa"
                    evaluateTx.text = data.daqiYaEva
                    setPading(data.daqiyaPro, layout_arrow, valueTx)
                }
            }
        //            case 5:
        //                nameTx.setText("人员信息");
        //                valueTx.setVisibility(View.GONE);
        //                layout_arrow.setVisibility(View.INVISIBLE);
        //                if (data != null) {
        //                    evaluateTx.setText(data.getRenyuan().equals("1") ? "有人" : "没人");
        //                }
        //                break;
            5 -> {
                nameTx.text = "光照强度"
                standard.setImageResource(R.drawable.ic_guanzhao_progress)
                if (data != null) {
                    valueTx.text = data.light + ""
                    evaluateTx.text = data.guanzhaoEva
                    setPading(data.guanzhaoPro, layout_arrow, valueTx)
                }
            }
            7 -> {
                nameTx.text = "烟雾"
                standard.setImageResource(R.drawable.ic_pm_progress)
                if (data != null) {
                    valueTx.text = data.yanwu + "级"
                    evaluateTx.text = data.yanwuEva
                    setPading(data.yanwuPro, layout_arrow, valueTx)
                }
            }
            8 -> {
                nameTx.text = "甲醛"
                standard.setImageResource(R.drawable.ic_pm_progress)
                if (data != null) {
                    valueTx.text = data.jiaquan + "ug/m³"
                    evaluateTx.text = data.jiaquanEva
                    setPading(data.jiaquanPro, layout_arrow, valueTx)
                }
            }
            9 -> {
                nameTx.text = "PM1"
                standard.setImageResource(R.drawable.ic_pm_progress)
                if (data != null) {
                    valueTx.text = data.pM1 + "ug/m³"
                    evaluateTx.text = data.pm1Eva
                    setPading(data.pm1Pro, layout_arrow, valueTx)
                }
            }
            10 -> {
                nameTx.text = "PM2.5"
                standard.setImageResource(R.drawable.ic_pm_progress)
                if (data != null) {
                    valueTx.text = data.pm25 + "ug/m³"
                    evaluateTx.text = data.pm25Eva
                    setPading(data.pm25Pro, layout_arrow, valueTx)
                }
            }
            11 -> {
                nameTx.text = "PM10"
                standard.setImageResource(R.drawable.ic_pm_progress)
                if (data != null) {
                    valueTx.text = data.pm10 + "ug/m³"
                    evaluateTx.text = data.pm10Eva
                    setPading(data.pm10Pro, layout_arrow, valueTx)
                }
            }
        //            case 14:
        //                nameTx.setText("X轴角度");
        //                valueTx.setVisibility(View.GONE);
        //                layout_arrow.setVisibility(View.INVISIBLE);
        //                if (data!=null){
        //                    evaluateTx.setText(data.xEva);
        //                }
        //                break;
        //            case 15:
        //                valueTx.setVisibility(View.GONE);
        //                layout_arrow.setVisibility(View.INVISIBLE);
        //                nameTx.setText("Y轴角度");
        //                if (data!=null){
        //                    evaluateTx.setText(data.yEva);
        //                }
        //                break;
            13 -> {
                valueTx.visibility = View.GONE
                layout_arrow.visibility = View.GONE
                standard.visibility = View.GONE
                nameTx.text = "建筑姿态"
                if (data != null) {
                    valueTx.text = data.offset + "°"
                    evaluateTx.text = data.zEva
                }
            }
            16 -> {
            }
        }//                valueTx.setVisibility(View.GONE);
        //                layout_arrow.setVisibility(View.INVISIBLE);
        //                nameTx.setText("振动强度");
        //                if (data!=null){
        //                    evaluateTx.setText(data.ge+"G");
        //                }
    }

    private fun setPading(progress: Int, layout_arrow: LinearLayout, value: TextView) {
        value.visibility = View.VISIBLE
        layout_arrow.visibility = View.VISIBLE
        layout_arrow.setPadding(Pwidth * progress / 200, 0, 0, 0)
        if (progress < 50) {
            value.gravity = Gravity.LEFT
            value.setPadding(Pwidth * progress / 200, 0, 0, 0)   //设置左边距
        } else {
            value.gravity = Gravity.RIGHT
            //            System.out.println(Pwidth);
            value.setPadding(0, 0, Pwidth * (90 - progress) / 200, 0)  //设置右边距
        }
    }

    private var handler = Handler()
    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }


}
