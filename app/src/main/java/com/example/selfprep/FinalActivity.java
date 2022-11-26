package com.example.selfprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class FinalActivity extends AppCompatActivity {

    Button backBtn;
    ImageView qImage;
    TextView nText;
    ArrayList<Integer> pickedQuestions = new ArrayList<>();
    Bitmap showingBitmap = null;
    FloatingActionButton prevBtn , nextBtn;
    int showingIndex = 0;
    String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        pickedQuestions = getIntent().getIntegerArrayListExtra("pickedQuestions");

        backBtn = findViewById(R.id.A5back_btn);
        qImage = findViewById(R.id.A5imageview);
        prevBtn = findViewById(R.id.A5prevbtn);
        nextBtn = findViewById(R.id.A5nextBtn);
        nText   = findViewById(R.id.A5textView);

        Collections.sort(pickedQuestions);

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(FinalActivity.this, ChapterListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        try {
            getImageFromStorage(pickedQuestions.get(showingIndex));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        qImage.setImageBitmap(showingBitmap);
        number = "" + (showingIndex+1);
        nText.setText(number);

        prevBtn.setOnClickListener(v -> {
            if(showingIndex > 0){
                showingIndex--;
                try {
                    getImageFromStorage(pickedQuestions.get(showingIndex));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                qImage.setImageBitmap(showingBitmap);
                number = "" + (showingIndex+1);
                nText.setText(number);
            }
        });
        nextBtn.setOnClickListener(v -> {
            if(showingIndex < pickedQuestions.size()-1){
                showingIndex++;
                try {
                    getImageFromStorage(pickedQuestions.get(showingIndex));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                qImage.setImageBitmap(showingBitmap);
                number = "" + (showingIndex+1);
                nText.setText(number);
            }
        });

    }

    void getImageFromStorage(int questionId) throws FileNotFoundException {
        File dir = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/SelfPrep/questions/"
        );
            File img = new File(dir , questionId + ".vin");
            try {
                InputStream mInputStream = new FileInputStream(img);
                showingBitmap = BitmapFactory.decodeStream(mInputStream);
            }
            catch(IOException e){
                e.printStackTrace();
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FinalActivity.this, ChapterListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}