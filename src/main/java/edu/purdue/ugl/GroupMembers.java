package edu.purdue.ugl;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import edu.purdue.ugl.adapters.BaseInflaterAdapter;
import edu.purdue.ugl.adapters.CardItemData;
import edu.purdue.ugl.adapters.inflaters.CardInflater;

public class GroupMembers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView list = (ListView)findViewById(R.id.list_view);
        list.addHeaderView(new View(this));
        list.addFooterView(new View(this));

        BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());

        CardItemData data = new CardItemData("Xiangyu Bu");
        adapter.addItem(data,false);

        data = new CardItemData("Chris Fulton");
        adapter.addItem(data,false);

        data = new CardItemData("Nick Houser");
        adapter.addItem(data,false);

        data = new CardItemData("Nishant Moorthy");
        adapter.addItem(data,false);

        data = new CardItemData("Adam Sill");
        adapter.addItem(data,false);

        list.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
