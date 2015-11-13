package com.morefriends.morefriends;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends FragmentActivity {


    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private String[] menuItems = new String[]{"My Profile", "Log Out"};
    private CircleImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         mLeftDrawer = (ListView) findViewById(R.id.left_drawer);
         View v = getLayoutInflater().inflate(R.layout.sidemenuheader, null);
         ((TextView)v.findViewById(R.id.name)).setText(ParseUser.getCurrentUser().getString("nickname"));
         ((TextView)v.findViewById(R.id.email)).setText(ParseUser.getCurrentUser().getEmail());
         if (ParseUser.getCurrentUser().getBytes("image") != null) {
             iv = (CircleImageView) v.findViewById(R.id.image);
             iv.setImageBitmap(BitmapFactory.decodeByteArray(ParseUser.getCurrentUser().getBytes("image"), 0, ParseUser.getCurrentUser().getBytes("image").length));
         } else {
            iv = (CircleImageView) v.findViewById(R.id.image);
             iv.setImageResource(R.drawable.anon_54);
         }
         mLeftDrawer.addHeaderView(v);
         mLeftDrawer.setAdapter(new ArrayAdapter<String>(this, R.layout.my_textview, menuItems));
         mLeftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 if (position == 1) {
                     Intent i = new Intent(MainActivity.this, PersonalInfo.class);
                     startActivity(i);
                 } else if (position == 2) {
                     ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                         @Override
                         public void done(ParseException e) {
                             if (e == null) {
                                 Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                 startActivity(i);
                                 finish();
                             } else {
                                 Toast.makeText(MainActivity.this, "Connection failed, try again later.", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
             }
         });
        findViewById(R.id.button_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter());
        findViewById(R.id.button_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FriendList.class);
                startActivity(i);
            }
        });
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return (Fragment)(new ScreenSlidePageFragment());
            return new ChatList();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Search Friends" : "Chat List";
        }
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment create(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
            return view;
        }
    }

    public void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser().getBytes("image") != null) {
            iv.setImageBitmap(BitmapFactory.decodeByteArray(ParseUser.getCurrentUser().getBytes("image"), 0, ParseUser.getCurrentUser().getBytes("image").length));
        } else {
            iv.setImageResource(R.drawable.anon);
        }
    }

}
