package com.foodatpenn;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {

    ArrayList<Post> posts;

    @Override
    protected String doInBackground(URL... urls) {
        try {
            URL url = urls[0];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            //parse thru everything
            Scanner in = new Scanner(url.openStream());

            posts = new ArrayList<Post>();

            while (in.hasNext()) {
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);

                String id = (String) jo.get("id");
                Date date = (Date) jo.get("date");
                String food = (String) jo.get("food");
                String description = (String) jo.get("description");
                String location = (String) jo.get("location");

                posts.add(new Post(food, description, location, date, Integer.parseInt(id)));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // WORK on UI thread here
                }
            });

        } catch (Exception e) {
            e.toString();
        }

        return null;
    }

    private void runOnUiThread(Runnable runnable) {
    }

    protected void onPostExecute(String result) {
        //Log.println(Log.INFO, "onPost", "here");
        super.onPostExecute(result);
    }
}
