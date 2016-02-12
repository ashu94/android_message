package com.example.juggernaut.message;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> phone = new ArrayList<String>();
    ArrayList<String> message = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        refreshSmsInbox();
        MessageAdapter adapter = new MessageAdapter(this,phone,message);
        listView.setAdapter(adapter);

    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        long timeMillis = smsInboxCursor.getColumnIndex("date");
        Date date = new Date(timeMillis);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        String dateText = format.format(date);

        if (indexBody < 0 || !smsInboxCursor.moveToFirst())
            return;
        //arrayAdapter.clear();
        do {
            /*String str = smsInboxCursor.getString(indexAddress) +" at "+
                    "\n" + smsInboxCursor.getString(indexBody) +dateText+ "\n";
            arrayAdapter.add(str);*/
            phone.add(smsInboxCursor.getString(indexAddress));
            message.add(smsInboxCursor.getString(indexBody));
        } while (smsInboxCursor.moveToNext());

    }

}

class MessageAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();

    MessageAdapter(Context context, ArrayList<String> title, ArrayList<String> message){

        super(context, R.layout.single_row, R.id.textView, title);
        this.context = context;
        this.title = title;
        this.desc = message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row, parent, false);
        TextView myTitle = (TextView) row.findViewById(R.id.textView);
        TextView myDescription = (TextView) row.findViewById(R.id.textView2);

        myTitle.setText(title.get(position));
        myDescription.setText(desc.get(position));

        return row;
    }
}