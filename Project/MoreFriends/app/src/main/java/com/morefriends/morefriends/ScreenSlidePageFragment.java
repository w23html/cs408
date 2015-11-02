package com.morefriends.morefriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class ScreenSlidePageFragment extends Fragment {

    void getRandomUser() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), null, "Searching...");
        ParseQuery<ParseUser> pq = ParseUser.getQuery();
        pq.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    int total = objects.size();
                    Random ran = new Random();
                    int idx = ran.nextInt(total);
                    Intent i = new Intent(getActivity(), MainPage.class).putExtra("name", objects.get(idx).getObjectId());
                    startActivity(i);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "No chats available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        rootView.findViewById(R.id.button_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomUser();
            }
        });
        return rootView;
    }
}
