package com.rajnish.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.SeekBar;

import com.rajnish.mymusicplayer.databinding.ActivityMainBinding;
import com.rajnish.mymusicplayer.databinding.ActivityPlaySongBinding;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();

    }

    ActivityPlaySongBinding binding;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    int possition;
    Thread updateSeek;
    String textContent;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityPlaySongBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentsong");
        binding.MusicnameTextview.setText(textContent);
        binding.MusicnameTextview.setSelected(true);
        possition = intent.getIntExtra("position",0);
         uri = Uri.parse(songs.get(possition).toString());

        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        binding.seekbar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try{
                    binding.next.performClick();



                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });




        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

         updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPossition = 0;
                try {
                    while (currentPossition < mediaPlayer.getDuration()) {
                        currentPossition = mediaPlayer.getCurrentPosition();
                        binding.seekbar.setProgress(currentPossition);
                        sleep(800);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        updateSeek.start();

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    binding.play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();

                }
                else {
                    binding.play.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
        binding.previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(possition!=0){
                    possition = possition-1;
                }
                else {
                    possition = songs.size() -1;
                }
                Uri uri = Uri.parse(songs.get(possition).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                binding.play.setImageResource(R.drawable.ic_baseline_pause_24);
                binding.seekbar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(possition).getName().toString();
                binding.MusicnameTextview.setText(textContent);

            }
        });
        binding.next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(possition!=songs.size()-1){
                    possition = possition+1;
                }
                else {
                    possition = 0;
                }
                Uri uri = Uri.parse(songs.get(possition).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                binding.play.setImageResource(R.drawable.ic_baseline_pause_24);
                binding.seekbar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(possition).getName().toString();
                binding.MusicnameTextview.setText(textContent);


            }
        });












    }

}