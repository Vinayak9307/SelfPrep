package com.example.selfprep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChapterListActivity extends AppCompatActivity {

    AppCompatButton back_btn ;
    FloatingActionButton done_btn;

    ArrayList<Chapter> chapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        ListView chapterListView = findViewById(R.id.chapterList);

        read_csv();

        ArrayList<String> chapterName = new ArrayList<>();

        for(Chapter C:chapters){
            chapterName.add(C.name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked,chapterName);

        chapterListView.setAdapter(arrayAdapter);

        back_btn = findViewById(R.id.back_btn);
        done_btn = findViewById(R.id.floatingActionButton);


        back_btn.setOnClickListener(v -> finish());

        done_btn.setOnClickListener(v -> {

            SparseBooleanArray sparseBooleanArray = chapterListView.getCheckedItemPositions();
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                int index = sparseBooleanArray.keyAt(i);
                chapters.get(index).done = sparseBooleanArray.valueAt(i);
            }

            if(chapterListView.getCheckedItemCount()>0){
                Intent intent = new Intent(ChapterListActivity.this , NumInputActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("chapter_list",chapters);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please Select atleast one chapter !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void read_csv() {
        InputStream in = getResources().openRawResource(R.raw.question);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = "";
        String[] values;
        int index = 1;
        try {
            while ((line = reader.readLine()) != null)
            {
                ArrayList<Integer> questions = new ArrayList<>();

                values = line.split(",");

                for(int i = 1 ; i <=8 ;i++){
                    int no_of_questions = Integer.parseInt(values[i]);
                    if(no_of_questions != 0){
                        questions.add(no_of_questions);
                    }
                }

                int total = Integer.parseInt(values[9]);

                chapters.add(new Chapter(values[0],index++,questions.size(), questions , total , false));

            }
        }
        catch (IOException e){
            Toast.makeText(
                    getApplicationContext(), "Error reading data File on line " + line, Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}