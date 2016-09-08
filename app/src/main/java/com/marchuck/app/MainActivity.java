package com.marchuck.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.brianattwell.app.R;
import com.marchuck.Injectable;
import com.marchuck.WhenClicked;

@Injectable
public class MainActivity extends Activity {

    @WhenClicked(R.id.button)
    public void doWhenButtonClicked() {
        Toast.makeText(MainActivity.this, "BUTTON CLICKED", Toast.LENGTH_SHORT).show();
    }


    @WhenClicked(R.id.textView)
    public void doWhenTextViewClicked() {
        Toast.makeText(MainActivity.this, "TEXTVIEW CLICKED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MutableBean bean = new MutableBean(new Bean());

    }
}
