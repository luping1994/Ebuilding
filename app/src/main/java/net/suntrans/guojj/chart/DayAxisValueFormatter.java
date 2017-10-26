package net.suntrans.guojj.chart;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    public static int DAYS = 1;
    public static int MONTHS = 2;
    public static int YEARS = 3;
    private int mType;

    protected String[] mMonths = new String[]{
            "1日", "2日", "3日", "4日", "5日", "6日",
            "7日", "8日", "9日", "10日", "11日", "12日",
            "13日", "14日", "15日", "16日", "17日", "18日",
            "19日", "20日", "21日", "22日", "23日", "24日",
            "25日", "26", "27日", "28日", "23日", "29日",
            "30日", "31日"
    };
    protected String[] mDays = new String[]{
            "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"
    };
    protected String[] mYears = new String[]{
            "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, int type) {
        this.chart = chart;
        this.mType = type;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return value+"度";

    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            int x400 = month % 400;
            if (x400 < 0) {
                x400 = -x400;
            }
            boolean is29 = (month % 4) == 0 && x400 != 100 && x400 != 200 && x400 != 300;

            return is29 ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }

    private int determineYear(int days) {

        if (days <= 366)
            return 2016;
        else if (days <= 730)
            return 2017;
        else if (days <= 1094)
            return 2018;
        else if (days <= 1458)
            return 2019;
        else
            return 2020;

    }

//    @Override
//    public int getDecimalDigits() {
//        return 0;
//    }
}
