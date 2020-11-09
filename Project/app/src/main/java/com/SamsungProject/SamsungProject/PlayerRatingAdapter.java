package com.SamsungProject.SamsungProject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PlayerRatingAdapter extends ArrayAdapter<PlayerRating> {


    public PlayerRatingAdapter(@NonNull Context context, int resource, @NonNull List<PlayerRating> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;


    }

    public void clear(){
        super.clear();
    }
    private static final String TAG = "PlayerRatingAdapter";

    private Context mContext;
    int mResource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int place = getItem(position).getPosition();
        String name = getItem(position).getName();
        int rating = getItem(position).getRating();

        PlayerRating rating_of_player = new PlayerRating(name, rating, place);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView placeView = (TextView) convertView.findViewById(R.id.place);
        TextView nameView = (TextView) convertView.findViewById(R.id.name);
        TextView ratingView = (TextView) convertView.findViewById(R.id.rating);

        placeView.setText(Integer.toString(place));
        nameView.setText(name);
        ratingView.setText(Integer.toString(rating));

        return convertView;


    }
}