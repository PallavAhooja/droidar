package droidar2.sample;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.droidar2.system.ArActivity;

import de.rwth.R;
import droidar2.sample.setups.GeoPosTestSetup;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        ArActivity.startWithSetup(MainActivity.this,
                new GeoSetup(MainActivity.this,28.411882, 77.041739, "Beautiful Girl"));





    }
}
