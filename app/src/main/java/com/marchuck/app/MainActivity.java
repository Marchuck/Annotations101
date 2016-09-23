package com.marchuck.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.brianattwell.app.R;
import com.marchuck.Buildable;
import com.marchuck.Injectable;
import com.marchuck.WhenClicked;

@Injectable
public class MainActivity extends Activity {

    //  @WhenClicked(R.id.button)
    public void doWhenButtonClicked() {
        Toast.makeText(MainActivity.this, "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
    }


    @WhenClicked(R.id.textView)
    public void doWhenTextViewClicked() {
        Toast.makeText(MainActivity.this, "TEXT VIEW CLICKED", Toast.LENGTH_SHORT).show();
    }


    @Buildable()
    public void testo() {
        String s = "";

    }

    @Buildable()
    public void manifesto(String s, int c) {
        s = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

}