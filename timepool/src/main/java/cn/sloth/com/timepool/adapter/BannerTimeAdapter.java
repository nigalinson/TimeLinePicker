package cn.sloth.com.timepool.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.recyclerview.widget.RecyclerView;
import cn.sloth.com.timepool.R;
import cn.sloth.com.timepool.data.BannerTimeProvider;
import cn.sloth.com.timepool.utils.WindowUtils;

/**
 * top week data adapter
 * @author nigal
 * @date 2019/10/9
 */
public class BannerTimeAdapter extends RecyclerView.Adapter{

    private Context context;
    private BannerChooseCallback callback;
    private BannerTimeProvider bannerTimeProvider;
    private SimpleDateFormat simpleDateFormat;

    private int choosenColor;
    private int choosenSecondaryColor;

    private int choosenIndex = 0;

    public BannerTimeAdapter(Context context, BannerChooseCallback callback) {
        this.context = context;
        this.callback = callback;
        bannerTimeProvider = BannerTimeProvider.getIns(context);
        simpleDateFormat = new SimpleDateFormat("dd", Locale.CHINA);
        choosenColor = Color.parseColor("#FC9153");
        choosenSecondaryColor = Color.parseColor("#FFC4A6");

    }

    public void setChoosenIndex(int choosenIndex) {
        this.choosenIndex = choosenIndex;
    }

    public int getChoosenIndex() {
        return this.choosenIndex;
    }

    public Date getChooseDate(){
        return bannerTimeProvider.getBannerDate().get(choosenIndex);
    }

    public void setChoosenColor(int choosenColor) {
        this.choosenColor = choosenColor;
    }

    public void setChoosenSecondaryColor(int choosenSecondaryColor) {
        this.choosenSecondaryColor = choosenSecondaryColor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.wrapper_banner_time, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(WindowUtils.windowX((Activity) context) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Date date = bannerTimeProvider.getBannerDate().get(position);
        if(position == 0){
            ((Vh)holder).tvWeekday.setText(((Vh)holder).tvWeekday.getContext().getResources().getString(R.string.today));
        }else if(position == 1){
            ((Vh)holder).tvWeekday.setText(((Vh)holder).tvWeekday.getContext().getResources().getString(R.string.tomorrow));
        }else{
            ((Vh)holder).tvWeekday.setText(bannerTimeProvider.getDayOfWeek(date));
        }
        ((Vh)holder).tvDate.setText(simpleDateFormat.format(date));
        //选中第一天
        if(choosenIndex == 0){
            //第一天
            if(0 == position){ 
                ((Vh)holder).tvWeekday.setTextColor(Color.parseColor("#FFFFFF"));
                ((Vh)holder).tvDate.setTextColor(Color.parseColor("#FFFFFF"));
                holder.itemView.setBackgroundColor(choosenColor);
                ((Vh)holder).tvDate.setBackgroundColor(choosenColor);

            }else{ //其他
                ((Vh)holder).tvWeekday.setTextColor(Color.parseColor("#9a9a9a"));
                ((Vh)holder).tvDate.setTextColor(Color.parseColor("#313131"));
                ((Vh)holder).tvDate.setBackgroundColor(Color.parseColor("#00FFFFFF"));

            }
        }else{ //未选中第一天
            if(0 == position){
                ((Vh)holder).tvWeekday.setTextColor(Color.parseColor("#FFFFFF"));
                ((Vh)holder).tvDate.setTextColor(Color.parseColor("#FFFFFF"));
                holder.itemView.setBackgroundColor(choosenSecondaryColor);
                ((Vh)holder).tvDate.setBackgroundColor(choosenSecondaryColor);
            }else if(choosenIndex == position){
                ((Vh)holder).tvWeekday.setTextColor(Color.parseColor("#9a9a9a"));
                ((Vh)holder).tvDate.setTextColor(Color.parseColor("#ffffff"));
                ((Vh)holder).tvDate.setBackgroundColor(choosenColor);
            }else{
                ((Vh)holder).tvWeekday.setTextColor(Color.parseColor("#9a9a9a"));
                ((Vh)holder).tvDate.setTextColor(Color.parseColor("#313131"));
                ((Vh)holder).tvDate.setBackgroundColor(Color.parseColor("#00FFFFFF"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return bannerTimeProvider.getBannerDate().size();
    }

    class Vh extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvWeekday,tvDate;
        public Vh(View itemView) {
            super(itemView);
            tvWeekday = itemView.findViewById(R.id.tv_weekday);
            tvDate = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            choosenIndex = getAdapterPosition();
            if(callback != null){
                callback.bannerChoose(choosenIndex);
            }
            notifyDataSetChanged();
        }
    }

    public interface BannerChooseCallback{
        /**
         * click banner
         * @param index banner index be clicked
         */
        void bannerChoose(int index);
    }

}
