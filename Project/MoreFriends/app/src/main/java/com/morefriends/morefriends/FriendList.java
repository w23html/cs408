package com.morefriends.morefriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FriendList extends ActionBarActivity {

    /* The user list */
    private ArrayList<User> userlist;

    /* The current user */
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        currentUser = ParseUser.getCurrentUser();
        final ProgressDialog dialog = ProgressDialog.show(this, null, "Loading...");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Friend");
        query.whereEqualTo("me", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<String> ss = new LinkedList<String>();
                    for (ParseObject po : objects) {
                        ss.add(po.getString("friend"));
                    }
                    ParseUser.getQuery().whereContainedIn("objectId", ss).findInBackground(new FindCallback<ParseUser>() {

                                @Override
                                public void done(List<ParseUser> li, ParseException e) {
                                    dialog.dismiss();
                                    if (li != null) {
                                        if (li.size() == 0) {
                                            Toast.makeText(FriendList.this, "No User Found!", Toast.LENGTH_SHORT).show();
                                        }
                                        ArrayList<ParseUser> tempList = new ArrayList<ParseUser>(li);
                                        userlist = new ArrayList<User>();
                                        while (!tempList.isEmpty()) {
                                            ParseUser pu = tempList.remove(0);
                                            User u = new User(pu.getUsername());
                                            u.setAge(pu.getInt("age"));
                                            u.setDescription(pu.getString("description"));
                                            u.setEmail(pu.getEmail());
                                            u.setImage(pu.getBytes("image"));
                                            u.setObjectId(pu.getObjectId());
                                            userlist.add(u);
                                        }
                                        ListView lv = (ListView) findViewById(R.id.list);
                                        FriendListAdapter fla = new FriendListAdapter(FriendList.this, userlist);
                                        lv.setAdapter(fla);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(FriendList.this, MainPage.class).putExtra("name", userlist.get(position).getObjectId());
                                                startActivity(i);
                                            }
                                        });
                                    } else {


                                    }
                                }
                            });
                    }
            }
        });
    }
}
