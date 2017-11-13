package com.classy.hoth3.classy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Adapter;
import java.util.ArrayList;


public class favorites extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mListView = (ListView) findViewById(R.id.favoritesListView);
        final ArrayList<String> favoritesArray = new ArrayList<>();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("FavPref", 0);

        String anotherCard = "";
        int i = 0;
        while ( i < 20 )
        {
            anotherCard = pref.getString("" + i, null);
            if (anotherCard == null)
                break;
            favoritesArray.add(anotherCard);

            i++;
        }
        favoritesAdapter adapter = new favoritesAdapter(this, favoritesArray);
        mListView.setAdapter(adapter);

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

            String classinfo = (String) getItem(position);
            classinfo.replace('\n', ' ');
            int len = classinfo.length();

            char c;
            int firstNumber = 0;
            for ( ; firstNumber < len; firstNumber++ ) {
                c = classinfo.charAt(firstNumber);
                if ( c == '.' && classinfo.charAt(firstNumber-1) >= '0' && classinfo.charAt(firstNumber-1) <= '9') {
                    firstNumber -= 1;
                    break;
                }
            }

            if ( classinfo.length() == 0 || firstNumber + 12 > len)
                return rowView;

            titleTextView.setText( classinfo.substring(0,firstNumber));  /// Class Name and Professor Name

            String ratings = "Easy: " + classinfo.substring(firstNumber+3, firstNumber+6);
            ratings += " Work: " + classinfo.substring(firstNumber + 6, firstNumber + 9);
            ratings += " Clarity: " + classinfo.substring(firstNumber + 9, firstNumber + 12);
            ratings += " Helpful: " + classinfo.substring(firstNumber + 12, firstNumber + 15);

            subtitleTextView.setText(ratings);  // 4 other ratings
            detailTextView.setText(classinfo.substring(firstNumber, firstNumber + 3));  // Overall Star Rating

            return rowView;

        }
    }
}
