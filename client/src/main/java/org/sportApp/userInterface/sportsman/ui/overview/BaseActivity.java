package org.sportApp.userInterface.sportsman.ui.overview;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.sportApp.userInterface.adapters.BaseAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<T, R> extends AppCompatActivity {
    protected List<T> items = new ArrayList<>();
    protected R entity;

    protected abstract int getLayout();

    protected abstract List<T> getItems();

    protected abstract int getRecyclerView();

    protected abstract Class<?> getShowWindowClass();

    protected abstract String getName();

    protected abstract BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> createAdapter();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout = getLayout();
        setContentView(layout);
        initializeEntity();
        int recyclerView = getRecyclerView();
        RecyclerView currentRecyclerView = findViewById(recyclerView);
        BaseAdapter<T, ? extends BaseAdapter.BaseViewHolder<T>> currentAdapter = createAdapter();
        currentRecyclerView.setAdapter(currentAdapter);
        currentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void showItem(int position, String dto) {
        if (position != RecyclerView.NO_POSITION) {
            T item = items.get(position);
            Class<?> showClass = getShowWindowClass();
            Intent intent = new Intent(this, showClass);
            intent.putExtra(dto, (Serializable) item);
            startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    protected void initializeEntity() {
        Intent intent = getIntent();
        String entityName = getName();
        if (intent != null && intent.hasExtra(entityName)) {
            entity = (R) intent.getSerializableExtra(entityName);
            items = getItems();
        }
    }

}
