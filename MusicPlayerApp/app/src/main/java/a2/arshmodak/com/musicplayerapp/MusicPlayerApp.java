package a2.arshmodak.com.musicplayerapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayerApp extends AppCompatActivity
{
    ListView listView;
    String []items;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_app);
        final ArrayList<File> mySongs=findSongs(Environment.getExternalStorageDirectory());
        listView= (ListView) findViewById(R.id.lv);
        items= new String[mySongs.size()];
        /*for(int i=0; i<mySongs.size(); i++)
        {
            toast(mySongs.get(i).getName().toString());
            items[i]=mySongs.get(i).getName().toString();
        }*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MusicPlayerApp.this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Intent i= new Intent(MusicPlayerApp.this, PlayerControls.class);
                i.putExtra("pos", position);
                i.putExtra("songlist", mySongs);
                startActivity(i);

            }
        });
    }
    public ArrayList<File> findSongs(File file)
    {
        ArrayList<File> al= new ArrayList<File>();
        File[] files= file.listFiles();

        for(File singleFile :files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                al.addAll(findSongs(singleFile));

            }
            else
            {
                if(singleFile.getName().endsWith(".mp3"))
                {
                    al.add(singleFile);

                }

            }
        }
        return al;
    }
   /* public void toast(String text)
    {
        Toast.makeText(MusicPlayerApp.this, text, Toast.LENGTH_SHORT).show();
    }*/


}
