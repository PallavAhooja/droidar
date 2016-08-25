package droidar2.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.droidar2.system.ArActivity;
import com.droidar2.util.Support;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Toast.makeText(this,Support.supportsAR(this)+"-",Toast.LENGTH_SHORT).show();

        ArActivity.startWithSetup(MainActivity.this,
                new GeoSetup(MainActivity.this,28.411882, 77.041739, "Beautiful Girl"));







    }
}
