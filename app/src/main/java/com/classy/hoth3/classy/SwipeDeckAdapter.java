package com.classy.hoth3.classy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class SwipeDeckAdapter extends BaseAdapter {

    private List<String> data;
    private List<Float> ratings;
    private Context context;

    public SwipeDeckAdapter(List<String> data, List<Float> ratings, Context context) {
        this.data = data;
        this.ratings = ratings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.card_view, parent, false);
        }

        //ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
        //Picasso.with(context).load(R.drawable.ucla).fit().centerCrop().into(imageView);

        int ratingPosition = position * 5;

        RatingBar overall = v.findViewById(R.id.overall);
        RatingBar easy = v.findViewById(R.id.easy);
        RatingBar work = v.findViewById(R.id.work);
        RatingBar clarity = v.findViewById(R.id.clarity);
        RatingBar help = v.findViewById(R.id.help);

        overall.setMax(5);
        overall.setStepSize(0.1f);
        overall.setRating(ratings.get(ratingPosition));

        easy.setMax(5);
        easy.setStepSize(0.1f);
        easy.setRating(ratings.get(ratingPosition + 1));

        work.setMax(5);
        work.setStepSize(0.1f);
        work.setRating(ratings.get(ratingPosition + 2));

        clarity.setMax(5);
        clarity.setStepSize(0.1f);
        clarity.setRating(ratings.get(ratingPosition + 3));

        help.setMax(5);
        help.setStepSize(0.1f);
        help.setRating(ratings.get(ratingPosition + 4));

        TextView textView = (TextView) v.findViewById(R.id.sample_text);
        String item = (String) getItem(position);
        textView.setText(item);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
            }
        });
        return v;
    }
}