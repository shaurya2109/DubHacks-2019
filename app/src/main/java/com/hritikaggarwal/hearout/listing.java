package com.hritikaggarwal.hearout;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat; //NEW
import java.util.Date; //NEW

import java.util.ArrayList;
import java.util.Locale;

// this is the class where we are listing to the conversation and also
public class listing extends AppCompatActivity {

    private ListAdapter adapter;
    private ListAdapter timeAdapter;
    private ArrayList<String> listItems;
    private ArrayList<String> listTimes;
    private ListView listView;
    private boolean noteCreate;
    private String speakers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        listItems = new ArrayList<String>();
        listTimes = new ArrayList<String>();
        speakers = "_____";

        // this thing is used in the display of the items in the list
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, listItems);

        // this is the place where we need to display the things into the field in the view of the app
        listView = (ListView) findViewById(R.id.listview);

        indenter();

        Button btn = (Button) findViewById(R.id.hear_again);
        Log.d("titu",listItems.toString());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indenter();
            }
        });

        Button btn2 = (Button) findViewById(R.id.save);

        Button btn3 = (Button) findViewById(R.id.todoList);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteCreate = true;
                Intent intent = new Intent(listing.this, ToDoList.class);
                startActivity(intent);
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = "";
                int size = listItems.size();
                for (int i = 0; i < size; i++) {
                    l += "Conversation with " + speakers +  " on " + listTimes.get(i) + "- " + listView.getItemAtPosition(i).toString() + '\n';
                }
                noteCreate = true;
                // here we need to add the date of the conversation for easy understanding of the user
                createNote("Conversation with " + speakers, l);
            }
        });

        // this function defines what happens when you long press each note
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                noteCreate = true;
                String l =  "Conversation with " + speakers +  " on " + listTimes.get(pos) + "- " + listView.getItemAtPosition(pos).toString();
                createNote("Important Message", l);
                return true;
            }
        });

    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
        // intent here
        Log.d("onStop", "The thing works"); //REMOVE THIS
        if(!noteCreate){
            Intent intent = new Intent(listing.this, ListeningPage.class);
            startActivity(intent);
        }
        noteCreate = false;  // New added

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView textView = findViewById(R.id.NameDisplay);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    String[] speech = result.get(0).trim().split(" ");
                    if (speech[0].equalsIgnoreCase("please") && speech.length > 1) {
                        int pos = 1;
                        String task = "", deadline = "none";
                        while (!speech[pos].equalsIgnoreCase("by") && pos < speech.length) {
                            task+=speech[pos] + " ";
                            pos++;
                        }
                        if (pos < speech.length) {
                            deadline = speech[pos + 1];
                        }
                        pos+=2;
                        while(pos < speech.length) {
                            deadline += " " + speech[pos];
                            pos++;
                        }
                        Log.d("titu Task", task);
                        Log.d("titu Deadline", deadline);
                    } else {
                        listItems.add(result.get(0));
                        listTimes.add(currentDateandTime);

                        listView.setAdapter(adapter);
                    }

                    indenter();
                }

                break;
        }
    }

    public void createNote(String subject, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    public void indenter() {
        // TEXT TO SPEECH AFTER LISTENING STARTS *************
        Intent intent2 = new Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent2.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent2.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent2.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent2, 10);
        } else {
            Toast.makeText(listing.this, "Your Device Doesn't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
        Log.d("titu",listItems.toString()); //REMOVE THIS
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(listing.this, ListeningPage.class);
        startActivity(intent);

        super.onBackPressed();
    }
}