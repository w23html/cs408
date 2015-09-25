package com.morefriends.morefriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;


public class ChatList extends Fragment {

    public ChatList() {
        // Required empty public constructor
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
        ListView lv = (ListView) root.findViewById(R.id.list);
        List<Contact> list = new LinkedList<>();
        list.add(new Contact("Jiaping", "I love 408", "9/25/2015"));
        list.add(new Contact("Jiaping", "I love 307", "9/25/2015"));
        list.add(new Contact("Jiaping", "I love 381", "9/25/2015"));
        ListAdapter la = new ListAdapter(list);
        lv.setAdapter(la);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ChatSender.class);
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
            TextView date = (TextView) v.findViewById(R.id.date);

            name.setText(list.get(position).getName());
            msg.setText(list.get(position).getLastMessage());
            date.setText(list.get(position).getDate());

            return v;
        }
    }

}
