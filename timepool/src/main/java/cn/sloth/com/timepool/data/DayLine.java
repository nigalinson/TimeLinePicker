package cn.sloth.com.timepool.data;

import android.util.SparseArray;

public class DayLine {

    private SparseArray<TimeLineItem> items;

    public SparseArray<TimeLineItem> getItems() {
        return items;
    }

    public void setItems(SparseArray<TimeLineItem> items) {
        this.items = items;
    }
}
