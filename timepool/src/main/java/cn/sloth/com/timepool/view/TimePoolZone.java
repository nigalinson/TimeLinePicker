package cn.sloth.com.timepool.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.LinearLayout;
import cn.sloth.com.timepool.R;
import cn.sloth.com.timepool.anim.AnimHelper;
import cn.sloth.com.timepool.data.DayLine;
import cn.sloth.com.timepool.data.TimeLineItem;
import cn.sloth.com.timepool.utils.WindowUtils;

/**
 * this is the whole time line view
 * @author nigal
 * @date 2019/10/9
 */
public class TimePoolZone extends LinearLayout {

    private float itemHeight = 0;
    private float leftLineWidth = 0;
    private float windowX = 0;
    private float footer = 0;
    private float header = 0;
    private int choosenColor, outDateColor;
    private int leftTimePoolTimeColor,lineColor, leftTimePoolBackgroundColor;
    private float timePoolLeftTextSize;
    private float orderFullTextSize;
    private float timePoolRightMargin;
    private float itemMargin;
    private float itemRadius;
    private DayLine dayline;
    private int nowLine;

    public TimePoolZone(Context context) {
        super(context);
        init(context);
    }

    public TimePoolZone(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimePoolZone(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        windowX = WindowUtils.windowX((Activity) context);
        itemHeight = context.getResources().getDimension(R.dimen.time_line_item_height);
        leftLineWidth = context.getResources().getDimension(R.dimen.time_line_left_width);
        lineColor = ContextCompat.getColor(context, R.color.line);
        leftTimePoolTimeColor = ContextCompat.getColor(context, R.color.left_time_pool_text_color);
        leftTimePoolBackgroundColor = Color.parseColor("#ffffff");
        timePoolLeftTextSize = context.getResources().getDimension(R.dimen.time_pool_left_text_size);
        orderFullTextSize = context.getResources().getDimension(R.dimen.order_full_text_size);
        header = context.getResources().getDimension(R.dimen.time_pool_header);
        timePoolRightMargin = context.getResources().getDimension(R.dimen.time_line_right_margin);
        itemMargin = context.getResources().getDimension(R.dimen.item_margin);
        itemRadius = context.getResources().getDimension(R.dimen.item_radius);
    }

    public void setChoosenColor(int choosenColor) {
        this.choosenColor = choosenColor;
    }

    public void setOutDateColor(int outDateColor) {
        this.outDateColor = outDateColor;
    }

    public void setData(int nowLine, DayLine tmp){
        this.nowLine = nowLine;
        this.dayline = tmp;

        refresh();
    }

    public int inMess(int start, int end){

        if(nowLine != -1){
            if( start >= nowLine){

                if(inMessWithOthers(start, end)){
                    return RangePicker.MESS_STATE_MESS;
                }

                return RangePicker.MESS_STATE_NORMAL;
            }else{
                return RangePicker.MESS_STATE_OUT_DATE;
            }
        }

        if(inMessWithOthers(start, end)){
            return RangePicker.MESS_STATE_MESS;
        }

        return RangePicker.MESS_STATE_NORMAL;
    }

    private boolean inMessWithOthers(int start, int end){

        if(dayline != null && dayline.getItems() != null){

            for(int i = 0;i < dayline.getItems().size();i++){
                TimeLineItem pair = dayline.getItems().get(i);
                if(pair.getEnd() <= start || pair.getStart() >= end){
                    continue;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint p = new Paint();
        p.setStrokeWidth(1);
        p.setTextSize(timePoolLeftTextSize);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);

        /**
         * 画时间轴白色背景
         */
        p.setColor(leftTimePoolBackgroundColor);
        RectF leftBg = new RectF(0, 0 , leftLineWidth, header + itemHeight * 48);
        canvas.drawRect(leftBg, p);

        /**
         * 画时间轴
         */
        int totalBars = 48;
        for(int index = 0; index < totalBars; index++){
            String text = index % 2 == 0 ? String.valueOf( index / 2 ) + ":00" : String.valueOf( index / 2 ) + ":30";
            p.setColor(leftTimePoolTimeColor);
            canvas.drawText(text, 20, itemHeight * index + timePoolLeftTextSize / 2 + header, p);
            p.setColor(lineColor);
            canvas.drawLine(leftLineWidth, itemHeight * index + header, windowX - timePoolRightMargin, itemHeight * index + header, p);
        }

        /**
         * 画选中时间片
         */
        if(dayline != null && dayline.getItems() != null && dayline.getItems().size() > 0){
            for(int flag = 0; flag < dayline.getItems().size(); flag ++){
                drawRangedRect(canvas, dayline.getItems().get(flag), dayline.getItems().get(flag).getName(), p);
            }
        }


        /**
         * 画今日过期时间
         */
        if(nowLine != -1){
            drawOutDateRect(canvas, nowLine,  "已过期", p);
        }

    }

    private void drawRangedRect(Canvas canvas, TimeLineItem pair, String name, Paint p) {

        p.setColor(choosenColor);
        float tmpTop = pair.getStart() * itemHeight + itemMargin;
        float tmpBottom = pair.getEnd() * itemHeight - itemMargin;
        // 设置个新的长方形
        RectF oval3 = new RectF(leftLineWidth, tmpTop + header , windowX - timePoolRightMargin, tmpBottom + header);
        //第二个参数是x半径，第三个参数是y半径
        canvas.drawRoundRect(oval3, itemRadius, itemRadius, p);

        p.setColor(Color.WHITE);
        p.setTextSize(orderFullTextSize);
        float texWid = p.measureText(TextUtils.isEmpty(name) ? "约满" : name);
        float x = (windowX - leftLineWidth - timePoolRightMargin) / 2 + leftLineWidth -20 - ( texWid / 2 );
        canvas.drawText(TextUtils.isEmpty(name) ? "约满" : name, x, (tmpBottom - tmpTop) / 2 + (orderFullTextSize / 2) + tmpTop + header, p);

    }

    private void drawOutDateRect(Canvas canvas, int nowLine, String name, Paint p) {

        p.setColor(outDateColor);
        float tmpTop = 0;
        float tmpBottom = nowLine * itemHeight - itemMargin;
        // 设置个新的长方形
        RectF oval3 = new RectF(leftLineWidth, tmpTop + header , windowX - timePoolRightMargin, tmpBottom + header);
        //第二个参数是x半径，第三个参数是y半径
        canvas.drawRect(oval3, p);

        p.setColor(Color.WHITE);
        p.setTextSize(orderFullTextSize);
        float texWid = p.measureText(TextUtils.isEmpty(name) ? "已占用" : name);
        float x = (windowX - leftLineWidth - timePoolRightMargin) / 2 + leftLineWidth -20 - ( texWid / 2 );
        canvas.drawText(TextUtils.isEmpty(name) ? "已占用" : name, x, (tmpBottom - tmpTop) / 2 + (orderFullTextSize / 2) + tmpTop + header, p);

    }

    public void refresh() {
        AnimHelper.fadeOut(this);
        invalidate();
        AnimHelper.fadeIn(this);

    }

    public void clear() {
        if(dayline != null &&  dayline.getItems() != null){
            dayline.getItems().clear();
            invalidate();
        }
    }
}
