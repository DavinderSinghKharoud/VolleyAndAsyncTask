package com.example.volleyandasynctask;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    TextView displayText;
    private RequestQueue Queue;
    private ProgressDialog dialog;
    private Button start;

    String jsonUrl = "https://api.myjson.com/bins/dv15n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(MainActivity.this);
        displayText = (TextView) findViewById(R.id.textView12);
        displayText.setMovementMethod(new ScrollingMovementMethod());
        Queue = Volley.newRequestQueue(getApplicationContext());
        displayText.setText("Fetch Data");
        start = findViewById(R.id.searchButton);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayText.setText("");
                DemoAsyncTask task = new DemoAsyncTask();
                task.execute(jsonUrl);
            }
        });


    }



    private class DemoAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Wait, doing something");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, strings[0], null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("companies");

                                for (int count = 0; count < jsonArray.length(); count++) {
                                    JSONObject companies = jsonArray.getJSONObject(count);

                                    String name = companies.getString("name");
                                    String jobs = companies.getString("jobs");

                                    displayText.append("Company Name" + (count + 1) + ": " + name +
                                            "\n Jobs: " + jobs + "\n");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Queue.add(request);

            return "process finished";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

        }
    }
}
