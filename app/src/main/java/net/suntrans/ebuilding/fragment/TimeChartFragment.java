package net.suntrans.ebuilding.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import net.suntrans.ebuilding.BasedFragment2;
import net.suntrans.ebuilding.R;
import net.suntrans.ebuilding.adapter.FragmentAdapter;
import net.suntrans.ebuilding.bean.HisEntity;
import net.suntrans.ebuilding.chart.MyMarkerView;
import net.suntrans.ebuilding.databinding.FragmentTimechartBinding;
import net.suntrans.ebuilding.utils.UiUtils;
import net.suntrans.ebuilding.views.CompatDatePickerDialog;
import net.suntrans.stateview.StateView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Looney on 2018/4/16.
 * Des:
 */
public class TimeChartFragment extends BasedFragment2 implements View.OnClickListener {

    private FragmentTimechartBinding binding;
    protected Typeface mTfLight;
    protected Typeface mTfRegular;

    private StateView stateView;


    private int mYear;
    private int mMonth;
    private int mDay;

    private int checkedId;
    private CompatDatePickerDialog pickerDialog;
    private String unit;
    private String paramName;
    private FragmentAdapter adapter;


    public static TimeChartFragment newInstance(String unit, String paramName) {

        Bundle args = new Bundle();
        args.putString("unit", unit);
        args.putString("paramName", paramName);
        TimeChartFragment fragment = new TimeChartFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timechart,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initChart();
    }

    private void initView(){
        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
//                getData(binding.startTime.getText().toString(), binding.endTime.getText().toString());
            }
        });

        unit = getArguments().getString("unit");
        paramName = getArguments().getString("paramName");

        mTfRegular = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.radio0.setChecked(true);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                }
//                setData(data);
            }
        });

        binding.startTime.setOnClickListener(this);
        binding.endTime.setOnClickListener(this);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);




        binding.endTime.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));


        c.add(Calendar.DAY_OF_MONTH,-1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);


        binding.startTime.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));


        binding.query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = binding.startTime.getText().toString();
                String endTime = binding.endTime.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long startTimeLong = 0;
                long endTimeLong = 0;
                try {
                    startTimeLong = sdf.parse(startTime).getTime();
                    endTimeLong = sdf.parse(endTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startTimeLong > endTimeLong) {
                    UiUtils.showToast("起始时间必须小于结束时间");
                    return;
                }
               mListener.getData(startTime, endTime);
            }
        });

        if (paramName != null) {
            String name = paramName;
//            if (name.contains("A")) {
//                name = name.replace("A", "三");
//            }
//            if (name.contains("B")) {
//                name = name.replace("B", "三");
//            }
//            if (name.contains("C")) {
//                name = name.replace("C", "三");
//            }
            binding.unit.setText(name + "(" + unit + ")");
        }
    }



    private void initChart(){

//        mChart.setOnChartGestureListener(this);
//        binding.mChart.setOnChartValueSelectedListener(this);
        binding.mChart.setDrawGridBackground(false);
        binding.mChart.setNoDataText("暂无数据...");

        // no description text
        binding.mChart.getDescription().setEnabled(false);

        // enable touch gestures
        binding.mChart.setTouchEnabled(true);

        // enable scaling and dragging
        binding.mChart.setDragEnabled(true);
        binding.mChart.setScaleEnabled(true);

        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        binding.mChart.setPinchZoom(true);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(binding.mChart); // For bounds control
        binding.mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
//        LimitLine llXAxis = new LimitLine(10f, "Index 10");
//        llXAxis.setLineWidth(4f);
//        llXAxis.enableDashedLine(10f, 10f, 0f);
//        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        llXAxis.setTextSize(10f);

        /**
         * 设置x轴
         */
        XAxis xAxis = binding.mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setAxisMaximum(30);
//        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(true);
        xAxis.setGridLineWidth(1f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");//created_at=2017-10-23 15:30:29

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return mFormat.format(new Date((long) value));
            }
        });
//

        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

//        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

//        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = binding.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
//        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);


        binding.mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
//        setData(data);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

//        binding.mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = binding.mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();


    }



    private boolean threeLine = false;

    public void setData(HisEntity hisEntity) throws ParseException {
        if (hisEntity == null) {
            stateView.showEmpty();
            binding.mainContent.setVisibility(View.INVISIBLE);
            return;
        }
        if (!threeLine) {
            if (hisEntity.data == null) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
            if (hisEntity.data.size() == 0) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
        } else {
            if (hisEntity.data == null) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
            if (hisEntity.data.size() == 0) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
        }


        SimpleDateFormat mFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//created_at=2017-10-23 15:30:29

        if (!threeLine) {
            ArrayList<Entry> values = new ArrayList<Entry>();
            List<String> ls = new ArrayList<>();
            ls.clear();
            values.clear();
            for (int i = hisEntity.data.size() - 1; i >= 0; i--) {
                Date parse = mFormat.parse(hisEntity.data.get(i).GetTime);

                float val = 0f;

                val = Float.parseFloat(hisEntity.data.get(i).Value);
                values.add(new Entry(parse.getTime(), val));
            }

            LineDataSet set1;

            if (binding.mChart.getData() != null &&
                    binding.mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                set1.setLabel(paramName + "(" + unit + ")");

                binding.mChart.getData().notifyDataChanged();
                binding.mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(values, paramName + "(" + unit + ")");

                set1.setDrawCircles(false);

                // set the line to be drawn like this "- - - - - -"
//                set1.enableDashedLine(10f, 5f, 0f);
//                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(getResources().getColor(R.color.colorPrimary));
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(1f);
                set1.setDrawCircles(false);
                set1.setCircleRadius(3f);
                set1.setDrawValues(false);
//            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_liner_chart);
                    set1.setFillDrawable(drawable);
                }
                else {
                    set1.setFillColor(getResources().getColor(R.color.colorPrimary));
                }

//                if (Utils.getSDKInt() >= 18) {
//                    // fill drawable only supported on api level 18 and above
//                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_chart);
//                    set1.setFillDrawable(drawable);
//                } else {
//                    set1.setFillColor(getResources().getColor(R.color.colorPrimary));
//                }

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets

                // create a data object with the datasets
                LineData data = new LineData(dataSets);

                // set data
                binding.mChart.setData(data);
            }
            binding.mChart.invalidate();

        } else {
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();


            String aTitle = "";
//            String bTitle = hisEntity.BInfo.name;
//            String cTitle = hisEntity.CInfo.name;
            for (int i = hisEntity.data.size() - 1; i >= 0; i--) {
                float val = Float.parseFloat(hisEntity.data.get(i).Value);
                Date parse = mFormat.parse(hisEntity.data.get(i).GetTime);
                yVals1.add(new Entry(parse.getTime(), val));
            }

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();

//            for (int i = hisEntity.BInfo.data.size() - 1; i >= 0; i--) {
//                float val = Float.parseFloat(hisEntity.BInfo.data.get(i).data);
//                Date parse = mFormat.parse(hisEntity.BInfo.data.get(i).created_at);
//
//                yVals2.add(new Entry(parse.getTime(), val));
////            if(i == 10) {
////                yVals2.add(new Entry(i, val + 50));
////            }
//            }
//
//            ArrayList<Entry> yVals3 = new ArrayList<Entry>();
//
//            for (int i = hisEntity.CInfo.data.size() - 1; i >= 0; i--) {
//                float val = Float.parseFloat(hisEntity.CInfo.data.get(i).data);
//                Date parse = mFormat.parse(hisEntity.CInfo.data.get(i).created_at);
//
//                yVals3.add(new Entry(parse.getTime(), val));
//            }

            LineDataSet set1, set2, set3;

            if (binding.mChart.getData() != null &&
                    binding.mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
//                set2 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(1);
//                set3 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(2);
                set1.setValues(yVals1);
//                set2.setValues(yVals2);
//                set3.setValues(yVals3);
                binding.mChart.getData().notifyDataChanged();
                binding.mChart.notifyDataSetChanged();
                binding.mChart.invalidate();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(yVals1, aTitle);

//                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(getResources().getColor(R.color.colorPrimary));
                set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
                set1.setHighLightColor(getResources().getColor(R.color.colorPrimary));
                set1.setLineWidth(2f);
                set1.setDrawCircles(false);
//                set1.setCircleRadius(3f);
//                set1.setFillAlpha(65);
                set1.setDrawFilled(true);

                set1.setDrawCircleHole(false);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_liner_chart);
                    set1.setFillDrawable(drawable);
                }
                else {
                    set1.setFillColor(getResources().getColor(R.color.colorPrimary));
                }

                //set1.setFillFormatter(new MyFillFormatter(0f));
                //set1.setDrawHorizontalHighlightIndicator(false);
                //set1.setVisible(false);
                //set1.setCircleHoleColor(Color.WHITE);

                // create a dataset and give it a type
//                set2 = new LineDataSet(yVals2, bTitle);
////                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//                set2.setColor(Color.GREEN);
//                set2.setCircleColor(Color.GREEN);
//                set2.setHighLightColor(Color.GREEN);
//                set2.setLineWidth(1f);
////                set2.setCircleRadius(3f);
//                set2.setDrawCircles(false);
////                set2.setFillAlpha(65);
//                set2.setDrawCircleHole(false);
//                //set2.setFillFormatter(new MyFillFormatter(900f));
//
//                set3 = new LineDataSet(yVals3, cTitle);
////                set3.setAxisDependency(YAxis.AxisDependency.LEFT);
//                set3.setColor(Color.RED);
//                set3.setCircleColor(Color.RED);
//                set3.setHighLightColor(Color.RED);
//                set3.setDrawCircles(false);
//                set3.setLineWidth(1f);
////                set3.setCircleRadius(3f);
////                set3.setFillAlpha(65);
////                set3.setFillColor(ColorTemplate.colorWithAlpha(Color.RED, 200));
//                set3.setDrawCircleHole(false);

                // create a data object with the datasets
                LineData data = new LineData(set1);
                data.setValueTextColor(Color.WHITE);
                data.setValueTextSize(9f);

                // set data
                binding.mChart.setData(data);

            }

//            MyAxisValueFormatter formatter = new MyAxisValueFormatter(ls);
//            binding.mChart.getXAxis().setValueFormatter(formatter);
        }
        setRecyclerViewDatas(hisEntity);
//        binding.mChart.invalidate();
        stateView.showContent();
        binding.mainContent.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        checkedId = v.getId();
        pickerDialog = new CompatDatePickerDialog(getContext(), mDateSetListener, mYear, mMonth - 1, mDay);
        DatePicker datePicker = pickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());

        pickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        String startTime = binding.startTime.getText().toString();
        String endTime = binding.endTime.getText().toString();
        mListener.getData(startTime,endTime);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void getData(String startTime, String endTime);
    }

    private void setRecyclerViewDatas(HisEntity hisEntity) {
        if (hisEntity == null) {
            return;
        }

        if (adapter == null && binding.viewpager.getAdapter() == null) {
            adapter = new FragmentAdapter(getChildFragmentManager());
            if (!threeLine) {
                ZhCurHisItemFragment fragment = ZhCurHisItemFragment.newInstance((ArrayList<HisEntity.EleParmHisItem>) hisEntity.data);
                adapter.addFragment(fragment, paramName);
            } else {

            }
            binding.viewpager.setOffscreenPageLimit(3);
            binding.viewpager.setAdapter(adapter);
            binding.tabLayout.setupWithViewPager(binding.viewpager);
        } else {
            ZhCurHisItemFragment fragment1 = (ZhCurHisItemFragment) adapter.getItem(0);

            fragment1.setData(hisEntity.data);


        }

    }

    private CompatDatePickerDialog.OnDateSetListener mDateSetListener =
            new CompatDatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    if (checkedId == R.id.startTime) {
                        binding.startTime.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth)).append("-")
                                        .append(pad(mDay))
                        );
                    } else {
                        binding.endTime.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth)).append("-")
                                        .append(pad(mDay))
                        );
                    }


                }
            };
}
