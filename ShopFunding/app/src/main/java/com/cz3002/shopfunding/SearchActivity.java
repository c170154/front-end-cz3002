package com.cz3002.shopfunding;

import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SearchActivity extends AppCompatActivity {


   SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sv= (SearchView) findViewById(R.id.mSearch);
        RecyclerView rv= (RecyclerView) findViewById(R.id.myRecycler);

        //SET ITS PROPETRIES
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        //ADAPTER
        final MyAdapter adapter=new MyAdapter(this,getPlayers());
        rv.setAdapter(adapter);

        //SEARCH
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });



    }

    //ADD PLAYERS TO ARRAYLIST
    private ArrayList<Player> getPlayers()
    {
        ArrayList<Player> players=new ArrayList<>();
        Player p=new Player();
        p.setName("APPLE Iphone 5 32gb");
        p.setPos("IT Devices");
        p.setImg(R.drawable.shopee);
        players.add(p);

        p=new Player();
        p.setName("Men Summer Hip Hop Dovetail");
        p.setPos("Men's Wear");
        p.setImg(R.drawable.shopee);
        players.add(p);

        p=new Player();
        p.setName("Baseus Donut Wireless Charger");
        p.setPos("IT Devices");
        p.setImg(R.drawable.shopee);
        players.add(p);

        p=new Player();
        p.setName("APPLE Iphone 5 32gb");
        p.setPos("IT Devices");
        p.setImg(R.drawable.shopee);
        players.add(p);

        p=new Player();
        p.setName("Vegorrs Couple Installed Short-Sleeved");
        p.setPos("Men's Wear");
        p.setImg(R.drawable.shopee);
        players.add(p);

        p=new Player();
        p.setName("Marble Phone Case For Iphone");
        p.setPos("IT Devices");
        p.setImg(R.drawable.shopee);
        players.add(p);


        return players;
    }



}
