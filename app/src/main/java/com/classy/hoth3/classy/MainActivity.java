package com.classy.hoth3.classy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daprlabs.aaron.swipedeck.SwipeDeck;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String subject, url;
    final ArrayList<String> mData = new ArrayList<>();
    final ArrayList<Float> mRatings = new ArrayList<>();
    final SwipeDeckAdapter adapter = new SwipeDeckAdapter(mData, mRatings, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        if (cardStack != null) {
            cardStack.setAdapter(adapter);
        }
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + positionInAdapter);
            }

            @Override
            public void cardSwipedRight(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positionInAdapter);

            }
        });

        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        //example of buttons triggering events on the deck
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);
            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
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

                    for (Element selectedClass : classes) {
                        //Elements profs = selectedClass.select("span[class=prof name]")
                        String prof = selectedClass.select("span[class=prof name]").first().ownText();
                        Elements ratings = selectedClass.select("td[class=rating-cell]");

                        String title = selectedCourse.select("div[class=title circle]").first().ownText();
                        String cardInfo = title + "/" + prof + "/";

                        mData.add(cardInfo);

                        // for each rating
                        for (Element rating : ratings) {
                            Log.e(title + " " + prof, rating.ownText() + ": " + rating.select("span.rating").first().ownText());
                            //cardInfo += rating.ownText() + "/" + rating.select("span.rating").first().ownText();
                            String r = rating.select("span.rating").first().ownText();
                            mRatings.add(r.equals("N/A") ? -1 : Float.parseFloat(r));
                        }
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
        }
    }
}