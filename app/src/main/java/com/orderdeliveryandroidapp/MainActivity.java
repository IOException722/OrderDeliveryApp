package com.orderdeliveryandroidapp;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orderdeliveryandroidapp.services.APIClass;
import com.orderdeliveryandroidapp.services.MyResultReceiver;
import com.orderdeliveryandroidapp.services.Queries;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyResultReceiver.Receiver{
    TextView nameText, phoneText;
    TextView submitBtn;
    private Handler handler;
    private MyResultReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText = (TextView) findViewById(R.id.name_text);
        phoneText = (TextView) findViewById(R.id.phone_text);
        submitBtn =(TextView) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(this);
        handler = new Handler();
        myReceiver = new MyResultReceiver(handler);
        myReceiver.setReceiver(MainActivity.this);




    }

    private void callApi(final String url, final int type) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                new APIClass(type, myReceiver, nameText.getText().toString(), phoneText.getText().toString()).execute(url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_btn:
                callApi(Queries.QueriesUrl("registerUser", new String[]{}), 1);
                Toast.makeText(MainActivity.this, "Succesfully registered!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }
}
