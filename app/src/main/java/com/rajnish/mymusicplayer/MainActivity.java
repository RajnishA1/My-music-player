package com.rajnish.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rajnish.mymusicplayer.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> mySongs = fetchsong(Environment.getExternalStorageDirectory());
                        String [] items = new String[mySongs.size()];
                        for(int i=0;i<mySongs.size();i++){
                            items[i] =mySongs.get(i).getName().replace(".mp3", "");

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        binding.songlistview.setAdapter(adapter);

                        binding.songlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(MainActivity.this,PlaySong.class);
                                String cureenstsong = binding.songlistview.getItemAtPosition(i).toString();
                                intent.putExtra("songList",mySongs);
                                intent.putExtra("currentsong",cureenstsong);
                                intent.putExtra("position",i);
                               startActivity(intent);

                            }
                        });
ther shouldbe




                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.cancelPermissionRequest();
                    }
                })
                .check();


    }

    public ArrayList<File>  fetchsong(File file){

      ArrayList  arrayList = new ArrayList<>();
      File [] songs = file.listFiles();
      if(songs !=null){
         for ( File myFile : songs){
             if (!myFile.isHidden()&& myFile.isDirectory()){
                 arrayList.addAll(fetchsong(myFile));
             }
             else {
                 if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                     arrayList.add(myFile);

                 }
             }
         }
      }
      return arrayList;
    }

}