package com.example.selfprep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NumInputActivity extends AppCompatActivity {

    AppCompatButton back_btn;
    FloatingActionButton done_btn;
    EditText editText;
    ArrayList<Chapter> chapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_input);
        chapters = getIntent().getParcelableArrayListExtra("chapter_list");
        back_btn = findViewById(R.id.A3back_btn);
        done_btn = findViewById(R.id.A3donebtn);
        editText = findViewById(R.id.A3editText);

        back_btn.setOnClickListener(v -> finish());

        int total = 0;
        for(int i = 0 ; i < chapters.size() ; i++){
            if(chapters.get(i).done){
                total += chapters.get(i).total;
            }
        }

        editText.setHint("Maximum "+ total + " number can be entered !");

        int max_number_of_questions = total;
        done_btn.setOnClickListener(v -> {
            if (checkConnection()) {
                int max_number = 0;
                String editTextString = editText.getText().toString();
                if (!editTextString.equals("")) {
                    max_number = Integer.parseInt(editTextString);
                }
                if (max_number > max_number_of_questions || max_number == 0) {
                    Toast.makeText(getApplicationContext(), "Enter a number between 0 and " + (max_number_of_questions + 1), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NumInputActivity.this, AlgoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("chapterList", chapters);
                    bundle.putInt("test_Questions", max_number);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Please connect to a network .", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}