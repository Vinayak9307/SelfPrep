package com.example.selfprep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AlgoActivity extends AppCompatActivity {

    ArrayList<Chapter> chapters = new ArrayList<>();
    ArrayList<Chapter> tempChapters = new ArrayList<>();
    AppCompatButton back_btn;
    //Algorithm variables
    int testQuestions = 0;
    ArrayList<Integer> pickedQuestions = new ArrayList<>();
    StorageReference mStorageReference;
    OutputStream outputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);


        testQuestions = getIntent().getIntExtra("test_Questions",0);
        tempChapters = getIntent().getParcelableArrayListExtra("chapterList");
        back_btn = findViewById(R.id.A4back_btn);

        read_csv();

        pickQuestions();


        back_btn.setOnClickListener(v -> finish());

        for(int i = 0 ; i < pickedQuestions.size() ; i++){
            String qName = pickedQuestions.get(i) + ".png";
            if(!fileIsAlreadyPresent(qName)){
                downloadQuestion(qName);
            }
        }

        int delay;
        if(pickedQuestions.size() > 100){
            delay = 10000;
        }
        else{
            delay = 5000;
        }

        new Handler().postDelayed(()->{
        Intent intent = new Intent(AlgoActivity.this , FinalActivity.class);
        intent.putIntegerArrayListExtra("pickedQuestions" , pickedQuestions);
        startActivity(intent);
        }
        ,delay);

    }

    public static int random(int a, int b) {
        return (int) (Math.random() * (b + 1) + a);
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

    boolean fileIsAlreadyPresent(String qName){
            File inputFile = new File(Environment.getExternalStorageDirectory() + "/SelfPrep/questions/"+qName.replace(".png",".vin"));
            return inputFile.exists();
    }

    void downloadQuestion(String qName){
        long maxDownloadSize = 5L*1024*1024;
        String downloadName = qName.replace(".png",".vin");

        mStorageReference = FirebaseStorage.getInstance().getReference().child("questions/" + qName);
        mStorageReference.getBytes(maxDownloadSize).addOnSuccessListener(bytes -> {
            Bitmap temp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

            File iDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SelfPrep/questions/");
            File imgFile = new File(iDir , downloadName);

            try {
                FileOutputStream mOutputStream = new FileOutputStream(imgFile);
                temp.compress(Bitmap.CompressFormat.PNG , 100 , mOutputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e)  {
                    e.printStackTrace();
                }
            }

        });
    }

    void pickQuestions(){
        for(int i = 0 ; i < chapters.size() ; i++){
            chapters.get(i).done = tempChapters.get(i).done;
        }
        int totalQuestions = 0;
        for(Chapter c:chapters){
            if(c.done){
                totalQuestions += c.total;
            }
        }

        for(Chapter c: chapters){
            if(c.done){
                float testQuestionsFromThisChapter = ((float)c.total * testQuestions)/totalQuestions;

                for(int i = 0 ; i < c.topics ; i++){

                    float maxQuestions = c.questions.get(i);

                    float desiredPercentage = Math.round((maxQuestions * 100) / c.total);

                    int desiredNoOfQuestionsFromThisTopic = Math.round((desiredPercentage * testQuestionsFromThisChapter)/100);
                    if(desiredNoOfQuestionsFromThisTopic == 0){
                        desiredNoOfQuestionsFromThisTopic = 1;
                    }
                    for(int j = 0 ; j < desiredNoOfQuestionsFromThisTopic ; j++){

                        int picked = c.index*1000 + i * 100 + random(1, Math.round(maxQuestions) - 1);

                            if (pickedQuestions.contains(picked)) {
                                j--;
                            } else {
                                pickedQuestions.add(picked);
                            }
                    }
                }
            }
        }

    }
}
