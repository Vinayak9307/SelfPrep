package com.example.selfprep;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    final static int REQUEST_CODE = 333;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    if(SDK_INT >= Build.VERSION_CODES.R){
                        if(Environment.isExternalStorageManager()){
                            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        new Handler().postDelayed(()->{
            if(permission()) {
                createAppDirectory();
                Intent intent = new Intent( MainActivity.this , BookListActivity.class);
                startActivity(intent);
            }
            else{
                RequestPermission_Dialog();
            }
        } , 4000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0){
                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(read && write){
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        if(permission()) {
                            createAppDirectory();
                            Intent intent = new Intent( MainActivity.this , BookListActivity.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "You Denied Permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public boolean permission(){
        if(SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int write = ContextCompat.checkSelfPermission(getApplicationContext() , WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext() , READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        }
    }
    public void RequestPermission_Dialog(){
        if(SDK_INT >= Build.VERSION_CODES.R){
            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s" , getApplicationContext().getPackageName())));
                activityResultLauncher.launch(intent);
            }
            catch (Exception e){
                 Intent obj = new Intent();
                 obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                 activityResultLauncher.launch(obj);
            }
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this , new String[]{
                    WRITE_EXTERNAL_STORAGE , READ_EXTERNAL_STORAGE
            } , REQUEST_CODE);
        }
    }
    public void createAppDirectory(){
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SelfPrep/");
        if(!dir.exists()){
            dir.mkdir();
        }
        File iDir = new File(dir,"questions/");
        if(!iDir.exists()){
            iDir.mkdir();
        }
    }
}