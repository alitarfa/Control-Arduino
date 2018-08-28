package com.controller.tr.controllearduino.youtube;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.controller.tr.controllearduino.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

public class YouTubeActivity extends YouTubeBaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        myToolbar.setTitle("");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // to display the arrow of back


        recyclerView= (RecyclerView) findViewById(R.id.recycleView);

        ArrayList<InformationVideo> videos =new ArrayList<>();

        // put all the video that you want to see ^_^


        videos.add(new InformationVideo("DjBRScRAVrc&t","Connect a Relay and PIR Motion Sensor to an Arduino"));
        videos.add(new InformationVideo("nL34zDTPkcs","You can learn Arduino in 15 minutes"));
        videos.add(new InformationVideo("kLd_JyvKV4Y","Getting Started and Connected"));
        videos.add(new InformationVideo("KbDPgxHpgAA","Arduino Stepper Motor Tutorial"));
        videos.add(new InformationVideo("gi9mqIha8n0","The Arduino talking"));
        videos.add(new InformationVideo("e1FVSpkw6q4","LED Sequential Control"));
        videos.add(new InformationVideo("-Jsvg6u9CYI","Arduino Robotics"));
        videos.add(new InformationVideo("kewza7RyKMQ","Smartphone Controlled Arduino"));
        videos.add(new InformationVideo("oZiioFLEAss","Control FAN and LIGHT using TV remote"));
        videos.add(new InformationVideo("9Ms59ofSJIY","Arduino TFT LCD Touch Screen Tutorial"));
        videos.add(new InformationVideo("4fN1aJMH9mM","Arduino Tutorial 06: LDR with LED"));
        videos.add(new InformationVideo("Ur1tzMDP97g","Talk with your Arduino Board using with Voice "));
        videos.add(new InformationVideo("ZejQOX69K5M","Ultrasonic Sensor HC-SR04 and Arduino Tutorial"));

        //


        AdapterVideo adapterVideo=new AdapterVideo(this,videos);
        recyclerView.setAdapter(adapterVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }






class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.Holder>{

    ArrayList<InformationVideo> listVideo;
     LayoutInflater inflater;
     Context context;
     YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    public AdapterVideo(Context context,ArrayList<InformationVideo> listVideo) {
        this.context=context;
        inflater =LayoutInflater.from(context);
        this.listVideo=listVideo;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view=inflater.inflate(R.layout.card_video,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int i) {

          // make the title for all the list of the video

           holder.title.setText(listVideo.get(i).title);

        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(listVideo.get(i).url);

                // youTubePlayer.cuePlaylist("PLdCxNaJvZqgXVdyoO2x-qFCs-1caOIW6F");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


            holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.youTubePlayerView.initialize(Configure.API_YOUTUBE,onInitializedListener);
            }
        });

    }



    @Override
    public int getItemCount() {
        return listVideo.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        Button play;
        YouTubePlayerView youTubePlayerView;
        TextView title;
        public Holder(View itemView) {

            super(itemView);

            play= (Button) itemView.findViewById(R.id.playbtn);
            youTubePlayerView= (YouTubePlayerView) itemView.findViewById(R.id.view2);
            title= (TextView) itemView.findViewById(R.id.title_video);

        }
    }


}



}