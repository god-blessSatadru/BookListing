package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BookListingActivity extends AppCompatActivity {
    String mURL;
    String mBooks;
    String mTitle, mAuthor;
    public static final String LOG_CAT = "Showing Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);
        Bundle bundle = getIntent().getExtras();
        mBooks = bundle.getString(getResources().getString(R.string.book));
        mURL = getResources().getString(R.string.URL) + mBooks + "&" + getResources().getString(R.string.max_result);
            BookAsyncTask task = new BookAsyncTask();
            task.execute();
    }



    private void mUpdateUserInterface(ArrayList<Book> book) {
        try {
            ListView bookListView = (ListView) findViewById(android.R.id.list);
            final BookAdapter adapter = new BookAdapter(this, book);
            bookListView.setEmptyView(findViewById(android.R.id.empty));
            bookListView.setAdapter(adapter);
        } catch (NullPointerException e) {
            Log.e(LOG_CAT, "No book name given", e);
        }
    }

    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {
        protected ArrayList<Book> doInBackground(URL... urls) {   // Create URL object
            URL url = createUrl(mURL);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
                Log.e(LOG_CAT, "Eexception in method doInBackGround", e);
            }
            ArrayList<Book> mBook = extractFeatureFromJSON(jsonResponse);
            return mBook;
        }

        protected void onPostExecute(ArrayList<Book> mBook) {
            if (mBook == null) {
                return;
            }

            mUpdateUserInterface(mBook);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_CAT, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(300000 /* milliseconds */);
                urlConnection.setConnectTimeout(350000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (Exception e) {
                Log.e(LOG_CAT, "Error in establishing HTTP cpnnection", e);

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<Book> extractFeatureFromJSON(String bookJSON) {
            if (TextUtils.isEmpty(bookJSON))
                return null;
            ArrayList<Book> mBook = new ArrayList<Book>();
            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray jArray = baseJsonResponse.getJSONArray("items");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject volumeInfo = jArray.getJSONObject(i).getJSONObject("volumeInfo");
                    mTitle = volumeInfo.getString("title");
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        mAuthor = authors.getString(j) + "\n";
                    }
                    mBook.add(new Book(mTitle, mAuthor));
                }

            } catch (JSONException e) {
                Log.e(LOG_CAT, "Error in parsing JSON", e);
            }
            return mBook;
        }
    }
}