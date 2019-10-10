# TimeLinePicker

[英文](/README.MD)

中文

这个库可以用来在一周以内的时间轴上展示或选择时间片。只需几行代码就可以使用默认样式的选择器，如果有需要的话，也可以自定义颜色和选择器的大小限制。谢谢大家的使用，希望大家能够多多点赞。给您拜个早年了。

## 展示
### 默认样式
![](examples/example.gif)

### 自定义样式
![](examples/diy.png)

## 使用

### 简单用法

```java

//xml
<cn.sloth.com.timepool.view.ChooserView
    android:id="@+id/chooser"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

//java
//listener
chooserView.setChooseDateListener(new ChooserView.ChooseDateListener() {
    @Override
    public void chooseDate(int inMess, Date date, int start, int end, int weekIndex) {
        //inMess: 0-normal / 1-mess with others / 2-out of time
        //start: picker start time. For example, 16 means 8 am, 17 means 8.30 am
    }
});

//destroy
chooserView.destroy();

```


### 自定义用法

```java

//xml
 <cn.sloth.com.timepool.view.ChooserView
      android:id="@+id/chooser"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:weekPrimaryColor="#048074"
      app:weekSecondaryColor="#50ACA2"
      app:timelineChoosenColor="#79333333"
      app:timelineOutDateColor="#8B7E7E7E"
      app:pickerColor="#80009688"
      app:pickerMsgColor="#FFFFFE"
      app:pickerMessColor="#80C42828"
      app:pickerMessMsgColor="#FFFFFE"
      app:defaultCubeSize="2"
      app:minCubeSize="1"
      app:maxCubeSize="8"
      app:pickerString="Picking"
      app:pickerMessString="Has been used"
      app:pickerOutDateString="Out of date" />

//java
//listener
chooserView.setChooseDateListener(new ChooserView.ChooseDateListener() {
    @Override
    public void chooseDate(int inMess, Date date, int start, int end, int weekIndex) {
        //inMess: 0-normal / 1-mess with others / 2-out of time
        //start: picker start time. For example, 16 means 8 am, 17 means 8.30 am
    }
});

//set in showing dataset
 for(int i = 0; i < 7; i++){// must set 7 days
    //item of a day
    DayLine weekDataSet = new DayLine();
    //arrangements in a day
    SparseArray<TimeLineItem> arrangements = new SparseArray<>();
    TimeLineItem a1 = new TimeLineItem();
    a1.setId(1);
    a1.setName("阿里巴巴");
    a1.setStart(new Random().nextInt(24));
    a1.setEnd(a1.getStart() + new Random().nextInt(4) + 1);
    arrangements.append(arrangements.size(), a1);
    weekDataSet.setItems(arrangements);
    //set into picker
    chooserView.setTime(i, weekDataSet);
}

//destroy
chooserView.destroy();

```
