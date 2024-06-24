package org.sportApp.userInterface.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {
    protected List<T> items;
    protected OnItemClickListener<T> listener;
    protected int layout;

    protected ViewHolderFactory<VH> viewHolderFactory;

    public BaseAdapter(List<T> items, int layout, OnItemClickListener<T> listener, ViewHolderFactory<VH> viewHolderFactory) {
        this.items = items;
        this.listener = listener;
        this.layout = layout;
        this.viewHolderFactory = viewHolderFactory;
    }

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return viewHolderFactory.create(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener<T> {
        default void onItemLongClick(int position) {}

        default void onItemClick(int position){}
    }

    public interface ViewHolderFactory<VH> {
        VH create(View itemView);
    }


    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(@NonNull T item, OnItemClickListener<T> listener) {
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(getAdapterPosition());
                }
                return true;
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
