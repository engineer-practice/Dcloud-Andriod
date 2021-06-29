package com.example.daoyun09.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daoyun09.httpBean.SearchListBean;
import com.example.daoyun09.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SearchListBean> data;
    private Context context;
    public SearchListAdapter(List<SearchListBean> data, Context context) {
        this.data = data;
        this.context = context;
    }
    public void setDatas(List<SearchListBean> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.search_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SearchListBean bean = data.get(i);
        viewHolder.bigTime.setText("yyyy-mm-dd");
        viewHolder.Time.setText("hh:mm:ss");
        viewHolder.attend.setText("50");

        //viewHolder.searchListItem.setOnClickListener(v -> mOnListListener.onItemClick(v, data.get(i).getCourse_id()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnListListener {
        void onButtonClick(View view, int id);

        void onItemClick(View view, int id);
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bigTime)
        TextView bigTime;
        @BindView(R.id.Time)
        TextView Time;
        @BindView(R.id.attend)
        TextView attend;
        @BindView(R.id.search_list_item)
        RelativeLayout searchListItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
