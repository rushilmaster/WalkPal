package ca.uwaterloo.Lab3_203_23;

import java.util.Arrays;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import mapper.*;

public class MainActivity extends Activity {
	TextView stepvalue;
	TextView rotationvalue;
	LineGraphView graph;
	Button stepButton;
	MapView mv;
	NavigationalMap map;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		mv.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item)
				|| mv.onContextItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		map = MapLoader.loadMap(Environment.getExternalStorageDirectory(),"Lab-room.svg");
		//mv.setMap(map);

		mv = new MapView(getApplicationContext(), 1000, 800, 57, 60);
		registerForContextMenu(mv);
		mv.setMap(map);

		rotationvalue = new TextView(getApplicationContext());
		stepvalue = new TextView(getApplicationContext());
		graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList(
				"x", "y", "z"));
		stepButton = new Button(this);

		stepButton.setText("Reset");

		stepButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AccelEventListener.stepcount = 0;
				AccelEventListener.stepsNorth = 0;
				AccelEventListener.stepsEast = 0;
			}
		});

		LinearLayout LL = (LinearLayout) findViewById(R.id.layout);
		LL.setOrientation(LinearLayout.VERTICAL);

		LL.addView(mv);
		//LL.addView(graph);
		LL.addView(rotationvalue);
		LL.addView(stepvalue);
		LL.addView(stepButton);

		graph.setVisibility(View.VISIBLE);

		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

		Sensor accel = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		Sensor rotation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		SensorEventListener accelListener = new AccelEventListener(stepvalue, graph);
		SensorEventListener rotationListener = new Rotation(rotationvalue);
		
		sm.registerListener(accelListener, accel,
				SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(rotationListener, rotation,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}