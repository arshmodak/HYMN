package a2.arshmodak.com.musicplayerapp;

import  android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayerControls extends AppCompatActivity implements View.OnClickListener
{
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    Button b_next, b_prev, b_fwd, b_rwd, b_play, b_playlist;
    SeekBar seekBar;
    CheckBox shuffle, repeat;
    int position;
    int currentPosition, totalDuration;
    Uri uri;
    String name;
    TextView tv;
    Boolean isShuffle = false;
    Boolean isRepeat = false;
    Thread updateSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_controls);
        b_next = (Button) findViewById(R.id.button5);
        b_prev = (Button) findViewById(R.id.button);
        b_fwd = (Button) findViewById(R.id.button4);
        b_rwd = (Button) findViewById(R.id.button2);
        b_play = (Button) findViewById(R.id.button3);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        b_playlist = (Button) findViewById(R.id.button16);
        shuffle=(CheckBox)findViewById(R.id.checkBox);
        repeat=(CheckBox)findViewById(R.id.checkBox2);
        tv= (TextView) findViewById(R.id.textView3);


        b_next.setOnClickListener(this);
        b_prev.setOnClickListener(this);
        b_fwd.setOnClickListener(this);
        b_rwd.setOnClickListener(this);
        b_play.setOnClickListener(this);
        b_playlist.setOnClickListener(this);


        if (mp != null) {
            mp.stop();
            mp.release();
        }

        updateSeekBar = new Thread()
        {
            public void run()
            {
                totalDuration = mp.getDuration();
                currentPosition = 0;
                seekBar.setMax(totalDuration);
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (currentPosition == totalDuration)
                {
                    position=(position+1)%mySongs.size();
                    currentPosition=0;
                    seekBar.setProgress(currentPosition);
                    name = mySongs.get(position).getName().toString();
                    uri=Uri.parse(mySongs.get(position).toString());
                    mp=MediaPlayer.create(PlayerControls.this,uri);
                    mp.start();
                    //addNotification();
                }
            }
        };
        shuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(shuffle.isChecked())
                {
                    isShuffle=true;
                    Toast.makeText(PlayerControls.this, "Shuffle Enabled", Toast.LENGTH_SHORT).show();
                    repeat.setChecked(false);
                    isRepeat=false;
                }
                else
                {
                    isShuffle=false;
                    Toast.makeText(PlayerControls.this, "Shuffle Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (repeat.isChecked())
                {
                    isRepeat=true;
                    Toast.makeText(PlayerControls.this, "Repeat Enabled", Toast.LENGTH_SHORT).show();
                    isShuffle=false;
                    shuffle.setChecked(false);
                }
                else
                {
                    isRepeat=false;
                    Toast.makeText(PlayerControls.this, "Repeat Diabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        position = bundle.getInt("pos", 0);
        uri = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(PlayerControls.this, uri);

        mp.start();
       // addNotification();
       // name = mySongs.get(position).getName().toString();
        //tv.setText("Now Playing: "+name);

        seekBar.setMax(mp.getDuration());

        updateSeekBar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });

    }
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button3:
                if(mp.isPlaying())
                {
                    b_play.setBackgroundResource(R.drawable.play);
                    mp.pause();
                }
                else
                {
                    b_play.setBackgroundResource(R.drawable.pause);
                    mp.start();
                }
                break;
            case R.id.button4:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.button2:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.button5:
                mp.stop();
                mp.release();
                if(isRepeat)
                {
                    uri = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(PlayerControls.this, uri);
                    mp.start();
                    currentPosition=0;
                    seekBar.setProgress(currentPosition);
                    //addNotification();
                    name = mySongs.get(position).getName().toString();
                    tv.setText("Now Playing: " + name);
                }

                else if(isShuffle)
                {
                    Random rand = new Random();
                    position=rand.nextInt((mySongs.size()-1)-0 + 1)+0;
                    uri=Uri.parse(mySongs.get(position).toString());
                    mp=MediaPlayer.create(PlayerControls.this,uri);
                    mp.start();
                    currentPosition=0;
                    seekBar.setProgress(currentPosition);
                    //addNotification();
                    name = mySongs.get(position).getName().toString();
                    tv.setText("Now Playing: "+name);
                }
                else {
                    position = (position + 1) % mySongs.size();
                    uri = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(PlayerControls.this, uri);
                    mp.start();
                    //addNotification();
                    name = mySongs.get(position).getName().toString();
                    tv.setText("Now Playing: " + name);
                }
                updateSeekBar.start();
                break;
            case R.id.button:
                mp.stop();
                mp.release();
                position=(position-1)<0 ?mySongs.size()-1: position-1;
                uri=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(PlayerControls.this, uri);
                mp.start();
                break;
            case R.id.button16:
                Intent i=new Intent(PlayerControls.this, MusicPlayerApp.class);
                startActivity(i);
                break;


        }


    }
   /*public void addNotification()
    {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Now Playing: ");
        name = mySongs.get(position).getName().toString();
        builder.setContentText(""+name);
        builder.setSmallIcon(R.drawable.earp);

        /*Intent notificationIntent = new Intent(this,Player.class);
        PendingIntent ci = PendingIntent.getActivity(this,0,notificationIntent,0);
        builder.setContentIntent(ci);*/

        //NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.notify(0,builder.build());
   // }


}
