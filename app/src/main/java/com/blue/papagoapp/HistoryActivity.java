package com.blue.papagoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.blue.papagoapp.adapter.HistoryAdapter;
import com.blue.papagoapp.model.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter adapter;
    ArrayList<History> historyArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 번역결과 히스토리 화면에 띄우기

        // 메인에서 보내준 데이터 받아옴
        // (리스트를 보냈으니 리스트로 받아야한다 라고 생각하면 되려나)
        // addAll 로 리스트 안에 다 추가
        historyArrayList.addAll((ArrayList<History>) getIntent().getSerializableExtra("history"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        // 어댑터 사용 (메인에서 하던것처럼)
        adapter = new HistoryAdapter(HistoryActivity.this, historyArrayList);
        recyclerView.setAdapter(adapter);

        // 리사이클러뷰가 다른 액티비티에 있어서 화면에 띄우기 위해
        // 데이터 보내주고 받는 것만 문제였지 나머지는 동일한 방식이였다~
    }
}