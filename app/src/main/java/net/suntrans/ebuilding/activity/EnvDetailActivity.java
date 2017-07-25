package net.suntrans.ebuilding.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.bean.SensusEntity;
import net.suntrans.ebuilding.utils.LogUtil;

import static android.R.attr.data;

/**
 * Created by Looney on 2017/7/22.
 */

public class EnvDetailActivity extends BasedActivity{
    LinearLayout rootLL;
    TextView time;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int Pwidth;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_detail);
        rootLL = (LinearLayout) findViewById(R.id.rootLL);
        time = (TextView) findViewById(R.id.time);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);//获取屏幕大小的信息
        Pwidth =displayMetrics.widthPixels;   //屏幕宽度,先锋的宽度是800px，小米2a的宽度是720px
        initView((SensusEntity.SixDetailData) getIntent().getParcelableExtra("info"));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("name"));
        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }


    private void initView(SensusEntity.SixDetailData data) {
        if (data!=null){
//           time.setText();
        }
        for (int i=0;i<rootLL.getChildCount();i++){
            if (i==0||i==1||i==7||i==13)
                continue;
            initView(i,data);
        }

    }

    Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void initView(int position, SensusEntity.SixDetailData data) {


        TextView nameTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.name);
        TextView evaluateTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.evaluate);
        TextView valueTx = (TextView) rootLL.getChildAt(position).findViewById(R.id.value);
        LinearLayout layout_arrow = (LinearLayout) rootLL.getChildAt(position).findViewById(R.id.layout_arrow);
        ImageView standard = (ImageView) rootLL.getChildAt(position).findViewById(R.id.standard);   //等级划分条
        ImageView arrow = (ImageView) rootLL.getChildAt(position).findViewById(R.id.arrow);   //箭头

        switch (position) {
            case 2:
                nameTx.setText("温度");
                standard.setImageResource(R.drawable.standard_tem);
                if (data!=null){
                    valueTx.setText(data.getWendu()+"℃");
                    evaluateTx.setText(data.wenduEva);
                    LogUtil.i("SensusDetaiActivity",data.wenduPro+","+ Pwidth * data.wenduPro / 200);
                    setPading(data.wenduPro,layout_arrow,valueTx);
                }
                break;
            case 3:
                standard.setImageResource(R.drawable.standard_humidity);
                nameTx.setText("湿度");
                if (data!=null){
                    valueTx.setText(data.getShidu()+"%RH");
                    evaluateTx.setText(data.shiduEva);
                    setPading(data.shiduPro,layout_arrow,valueTx);
                }
                break;
            case 4:
                nameTx.setText("大气压");
                standard.setImageResource(R.drawable.standard_humidity);
                if (data!=null){
                    valueTx.setText(data.getDaqiya()+"kPa");
                    evaluateTx.setText(data.daqiYaEva);
                    setPading(data.daqiyaPro,layout_arrow,valueTx);
                }
                break;
            case 5:
                nameTx.setText("人员信息");
                valueTx.setVisibility(View.GONE);
                layout_arrow.setVisibility(View.INVISIBLE);
                if (data!=null){
                    evaluateTx.setText(data.getRenyuan().equals("1")?"有人":"没人");
                }
                break;
            case 6:
                nameTx.setText("光照强度");
                standard.setImageResource(R.drawable.standard_light);
                if (data!=null){
                    valueTx.setText(data.getLight()+"");
                    evaluateTx.setText(data.guanzhaoEva);
                    setPading(data.guanzhaoPro,layout_arrow,valueTx);
                }
                break;
            case 8:
                nameTx.setText("烟雾");
                standard.setImageResource(R.drawable.standard_smoke);
                if (data!=null){
                    valueTx.setText(data.getYanwu()+"ppm");
                    evaluateTx.setText(data.yanwuEva);
                    setPading(data.yanwuPro,layout_arrow,valueTx);
                }
                break;
            case 9:
                nameTx.setText("甲醛");
                standard.setImageResource(R.drawable.standard_smoke);
                if (data!=null){
                    valueTx.setText(data.getJiaquan()+"ppm");
                    evaluateTx.setText(data.jiaquanEva);
                    setPading(data.jiaquanPro,layout_arrow,valueTx);
                }
                break;
            case 10:
                nameTx.setText("PM1");
                standard.setImageResource(R.drawable.bg_standard);
                if (data!=null){
                    valueTx.setText(data.getPM1()+"ppm");
                    evaluateTx.setText(data.pm1Eva);
                    setPading(data.pm1Pro,layout_arrow,valueTx);
                }
                break;
            case 11:
                nameTx.setText("PM2.5");
                standard.setImageResource(R.drawable.bg_standard);
                if (data!=null){
                    valueTx.setText(data.getPm25()+"ppm");
                    evaluateTx.setText(data.pm25Eva);
                    setPading(data.pm25Pro,layout_arrow,valueTx);
                }
                break;
            case 12:
                nameTx.setText("PM10");
                standard.setImageResource(R.drawable.bg_standard);
                if (data!=null){
                    valueTx.setText(data.getPm10()+"ppm");
                    evaluateTx.setText(data.pm10Eva);
                    setPading(data.pm10Pro,layout_arrow,valueTx);
                }
                break;
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
            case 14:
                valueTx.setVisibility(View.GONE);
                layout_arrow.setVisibility(View.INVISIBLE);
                nameTx.setText("水平夹角");
                if (data!=null){
                    evaluateTx.setText(data.zEva);
                }
                break;
            case 17:
//                valueTx.setVisibility(View.GONE);
//                layout_arrow.setVisibility(View.INVISIBLE);
//                nameTx.setText("振动强度");
//                if (data!=null){
//                    evaluateTx.setText(data.ge+"G");
//                }
                break;

        }
    }

    private void setPading(int progress,LinearLayout layout_arrow,TextView value){
        value.setVisibility(View.VISIBLE);
        layout_arrow.setVisibility(View.VISIBLE);
        layout_arrow.setPadding(Pwidth * progress / 200, 0, 0, 0);
        if(progress<50)
        {
            value.setGravity(Gravity.LEFT);
            value.setPadding(Pwidth * progress / 200, 0, 0, 0);   //设置左边距
        }
        else
        {
            value.setGravity(Gravity.RIGHT);
//            System.out.println(Pwidth);
            value.setPadding(0, 0, Pwidth * (90 - progress) / 200, 0);  //设置右边距
        }
    }
}
