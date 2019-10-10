package cn.sloth.com.timepool.data;

import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.sloth.com.timepool.R;

/**
 * @author nigal
 */
public class BannerTimeProvider {

    private static BannerTimeProvider ins;

    public static BannerTimeProvider getIns(Context context){
        if( ins == null ){
            ins = new BannerTimeProvider(context);
        }
        return ins;
    }

    private SparseArray<Date> bannerDate;
    private Calendar calendar;
    private String[] weeks;

    private BannerTimeProvider(Context context) {
        bannerDate = new SparseArray<>();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:OO"));

        bannerDate = new SparseArray<>(7);

        int dayPerWeek = 7;
        for(int i = 0; i < dayPerWeek; i++){
            bannerDate.append(i, calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        weeks = context.getResources().getStringArray(R.array.weeks);
    }

    public SparseArray<Date> getBannerDate(){
        return bannerDate;
    }

    public String getDayOfWeek(Date date){
        calendar.setTime(date);
        return dayOfWeek( calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    private String dayOfWeek(int dayIndex){
        if(weeks == null || weeks.length < dayIndex){
            return "";
        }
        return weeks[dayIndex];
    }
}
