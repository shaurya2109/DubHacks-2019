package com.hritikaggarwal.hearout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ToDoList extends AppCompatActivity {

    private ListView resultsListView;
    private TreeMap<String, String> taskAndDeadline;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Button btn3 = (Button) findViewById(R.id.prev_activity);

        // taskAndDeadline = new TreeMap<String, String>();
        gson = new Gson();

        SharedPreferences prefs = getSharedPreferences("com.hritikaggarwal.hearout", MODE_PRIVATE);
        String storedTaskAndDeadlineString = prefs.getString("taskAndDeadline", "oopsDidntWork");
        java.lang.reflect.Type type = new TypeToken<TreeMap<String, String>>(){}.getType();
        taskAndDeadline = gson.fromJson(storedTaskAndDeadlineString, type);


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoList.this, listing.class);
                startActivity(intent);
            }
        });

        resultsListView = (ListView) findViewById(R.id.listview);

        List<TreeMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"Task", "Deadline"},
                new int[]{R.id.text1, R.id.text2});

        Iterator it = taskAndDeadline.entrySet().iterator();
        while (it.hasNext())
        {
            TreeMap<String, String> resultsMap = new TreeMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("Task", pair.getKey().toString());
            resultsMap.put("Deadline", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        resultsListView.setAdapter(adapter);


    }

    //@Override
//    protected void onStop() {
//        // call the superclass method first
//        super.onStop();
//        // intent here
//        Intent intent = new Intent(ToDoList.this, ListeningPage.class);
//        startActivity(intent);
//    }
}
