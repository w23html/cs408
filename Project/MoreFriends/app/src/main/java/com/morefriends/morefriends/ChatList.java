package com.morefriends.morefriends;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ChatList extends Fragment {

    private Handler handler = new Handler();
    private String uid;
    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, ParseUser> umap = new HashMap<>();
    private List<Contact> glist = new LinkedList<>();
    private ListView lv;
    private ListAdapter la;

    public ChatList() {
        // Required empty public constructor
    }

    public void getMsgs() {
        ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
        query1.whereEqualTo("userId", uid);

        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo("id", uid);

        List<ParseQuery<Message>> list = new LinkedList<>();
        list.add(query1);
        list.add(query2);

        ParseQuery<Message> q = ParseQuery.or(list);
        q.orderByDescending("createdAt");
        q.setLimit(10000);
        q.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) {
                    for (Message m : objects) {
                        if (m.getUserId().compareTo(uid) == 0) {
                            if (!map.containsKey(m.getString("id"))) {
                                map.put(m.getString("id"), m.getBody());
                            }
                        } else {
                            if (!map.containsKey(m.getUserId())) {
                                map.put(m.getUserId(), m.getBody());
                            }
                        }
                    }
                    List<String> ss = new LinkedList<String>();
                    for (String s : map.keySet()) {
                        ss.add(s);
                    }
                    ParseQuery<ParseUser> puu = ParseUser.getQuery();
                    puu.whereContainedIn("objectId", ss);
                    puu.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                for (ParseUser u : objects) {
                                    umap.put(u.getObjectId(), u);
                                }
                                glist.clear();
                                for (String idd : map.keySet()) {
                                    if (umap.get(idd) != null) {
                                        Contact cc = new Contact(umap.get(idd).getString("nickname"), map.get(idd), null);
                                        cc.setObjectId(idd);
                                        glist.add(cc);
                                    }
                                }
                                la.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(), "Connection failed", Toast.LENGTH_SHORT).show();
                                map.clear();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Connection failed.", Toast.LENGTH_SHORT).show();
                    map.clear();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat_list, container, false);
        uid = ParseUser.getCurrentUser().getObjectId();
        lv = (ListView) root.findViewById(R.id.list);
        la = new ListAdapter(glist);
        lv.setAdapter(la);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMsgs();
               handler.postDelayed(this, 1000);
            }
        }, 1000);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ChatRoom.class);
                i.putExtra("id", glist.get(position).getObjectId());
                i.putExtra("fname", glist.get(position).getName());
                startActivity(i);
            }
        });
        return root;
    }


    public class ListAdapter extends BaseAdapter {

        private List<Contact> list;

        public ListAdapter(List<Contact> l) {
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inf = getActivity().getLayoutInflater();
                v = inf.inflate(R.layout.chat_list, null);
            }
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView msg = (TextView) v.findViewById(R.id.msg);
            //TextView date = (TextView) v.findViewById(R.id.date);

            name.setText(list.get(position).getName());
            msg.setText(list.get(position).getLastMessage());
            //date.setText(list.get(position).getDate());

            return v;
        }
    }

}
