package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String mBookToSearch;
    public EditText mTypeText;
    public Button mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTypeText = (EditText) findViewById(R.id.ed1);
        mSearch = (Button) findViewById(R.id.bt1);
        mSearch.setOnClickListener(this);

    }
    protected boolean mTestConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    @Override
    public void onClick(View view) {
        mBookToSearch = mTypeText.getText().toString();
        // Log.v(getResources().getString(R.string.search_intent), mBookToSearch);
        if (mTestConnectivity()) {
            Intent intent = new Intent(MainActivity.this, BookListingActivity.class);
            intent.putExtra(getResources().getString(R.string.book), mBookToSearch);
            //Log.v ("Initiating intent","INitiating intent");
            startActivity(intent);
        }

        else {
            Toast.makeText(this, "THe device don't have internet connection", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
