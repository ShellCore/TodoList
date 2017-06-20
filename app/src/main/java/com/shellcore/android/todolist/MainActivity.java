package com.shellcore.android.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // Componentes
    @BindView(R.id.rec_main)
    RecyclerView recMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();
    }

    @OnClick(R.id.btn_add)
    public void onClickBtnAdd() {

    }

    private void setupRecyclerView() {
        recMain.setLayoutManager(new LinearLayoutManager(this));
        recMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recMain.setHasFixedSize(true);
    }
}
