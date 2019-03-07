package com.rhinhospital.rnd.rhiot.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rhinhospital.rnd.rhiot.R;
import com.rhinhospital.rnd.rhiot.Result.MeasurementHistory;

import java.util.ArrayList;

public class MeasurementHistoryAdapter extends RecyclerView.Adapter<MeasurementHistoryAdapter.ViewHolder> {
    private ArrayList<MeasurementHistory> mList;

    public MeasurementHistoryAdapter(ArrayList<MeasurementHistory> list) {
        this.mList = list;
    }

    /**
     * RecyclerVie에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출 됨
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mearsurement_list_item, viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Adapter의 특정 위치에 있는 데이터를 보여줘야 할때 호출된다.
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvMeasurer.setText("측정자:"+mList.get(i).getMeasurer());
        viewHolder.tvMaximal.setText("최고 혈압:"+mList.get(i).getMaximalBloodPressure());
        viewHolder.tvMinimal.setText("최저 혈압:"+mList.get(i).getMinimalBloodPressure());
        viewHolder.tvHeartPerRate.setText("심박수:"+mList.get(i).getHeartRatePerMinute());
        viewHolder.tvMeasurementTime.setText("측정 시간:"+mList.get(i).getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvMeasurer, tvMaximal, tvMinimal, tvHeartPerRate, tvMeasurementTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvMeasurer = (TextView) itemView.findViewById(R.id.tvMeasurer);
            this.tvMaximal = (TextView) itemView.findViewById(R.id.tvMaximal);
            this.tvMinimal = (TextView) itemView.findViewById(R.id.tvMinimal);
            this.tvHeartPerRate = (TextView) itemView.findViewById(R.id.tvHeartRate);
            this.tvMeasurementTime = (TextView) itemView.findViewById(R.id.tvMeasurementTime);
        }
    }
}
