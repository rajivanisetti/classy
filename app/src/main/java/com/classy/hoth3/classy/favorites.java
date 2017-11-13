package com.classy.hoth3.classy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;


public class favorites extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mListView = (ListView) findViewById(R.id.favoritesListView);
      /**  final ArrayList<String> favoritesList = ;   /// change <String> to <class>

        favoritesAdapter adapter = new favoritesAdapter(this, favoritesList);
        mListView.setAdapter(adapter);
       **/
    }

    public class favoritesAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<String> mDataSource;

        public favoritesAdapter(Context context, ArrayList<String> items) {
            mContext = context;
            mDataSource = items;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return mDataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //4
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            View rowView = mInflater.inflate(R.layout.row_layout, parent, false);

            TextView titleTextView = (TextView) rowView.findViewById(R.id.title);

            // Get subtitle element
            TextView subtitleTextView = (TextView) rowView.findViewById(R.id.subinfo);

            // Get detail element
            TextView detailTextView = (TextView) rowView.findViewById(R.id.starrating);
/**
            Recipe recipe = (Recipe) getItem(position);

            titleTextView.setText(recipe.title);  /// Class Name and Professor Name
            subtitleTextView.setText(recipe.description);  // 4 other ratings
            detailTextView.setText(recipe.label);  // Overall Star Rating
 **/
            return rowView;

        }
    }
}
