package cn.sloth.com.timepool.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import cn.sloth.com.timepool.R;

/**
 * intercept touch event
 * when user were moving picker view, thw whole timeline view should not be scrollable
 * @author nigal
 */
public class TimePool extends ScrollView {

    private boolean scrollAble = true;
    private float itemHeight = 0;

    public TimePool(Context context) {
        super(context);
        init(context);
    }

    public TimePool(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimePool(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        itemHeight = context.getResources().getDimension(R.dimen.time_line_item_height);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollAble && super.onInterceptTouchEvent(ev);
    }

    public void setCanScroll(boolean canScroll){
        scrollAble = canScroll;
    }

    public void scrollToIndex(int index){
        scrollTo(0, (int) (index * itemHeight));
    }
}
