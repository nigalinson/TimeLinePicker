package cn.sloth.com.timepool.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import java.util.Calendar;
import java.util.Date;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.sloth.com.timepool.R;
import cn.sloth.com.timepool.adapter.BannerTimeAdapter;
import cn.sloth.com.timepool.data.DayLine;

/**
 * timeline chooser view
 * @author nigal
 * @date 2019/10/9
 */
public class ChooserView extends LinearLayout implements BannerTimeAdapter.BannerChooseCallback{

    private RecyclerView banner;

    private BannerTimeAdapter bannerTimeAdapter;

    private TimePool timePool;
    private RangePicker rangePicker;
    private TimePoolZone timePoolZone;
    private ChooseDateListener chooseDateListener;
    private SparseArray<DayLine> weekLines = new SparseArray<>(7);

    public ChooserView(Context context) {
        super(context);
        init(context, null);
    }

    public ChooserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChooserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        View view = View.inflate(context, R.layout.layout_appointment_time_chooser, null);
        addView(view);
        banner = view.findViewById(R.id.banner);
        banner.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        bannerTimeAdapter = new BannerTimeAdapter(context, this);
        banner.setAdapter(bannerTimeAdapter);

        timePool = view.findViewById(R.id.time_pool);
        rangePicker = view.findViewById(R.id.range_picker);
        timePoolZone = view.findViewById(R.id.time_pool_zone);

        int zoomChoosenColor = ContextCompat.getColor(context, R.color.choosen);
        int zoomOutDateColor = ContextCompat.getColor(context, R.color.out_date);

        timePoolZone.setChoosenColor(zoomChoosenColor);
        timePoolZone.setOutDateColor(zoomOutDateColor);

        int pickerColor = ContextCompat.getColor(context, R.color.time_picker_flipper_color);
        int pickerTextColor = ContextCompat.getColor(context, R.color.time_picker_flipper_normal_text_color);
        int pickerMessColor = ContextCompat.getColor(context, R.color.time_picker_flipper_color_wrong);
        int pickerMessTextColor = ContextCompat.getColor(context, R.color.time_picker_flipper_text_size);

        rangePicker.setPickerColor(pickerColor);
        rangePicker.setPickerNormalTextColor(pickerTextColor);
        rangePicker.setPickerMessColor(pickerMessColor);
        rangePicker.setPickerMessTextColor(pickerMessTextColor);

        rangePicker.setRangingListener(new RangePicker.TouchRangingListener() {
            @Override
            public void inTouchRanging(boolean bCenter) {
                if(timePool != null){
                    timePool.setCanScroll(!bCenter);
                }
            }
        });

        rangePicker.setChooseCubesListener(new RangePicker.ChooseCubesListener() {
            @Override
            public void collideCubes(int topIndex, int bottomIndex) {
                rangePicker.setInMess(timePoolZone.inMess(topIndex, bottomIndex));
            }

            @Override
            public void chooseCubes(int topIndex, int bottomIndex) {
                if(chooseDateListener != null){
                    chooseDateListener.chooseDate(rangePicker.isInMess(), bannerTimeAdapter.getChooseDate(), topIndex, bottomIndex, bannerTimeAdapter.getChoosenIndex());
                }
            }
        });

        final ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                timePoolZone.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bannerChoose(0);
            }
        };

        timePoolZone.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        initDeclaredStyles(context, attrs);
    }

    private void initDeclaredStyles(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChooserView);
        int weekPrimaryColor = array.getColor(R.styleable.ChooserView_weekPrimaryColor,  -1);
        int weekSecondaryColor = array.getColor(R.styleable.ChooserView_weekSecondaryColor,  -1);
        int timelineChoosenColor = array.getColor(R.styleable.ChooserView_timelineChoosenColor,  -1);
        int timelineOutDateColor = array.getColor(R.styleable.ChooserView_timelineOutDateColor,  -1);
        int pickerColor = array.getColor(R.styleable.ChooserView_pickerColor,  -1);
        int pickerMessColor = array.getColor(R.styleable.ChooserView_pickerMessColor,  -1);
        int pickerTextColor = array.getColor(R.styleable.ChooserView_pickerMsgColor,  -1);
        int pickerMessTextColor = array.getColor(R.styleable.ChooserView_pickerMessMsgColor,  -1);
        String pickerString = array.getString(R.styleable.ChooserView_pickerString);
        String pickerMessString = array.getString(R.styleable.ChooserView_pickerMessString);
        String pickerOutDateString = array.getString(R.styleable.ChooserView_pickerOutDateString);
        int defaultCubeSize = array.getInt(R.styleable.ChooserView_defaultCubeSize, 2);
        int minCubeSize = array.getInt(R.styleable.ChooserView_minCubeSize, 1);
        int maxCubeSize = array.getInt(R.styleable.ChooserView_maxCubeSize, 8);

        if(minCubeSize <= 0){
            minCubeSize = 1;
        }

        if(maxCubeSize > 48){
            maxCubeSize = 48;
        }

        if(maxCubeSize < minCubeSize){
            maxCubeSize = minCubeSize;
        }

        if(defaultCubeSize < minCubeSize){
            defaultCubeSize = minCubeSize;
        }else if(defaultCubeSize > maxCubeSize){
            defaultCubeSize = maxCubeSize;
        }

        if(weekPrimaryColor != -1){
            bannerTimeAdapter.setChoosenColor(weekPrimaryColor);
        }

        if(weekSecondaryColor != -1){
            bannerTimeAdapter.setChoosenSecondaryColor(weekSecondaryColor);
        }

        if(timelineChoosenColor != -1){
            timePoolZone.setChoosenColor(timelineChoosenColor);
        }

        if(timelineOutDateColor != -1){
            timePoolZone.setOutDateColor(timelineOutDateColor);
        }

        if(pickerColor != -1){
            rangePicker.setPickerColor(pickerColor);
        }

        if(pickerTextColor != -1){
            rangePicker.setPickerNormalTextColor(pickerTextColor);
        }

        if(pickerMessColor != -1){
            rangePicker.setPickerMessColor(pickerMessColor);
        }

        if(pickerMessTextColor != -1){
            rangePicker.setPickerMessTextColor(pickerMessTextColor);
        }

        if(pickerString != null){
            rangePicker.setPickerNormalString(pickerString);
        }

        if(pickerString != null){
            rangePicker.setPickerNormalString(pickerString);
        }

        if(pickerMessString != null){
            rangePicker.setPickerMessString(pickerMessString);
        }

        if(pickerOutDateString != null){
            rangePicker.setPickerOutDataString(pickerOutDateString);
        }

        if(defaultCubeSize != -1){
            rangePicker.setDefaultCubeSize(defaultCubeSize);
        }

        if(minCubeSize != -1){
            rangePicker.setMinCubeSize(minCubeSize);
        }

        if(maxCubeSize != -1){
            rangePicker.setMaxCubeSize(maxCubeSize);
        }

        array.recycle();
    }


    @Override
    public void bannerChoose(int index) {

        //今天
        if(index == 0){
            int nowLine = generateNowline();
            if(weekLines != null){
                DayLine dayLine = weekLines.get(index);
                timePoolZone.setData(nowLine, dayLine);
            }else{
                timePoolZone.setData(nowLine, null);
            }

            if(nowLine == 48){
                rangePicker.setCube(0);
                timePool.scrollToIndex(0);
            }else{
                int maxLimit = 48 - nowLine;

                int nowDefault = rangePicker.getDefaultCubeSize();
                if(nowDefault > maxLimit){
                    rangePicker.setCube(nowLine, nowLine + maxLimit);
                }else{
                    rangePicker.setCube(nowLine);
                }

                timePool.scrollToIndex(nowLine);
            }

        }else{
            if(weekLines != null){
                DayLine dayLine = weekLines.get(index);
                timePoolZone.setData(-1, dayLine);
            }else{
                timePoolZone.setData(-1, null);
            }
            timePool.scrollToIndex(0);
            rangePicker.setCube(0);
        }

    }

    private int generateNowline() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date(System.currentTimeMillis()));
        return cl.get(Calendar.HOUR_OF_DAY) * 2;
    }

    public void setTime(SparseArray<DayLine> tmp){
        if(tmp == null){
            throw new RuntimeException("时间片不能为空");
        }

        if(tmp.size() != 7){
            throw new RuntimeException("时间片需要固定设置7天");
        }

        bannerTimeAdapter.setChoosenIndex(0);
        bannerTimeAdapter.notifyDataSetChanged();

        for(int i = 0; i < tmp.size(); i++){
            weekLines.put(i, tmp.get(i));
        }

        bannerChoose(0);
    }

    public void setTime(int day, DayLine tmp){
        if(tmp == null){
            throw new RuntimeException("时间片不能为空");
        }

        if(day < 0 || day > 6){
            throw new RuntimeException("设置日期需要在0-6闭区间内");
        }

        bannerTimeAdapter.setChoosenIndex(0);
        bannerTimeAdapter.notifyDataSetChanged();

        weekLines.put(day, tmp);

        bannerChoose(0);
    }


    public void clear(){
        timePoolZone.clear();
    }

    public void setChooseDateListener(ChooseDateListener chooseDateListener) {
        this.chooseDateListener = chooseDateListener;
    }

    public void setPickerName(String s) {
        rangePicker.setPickerNormalString(s);
    }

    public interface ChooseDateListener {

        /**
         * data choose
         * @param inMess time available
         * @param date chosoen time
         * @param start start line index
         * @param end end line index
         * @param weekIndex week index
         */
        void chooseDate(int inMess, Date date, int start, int end, int weekIndex);
    }

    public void destroy(){
        if(rangePicker != null){
            rangePicker.destroy();
        }
    }
}
