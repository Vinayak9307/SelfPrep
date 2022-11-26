package com.example.selfprep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

public class BookListActivity extends AppCompatActivity {

    AppCompatButton close_btn , book_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard1);
        close_btn = findViewById(R.id.close_btn);
        book_btn = findViewById(R.id.ar_book_btn);

        close_btn.setOnClickListener(v -> onBackPressed());

        book_btn.setOnClickListener(v -> {
            Intent intent = new Intent(BookListActivity.this , ChapterListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }
}