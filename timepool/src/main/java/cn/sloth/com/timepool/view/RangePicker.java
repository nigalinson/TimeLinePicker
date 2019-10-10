package cn.sloth.com.timepool.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import cn.sloth.com.timepool.R;
import cn.sloth.com.timepool.anim.AnimHelper;
import cn.sloth.com.timepool.utils.WindowUtils;

/**
 * this is a picker view which could be used to choose timeline
 * @author nigal
 * @date 2019/10/9
 */
public class RangePicker extends LinearLayout {

    /**
     * normal state,which means useful time
     * @author nigal
     * @date 2019/10/9
     */
    public static final int MESS_STATE_NORMAL = 0;
    /**
     * has collided with other cubes
     * @author nigal
     * @date 2019/10/9
     */
    public static final int MESS_STATE_MESS = 1;
    /**
     * has out of date
     * @author nigal
     * @date 2019/10/9
     */
    public static final int MESS_STATE_OUT_DATE = 2;

    private final float MIN_GAP = 0.5F;
    private final float MAX_GAP = 48.5F;

    private float itemHeight = 0;
    private float leftLineWidth = 0;
    private float windowX = 0;
    private float footer = 0;
    private float header = 0;
    private int choosenColor;
    private int leftTimePoolTimeColor,lineColor;
    private float timePoolLeftTextSize;
    private float orderFullTextSize;
    private float timePoolRightMargin;
    private float itemMargin;
    private float itemRadius;


    private float fliperSize;
    private float fliperMargin;

    private float touchBarRange;

    private float pickerTop = 0;
    private float pickerBottom = 0;
    private int pickerColor;
    private int pickerMessColor;
    private int pickerMessTextColor;
    private int pickerNormalTextColor;

    private int inMess = MESS_STATE_NORMAL;

    private TouchRangingListener rangingListener;
    private ChooseCubesListener chooseCubesListener;

    private STATE state = STATE.NORMAL;

    private RefreshHandler refreshHandler;

    private String pickerNormalString = "选择";
    private String pickerMessString = "已被占用";
    private String pickerOutDataString = "已过期";
    private int defaultCubeSize ;
    private int minCubeSize;
    private int maxCubeSize;


    public enum STATE{
        //free
        NORMAL(0),
        //picker ranging up
        PULLING_UP(1),
        //picker ranging down
        PULLING_DOWN(2),
        //moving
        MOVIN(3);
        int value;

        STATE(int value) {
            this.value = value;
        }
    }

    public RangePicker(Context context) {
        super(context);
        init(context);
    }

    public RangePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RangePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setRangingListener(TouchRangingListener rangingListener) {
        this.rangingListener = rangingListener;
    }

    public void setChooseCubesListener(ChooseCubesListener chooseCubesListener) {
        this.chooseCubesListener = chooseCubesListener;
    }

    public void setInMess(int inMess) {
        this.inMess = inMess;
    }

    public int isInMess() {
        return inMess;
    }

    public void setCube(int start,int end){
        pickerTop = header + itemMargin + (start * itemHeight);

        pickerBottom = header + itemMargin + end * itemHeight;

        if(pickerBottom <= pickerTop){
            pickerBottom = pickerTop + itemHeight;
        }

        movingDataUserfulValidate();

        if( chooseCubesListener != null ){
            chooseCubesListener.chooseCubes(start, end);
        }

        invalidate();
    }

    public void setCube(int start){
        setCube(start, start + defaultCubeSize);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        windowX = WindowUtils.windowX((Activity) context);
        itemHeight = context.getResources().getDimension(R.dimen.time_line_item_height);
        leftLineWidth = context.getResources().getDimension(R.dimen.time_line_left_width);
        choosenColor = ContextCompat.getColor(context, R.color.choosen);
        lineColor = ContextCompat.getColor(context, R.color.line);
        leftTimePoolTimeColor = ContextCompat.getColor(context, R.color.left_time_pool_text_color);
        timePoolLeftTextSize = context.getResources().getDimension(R.dimen.time_pool_left_text_size);
        orderFullTextSize = context.getResources().getDimension(R.dimen.time_ranger_mess_text_size);
        header = context.getResources().getDimension(R.dimen.time_pool_header);
        timePoolRightMargin = context.getResources().getDimension(R.dimen.time_line_right_margin);
        itemMargin = context.getResources().getDimension(R.dimen.item_margin);
        itemRadius = context.getResources().getDimension(R.dimen.item_radius);

        fliperSize = context.getResources().getDimension(R.dimen.time_ranger_picker_fliper_size);
        fliperMargin = context.getResources().getDimension(R.dimen.time_ranger_picker_fliper_margin);
        touchBarRange = context.getResources().getDimension(R.dimen.time_ranger_bar_overflow_size);


        pickerTop = header + itemMargin;
        pickerBottom = pickerTop + itemHeight;

        refreshHandler = new RefreshHandler(this);
    }

    public void setPickerColor(int pickerColor) {
        this.pickerColor = pickerColor;
    }

    public void setPickerNormalTextColor(int pickerNormalTextColor) {
        this.pickerNormalTextColor = pickerNormalTextColor;
    }

    public void setPickerMessColor(int pickerMessColor) {
        this.pickerMessColor = pickerMessColor;
    }

    public void setPickerMessTextColor(int pickerMessTextColor) {
        this.pickerMessTextColor = pickerMessTextColor;
    }

    public void setPickerNormalString(String pickerNormalString) {
        this.pickerNormalString = pickerNormalString;
    }

    public void setPickerMessString(String pickerMessString) {
        this.pickerMessString = pickerMessString;
    }

    public void setPickerOutDataString(String pickerOutDataString) {
        this.pickerOutDataString = pickerOutDataString;
    }

    public void setDefaultCubeSize(int defaultCubeSize) {
        this.defaultCubeSize = defaultCubeSize;
    }

    public void setMinCubeSize(int minCubeSize) {
        this.minCubeSize = minCubeSize;
    }

    public void setMaxCubeSize(int maxCubeSize) {
        this.maxCubeSize = maxCubeSize;
    }

    public int getDefaultCubeSize() {
        return defaultCubeSize;
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

        p.setColor(inMess != MESS_STATE_NORMAL ? pickerMessColor : pickerColor);


        /**
         * 画拖动button
         */

        Path pathTop = new Path();
        pathTop.moveTo(leftLineWidth + fliperMargin, pickerTop);
        pathTop.lineTo(leftLineWidth + fliperMargin + (fliperSize / 2), pickerTop - (fliperSize / 2));
        pathTop.lineTo(leftLineWidth + fliperMargin + fliperSize, pickerTop);
        pathTop.close();
        canvas.drawPath(pathTop, p);

        Path pathBottom = new Path();
        pathBottom.moveTo(windowX - fliperMargin - fliperSize, pickerBottom - (itemMargin * 2));
        pathBottom.lineTo(windowX - fliperMargin, pickerBottom - (itemMargin * 2));
        pathBottom.lineTo(windowX - fliperMargin - (fliperSize / 2), pickerBottom - (itemMargin * 2) + (fliperSize / 2));
        pathBottom.close();
        canvas.drawPath(pathBottom, p);


        /**
         * 画矩形
         */
        // 设置个新的长方形
        RectF oval3 = new RectF(leftLineWidth, pickerTop , windowX - timePoolRightMargin, pickerBottom - (itemMargin * 2));
        //第二个参数是x半径，第三个参数是y半径
        canvas.drawRoundRect(oval3, itemRadius, itemRadius, p);

        /**
         * 画文字
         */
        if(inMess == MESS_STATE_MESS){
            p.setColor(pickerMessTextColor);
            p.setTextSize(orderFullTextSize);
            float texWid = p.measureText(pickerMessString);
            float x = (windowX - leftLineWidth - timePoolRightMargin) / 2 + leftLineWidth -20 - ( texWid / 2 );
            canvas.drawText( pickerMessString , x, (pickerBottom - pickerTop) / 2 + (orderFullTextSize / 2) + pickerTop - itemMargin, p);
        }else if(inMess == MESS_STATE_OUT_DATE){
            p.setColor(pickerMessTextColor);
            p.setTextSize(orderFullTextSize);
            float texWid = p.measureText(pickerOutDataString);
            float x = (windowX - leftLineWidth - timePoolRightMargin) / 2 + leftLineWidth -20 - ( texWid / 2 );
            canvas.drawText( pickerOutDataString , x, (pickerBottom - pickerTop) / 2 + (orderFullTextSize / 2) + pickerTop - itemMargin, p);
        }else{
            p.setColor(pickerNormalTextColor);
            p.setTextSize(orderFullTextSize);
            float texWid = p.measureText(pickerNormalString);
            float x = (windowX - leftLineWidth - timePoolRightMargin) / 2 + leftLineWidth -20 - ( texWid / 2 );
            canvas.drawText( pickerNormalString , x, (pickerBottom - pickerTop) / 2 + (orderFullTextSize / 2) + pickerTop - itemMargin, p);
        }

    }

    public void refresh() {
        AnimHelper.fadeOut(this);
        invalidate();
        AnimHelper.fadeIn(this);

    }

    public float getTotalHeight(){
        return 48 * itemHeight + footer + header;
    }

    public float getTotalWidth(){
        return windowX;
    }

    public void clear() {

    }

    float topOffset = 0;
    float bottomOffset = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return handlerDown(event);
            case MotionEvent.ACTION_MOVE:
                return handlerMove(event);
            case MotionEvent.ACTION_UP:
                return handlerUp();
            default: return true;
        }
    }

    private boolean handlerDown(MotionEvent event) {
        if(inTopRange(event)){
             state = STATE.PULLING_UP;
             topOffset = event.getY() - pickerTop;
             if(rangingListener != null){
                 rangingListener.inTouchRanging(true);
             }
         }else if(inBottomRange(event)){
             state = STATE.PULLING_DOWN;
             bottomOffset =pickerBottom - event.getY();
             if(rangingListener != null){
                 rangingListener.inTouchRanging(true);
             }
         }else  if(inCenterRange(event)){
            state = STATE.MOVIN;
            topOffset = event.getY() - pickerTop;
            bottomOffset =pickerBottom - event.getY();
            if(rangingListener != null){
                rangingListener.inTouchRanging(true);
            }
        }else{
             if(rangingListener != null){
                 rangingListener.inTouchRanging(false);
             }
         }

        return true;
    }

    private boolean handlerMove(MotionEvent event) {
        //正在拖动
        if(state == STATE.MOVIN){

            float tmpTop = event.getY() - topOffset;
            float tmpBottom = event.getY() + bottomOffset;

            /*最低最高限制*/
            if(tmpTop < header - (itemHeight * MIN_GAP) || tmpBottom > header + (MAX_GAP * itemHeight)){
                return true;
            }

            pickerTop = tmpTop;
            pickerBottom = tmpBottom;
            if(refreshHandler != null && !refreshHandler.isDrawing()){
                refreshHandler.setDrawing(true);
            }
            movingDataUserfulValidate();

        }else  if(state == STATE.PULLING_UP){
            //正在上拉
            float tmpTop = event.getY() - topOffset;

            /*最低最高限制*/
            if(tmpTop < header - (itemHeight * MIN_GAP)){
                return true;
            }
            /*最大限制*/
            if(pickerBottom - tmpTop > ((float)maxCubeSize + 0.5) * itemHeight){
                return true;
            }

            /*最小限制*/
            if(pickerBottom - tmpTop < ((float)minCubeSize - 0.5) * itemHeight){
                return true;
            }

            float preTop = event.getY() - topOffset;
            if(Math.abs(preTop - pickerBottom) < itemHeight){
                topOffset = event.getY() - pickerTop;
                return true;
            }

            pickerTop = preTop;
            if(refreshHandler != null && !refreshHandler.isDrawing()){
                refreshHandler.setDrawing(true);
            }
            movingDataUserfulValidate();
        }else  if(state == STATE.PULLING_DOWN){
            //正在下拉
            float tmpBottom = event.getY() + bottomOffset;

            /*最低最高限制*/
            if(tmpBottom > header + (MAX_GAP * itemHeight)){
                return true;
            }
            /*最大限制*/
            if(tmpBottom - pickerTop > ((float)maxCubeSize + 0.5) * itemHeight){
                return true;
            }

            /*最小限制*/
            if(tmpBottom - pickerTop < ((float)minCubeSize - 0.5) * itemHeight){
                return true;
            }

            float preBottom = event.getY() + bottomOffset;
            if(Math.abs(pickerTop - preBottom) < itemHeight){
                bottomOffset = pickerBottom - event.getY();
                return true;
            }

            pickerBottom = preBottom;
            if(refreshHandler != null && !refreshHandler.isDrawing()){
                refreshHandler.setDrawing(true);
            }
            movingDataUserfulValidate();
        }

        return true;
    }

    private boolean handlerUp() {
        state = STATE.NORMAL;

        validateCube();

        if(refreshHandler != null){
            refreshHandler.setDrawing(false);
        }
        return true;
    }

    /**
     * 滑动中的有效判断
     */
    private void movingDataUserfulValidate(){
        float userfulTopHeight = pickerTop - header - itemMargin;
        int jumpTop2Int = (int) (userfulTopHeight / itemHeight);
        float jumpTop2Float = userfulTopHeight / itemHeight;
        if(jumpTop2Float - jumpTop2Int >= MIN_GAP){
            jumpTop2Int++;
        }

        float userfulBottomHeight = pickerBottom - header - itemMargin;
        int jumpBottom2Int = (int) (userfulBottomHeight / itemHeight);
        float jumpBottom2Float = userfulBottomHeight / itemHeight;
        if(jumpBottom2Float - jumpBottom2Int >= MIN_GAP){
            jumpBottom2Int++;
        }

        if( chooseCubesListener != null ){
            chooseCubesListener.collideCubes(jumpTop2Int, jumpBottom2Int);
        }
    }

    /**
     * 自动跳转到固定高度的大小
     */
    private void validateCube() {

        float userfulTopHeight = pickerTop - header - itemMargin;
        int jumpTop2Int = (int) (userfulTopHeight / itemHeight);
        float jumpTop2Float = userfulTopHeight / itemHeight;
        if(jumpTop2Float - jumpTop2Int >= MIN_GAP){
            jumpTop2Int++;
        }
        pickerTop = header + itemMargin + (jumpTop2Int * itemHeight);

        float userfulBottomHeight = pickerBottom - header - itemMargin;
        int jumpBottom2Int = (int) (userfulBottomHeight / itemHeight);
        float jumpBottom2Float = userfulBottomHeight / itemHeight;
        if(jumpBottom2Float - jumpBottom2Int >= MIN_GAP){
            jumpBottom2Int++;
        }
        pickerBottom = header + itemMargin + jumpBottom2Int * itemHeight;

        if(pickerBottom <= pickerTop){
            pickerBottom = pickerTop + itemHeight;
        }

        if( chooseCubesListener != null ){
            chooseCubesListener.chooseCubes(jumpTop2Int, jumpBottom2Int);
        }

        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void destroy(){
        if(refreshHandler!=null){
            refreshHandler.destroy();
        }
    }

    private boolean inCenterRange(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        return y > pickerTop && y < pickerBottom;

    }

    private boolean inTopRange(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        int half = 2;
        return Math.abs(pickerTop - (fliperSize / half) - y) <= touchBarRange
                && Math.abs(leftLineWidth + fliperMargin + (fliperSize / half) - x) <= touchBarRange;

    }

    private boolean inBottomRange(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int half = 2;
        return Math.abs(pickerBottom + (fliperSize / half) - y) <= touchBarRange
                && Math.abs(windowX - fliperMargin - (fliperSize / half) - x) <= touchBarRange;

    }

    public static class RefreshHandler extends Handler {
        View view;
        boolean isDrawing = false;

        public RefreshHandler(View view) {
            this.view = view;
        }

        public void setDrawing(boolean isDrawing){
            this.isDrawing = isDrawing;
            if(isDrawing){
                sendEmptyMessage(0);
            }
        }

        public void setDrawingDelay(boolean isDrawing, long delay){
            this.isDrawing = isDrawing;
            if(isDrawing){
                sendEmptyMessageDelayed(0, delay);
            }
        }

        public boolean isDrawing(){
            return isDrawing;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!isDrawing){
                return;
            }

            sendEmptyMessageDelayed(0, 10);
            if(view != null){
                view.invalidate();
            }
        }

        public void destroy(){
            view = null;
        }
    }

    public interface TouchRangingListener {
        /**
         * judge touching range
         * @param bCenter is touching in center
         */
        void inTouchRanging(boolean bCenter);
    }

    public interface ChooseCubesListener{
        /**
         * cubes be collided
         * @param topIndex top line index
         * @param bottomIndex end line index
         */
        void collideCubes(int topIndex, int bottomIndex);

        /**
         * cubes be choose
         * @param topIndex top line index
         * @param bottomIndex end line index
         */
        void chooseCubes(int topIndex, int bottomIndex);
    }

}
