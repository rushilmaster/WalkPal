package ca.uwaterloo.Lab3_203_23;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class Rotation implements SensorEventListener {
	TextView rotationtv;
	static double degree;
	public Rotation(TextView rotationview){
		rotationtv = rotationview;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		degree = Math.round(event.values[0]);
		rotationtv.setText("Degree: " + degree);
	}
}