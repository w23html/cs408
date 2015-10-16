package com.morefriends.morefriends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by w23html on 10/16/15.
 */
public class FriendListAdapter extends BaseAdapter {

    ArrayList<User> list = new ArrayList<User>();
    LayoutInflater inflater;
    Context context;

    public FriendListAdapter(Context c, ArrayList<User> list) {
        this.context = c;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public User getItem(int pos) {
        return this.list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View convert, ViewGroup parent) {
        ViewHolder vh;

        if (convert == null) {
            convert = inflater.inflate(R.layout.list_bar, parent, false);
            vh = new ViewHolder(convert);
            convert.setTag(vh);
        }
        else {
            vh = (ViewHolder) convert.getTag();
        }
        User currentUser = getItem(pos);
        Bitmap bm;
        if (currentUser.getImage() == null) {

        } else {
            bm = BitmapFactory.decodeByteArray(currentUser.getImage(), 0, currentUser.getImage().length);
            vh.avater.setImageBitmap(bm);
        }
        vh.name.setText(currentUser.getName());
        return convert;
    }

    private class ViewHolder {
        ImageView avater;
        TextView name;

        public ViewHolder(View convert) {
            avater = (ImageView) convert.findViewById(R.id.avatar);
            name = (TextView) convert.findViewById(R.id.username);
        }
    }
}
