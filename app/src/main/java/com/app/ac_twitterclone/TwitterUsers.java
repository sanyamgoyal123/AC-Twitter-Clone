package com.app.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter adapter;
    private String followedUser = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        setTitle("Twitter");

        FancyToast.makeText(this, "Welcome!!!," + ParseUser.getCurrentUser().getUsername(),
                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects.size() > 0 && e == null) {

                    for(ParseUser user : objects) {
                        arrayList.add(user.getUsername());
                    }
                    listView.setAdapter(adapter);

                    for (String twitterUser : arrayList) {
                        if(ParseUser.getCurrentUser().getList("fanOf") != null) {
                            if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                followedUser = followedUser + twitterUser;
                                listView.setItemChecked(arrayList.indexOf(twitterUser), true);
                                FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername() + " is following" + followedUser,
                                        FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(TwitterUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.sendTweetItem:
                Intent intent = new Intent(TwitterUsers.this, SendTweet.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedText = (CheckedTextView) view;
        if(checkedText.isChecked()) {
            FancyToast.makeText(this, arrayList.get(i) + " is now followed!!!",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", arrayList.get(i));

        } else {
            FancyToast.makeText(this, arrayList.get(i) + " is now UnFollowed!!!",
                    FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(i));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e ==  null) {
                    FancyToast.makeText(TwitterUsers.this, "changes saved!!!",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                }
            }
        });
    }
}