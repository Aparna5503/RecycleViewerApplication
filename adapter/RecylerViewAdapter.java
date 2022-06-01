package in.com.demo.myrecycleviewerapplication.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.com.demo.myrecycleviewerapplication.R;
import in.com.demo.myrecycleviewerapplication.model.ArticleInfo;
import in.com.demo.myrecycleviewerapplication.view.DialogUtils;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private ArrayList<ArticleInfo> mItemList;

    public RecylerViewAdapter(ArrayList<ArticleInfo> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_row_items, parent, false);
            return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) holder, position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showWebview(mItemList.get(position).getUrl(), v.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtDesc;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAuthor = itemView.findViewById(R.id.txt_author);
            txtDesc = itemView.findViewById(R.id.txt_desc);
        }
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {
        ArticleInfo item = mItemList.get(position);
        viewHolder.txtTitle.setText(item.getTitle());
        viewHolder.txtAuthor.setText(item.getAuthor());
        viewHolder.txtDesc.setText(item.getDesc());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(ArrayList<ArticleInfo> data) {
        this.mItemList.clear();
        this.mItemList.addAll(data);
        notifyDataSetChanged();
    }
}