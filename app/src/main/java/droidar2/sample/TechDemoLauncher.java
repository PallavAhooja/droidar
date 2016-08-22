package droidar2.sample;

import com.droidar2.system.ArActivity;
import com.droidar2.system.ErrorHandler;
import com.droidar2.system.EventManager;
import com.droidar2.system.Setup;
import com.droidar2.tests.AndroidDeviceOnlyTests;
import com.droidar2.tests.EfficientListTests;
import com.droidar2.tests.GameLogicTests;
import com.droidar2.tests.GeoTests;
import com.droidar2.tests.GlTests;
import com.droidar2.tests.IOTests;
import com.droidar2.tests.SystemTests;
import com.droidar2.tests.WorldTests;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.droidar2.commands.ui.CommandShowToast;

import de.rwth.R;
import droidar2.sample.setups.CollectItemsSetup;
import droidar2.sample.setups.DebugSetup;
import droidar2.sample.setups.FarAwayPOIScenarioSetup;
import droidar2.sample.setups.FastChangingTextSetup;
import droidar2.sample.setups.GameDemoSetup;
import droidar2.sample.setups.GeoPosTestSetup;
import droidar2.sample.setups.GraphCreationSetup;
import droidar2.sample.setups.GraphMovementTestSetup;
import droidar2.sample.setups.LargeWorldsSetup;
import droidar2.sample.setups.LightningSetup;
import droidar2.sample.setups.PlaceObjectsSetup;
import droidar2.sample.setups.PlaceObjectsSetupTwo;
import droidar2.sample.setups.PositionTestsSetup;
import droidar2.sample.setups.SensorTestSetup;
import droidar2.sample.setups.StaticDemoSetup;

public class TechDemoLauncher extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.demoselector);

	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("DemoScreen onResume");
		LinearLayout l = ((LinearLayout) findViewById(R.id.demoScreenLinView));
		l.removeAllViews();

		showSetup("GeoPosTestSetup", new GeoPosTestSetup());
		showSetup("Demo Setup", new StaticDemoSetup());
		showSetup("Animation Demo", new DebugSetup());
		showSetup("Game Demo", new GameDemoSetup());
		showSetup("'Too far away' scenario", new FarAwayPOIScenarioSetup());
		showSetup("Large worlds", new LargeWorldsSetup());
		showSetup("Changing text Demo", new FastChangingTextSetup());
		showSetup("Lightning Demo", new LightningSetup());
		showSetup("Collecting Items Demo", new CollectItemsSetup());
		showSetup("Placing objects Demo", new PlaceObjectsSetup());
		showSetup("Placing objects Demo 2", new PlaceObjectsSetupTwo());
		showSetup("Graph Movement Test", new GraphMovementTestSetup());
		showSetup("Graph creation Test", new GraphCreationSetup());
		showSetup("Sensor Processing Demo", new SensorTestSetup());
		showSetup("Position com.droidar2.tests", new PositionTestsSetup());

		l.addView(new SimpleButton(
				"deviceHasLargeScreenAndOrientationFlipped = "
						+ EventManager
								.deviceHasLargeScreenAndOrientationFlipped(this)) {
			@Override
			public void onButtonPressed() {

			}
		});

		l.addView(new SimpleButton("Run com.droidar2.tests") {
			@Override
			public void onButtonPressed() {
				runTests();
			}
		});

		l.addView(new SimpleButton("Load test UI") {
			@Override
			public void onButtonPressed() {
				setContentView(R.layout.test_layout);
			}
		});

	}

	private void showSetup(String string, final Setup aSetupInstance) {
		((LinearLayout) findViewById(R.id.demoScreenLinView))
				.addView(new SimpleButton(string) {
					@Override
					public void onButtonPressed() {
						Activity theCurrentActivity = TechDemoLauncher.this;
						ArActivity.startWithSetup(theCurrentActivity,
								aSetupInstance);
					}
				});
	}

	private abstract class SimpleButton extends Button {
		public SimpleButton(String text) {
			super(TechDemoLauncher.this);
			setText(text);
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onButtonPressed();
				}
			});
		}

		public abstract void onButtonPressed();
	}

	private void runTests() {
		// execute all com.droidar2.tests defined in the ARTestSuite:
		try {

			com.droidar2.system.EventManager.getInstance().registerListeners(this, true);

			// new ThreadTest().run();
			// new MemoryAllocationTests().run();
			// new NetworkTests().run();

			new SystemTests().run();
			new EfficientListTests().run();
			new GeoTests().run();
			new IOTests(this).run();
			new WorldTests().run();
			new AndroidDeviceOnlyTests(this).run();
			new GameLogicTests().run();
			new GlTests().run();

			new CommandShowToast(this, "All com.droidar2.tests succeded on this device :)")
					.execute();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorHandler.showErrorLog(this, e, true);
		}
	}

}
