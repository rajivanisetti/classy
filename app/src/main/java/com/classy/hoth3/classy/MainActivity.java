package com.classy.hoth3.classy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.daprlabs.aaron.swipedeck.SwipeDeck;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    String subject, url;
    final ArrayList<String> mData = new ArrayList<>();
    final ArrayList<Float> mRatings = new ArrayList<>();
    final SwipeDeckAdapter adapter = new SwipeDeckAdapter(mData, mRatings, this);

    ProgressDialog progressDialog;

    int overall, easy, work, clarity, help;
/**
    SharedPreferences pref = getApplicationContext().getSharedPreferences("FavPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
**/
    int favoritesSaved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("FavPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        if (cardStack != null) {
            cardStack.setAdapter(adapter);
        }
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + positionInAdapter);
                Log.e("index", cardStack.getAdapterIndex() + "");
            }

            @Override
            public void cardSwipedRight(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positionInAdapter);
                int index = (int) positionInAdapter;
                String toSave = "";
                toSave += mData.get(index);
                index = index * 5;
                for ( int i = 0; i < 5; i++ )
                {
                    toSave += mRatings.get(index);
                }
                editor.putString("" + favoritesSaved, toSave);
                favoritesSaved += 1;
                editor.commit();

                notifyUser(toSave);
            }
        });

        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        //example of buttons triggering events on the deck
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(240);
            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(240);
            }
        });

        View mDecorView = getWindow().getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");

        overall = intent.getIntExtra("overall", 0);
        easy = intent.getIntExtra("easy", 0);
        work = intent.getIntExtra("work", 0);
        clarity = intent.getIntExtra("clarity", 0);
        help = intent.getIntExtra("help", 0);

        switch (subject) {
            case "Computer Science":
                url = "http://www.bruinwalk.com/search/?category=classes&dept=52";
                break;
            case "Electrical Engineering":
                url = "http://www.bruinwalk.com/search/?category=classes&dept=64";
                break;
            case "Physics":
                url = "http://www.bruinwalk.com/search/?category=classes&dept=147";
                break;
            case "Scandinavian":
                url = "http://www.bruinwalk.com/search/?category=classes&dept=164";

        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Stay classy while we load...");
        progressDialog.show();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(url)
                        .header("Accept-Encoding", "gzip, deflate")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .timeout(10 * 1000)
                        .maxBodySize(0)
                        .get();

                Elements courses = doc.select("div[class=course-result bruinwalk-card hide-for-small-only]");
                Elements links = courses.select("a[href]:has(h1)");

                // for each course (e.g. CS 133)
                for (Element link : links) {
                    Document selectedCourse = Jsoup.connect(link.attr("abs:href"))
                            .timeout(10 * 1000)
                            .get();
                    Elements elements = selectedCourse.select("div.hide-for-small-only");
                    Elements classes = elements.select("table[class=result]");

                    boolean add;

                    for (Element selectedClass : classes) {
                        String prof = selectedClass.select("span[class=prof name]").first().ownText();
                        Elements ratings = selectedClass.select("td[class=rating-cell]");

                        String title = selectedCourse.select("div[class=title circle]").first().ownText();
                        String cardInfo = title + "\n" + prof;

                        add = true;

                        // for each rating
                        for (Element rating : ratings) {
                            String name = rating.ownText();
                            String r = rating.select("span.rating").first().ownText();
                            Float f = r.equals("N/A") ? -1 : Float.parseFloat(r);

                            switch (name) {
                                case "Overall":
                                    if (f < (overall - 0.2))
                                        add = false;
                                    break;
                                case "Easiness":
                                    if (f < (easy - 0.2))
                                        add = false;
                                    break;
                                case "Workload":
                                    if (f < (work - 0.2))
                                        add = false;
                                    break;
                                case "Clarity":
                                    if (f < (clarity- 0.2))
                                        add = false;
                                    break;
                                case "Helpfulness":
                                    if (f < (help- 0.2))
                                        add = false;
                                    break;
                            }

                            if (add)
                                mRatings.add(f);
                        }
                        if (add)
                            mData.add(cardInfo);
                    }
                }

            } catch (IOException e) {
                Log.e("Main", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            progressDialog.hide();
        }
    }

    public void notifyUser(String msg) {
        final String NOTIFICATION_CHANNEL_ID = "4655";
        // Notification Channel
        CharSequence channelName = "classy.";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "classy.", importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.WHITE);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.bowtie)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                .setSound(null)
                .setContentTitle("classy.")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false);

        notificationManager.notify(420, builder.build());
    }
}