package com.sloth.www.timelinepicker;

import androidx.appcompat.app.AppCompatActivity;
import cn.sloth.com.timepool.data.TimeLineItem;
import cn.sloth.com.timepool.data.DayLine;
import cn.sloth.com.timepool.view.ChooserView;

import android.os.Bundle;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ChooserView chooserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chooserView = findViewById(R.id.chooser);
        chooserView.setChooseDateListener(new ChooserView.ChooseDateListener() {
            @Override
            public void chooseDate(int inMess, Date date, int start, int end, int weekIndex) {
                System.out.println(String.format("正在选择：%s, %d ~ %d 的时间，当前状态%d", new SimpleDateFormat("yyyy/MM/dd").format(date), start, end, inMess));
            }
        });

        putInData();
    }

    private void putInData() {
        for(int i = 0; i < 7; i++){
            DayLine weekDataSet = new DayLine();
            SparseArray<TimeLineItem> arrangements = new SparseArray<>();
            TimeLineItem a1 = new TimeLineItem();
            a1.setId(1);
            a1.setName("阿里巴巴");
            a1.setStart(new Random().nextInt(24));
            a1.setEnd(a1.getStart() + new Random().nextInt(4) + 1);
            arrangements.append(arrangements.size(), a1);
            weekDataSet.setItems(arrangements);

            chooserView.setTime(i, weekDataSet);
        }

    }

    @Override
    protected void onDestroy() {
        chooserView.destroy();
        super.onDestroy();
    }
}
