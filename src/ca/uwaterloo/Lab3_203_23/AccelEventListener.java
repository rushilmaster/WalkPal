package ca.uwaterloo.Lab3_203_23;

import java.text.DecimalFormat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class AccelEventListener implements SensorEventListener {
	TextView steptv;

	static int stepcount = 0;
	boolean rising, top, falling, step = false;
	LineGraphView plotgraph;
	float smoothedAccelX = 0;
	float smoothedAccelY = 0;
	float smoothedAccelZ = 0;
	float accel[] = new float[3];
	static float stepsNorth, stepsEast = 0;

	public AccelEventListener(TextView step,
			LineGraphView viewgraph) {
		steptv = step;
		plotgraph = viewgraph;
	}

	public void onAccuracyChanged(Sensor s, int i) {
	}

	public void onSensorChanged(SensorEvent se) {
		if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			smoothedAccelX += (se.values[0] - smoothedAccelX) / 2.5;
			smoothedAccelY += (se.values[1] - smoothedAccelY) / 2.5;
			smoothedAccelZ += (se.values[2] - smoothedAccelZ) / 2.5;
			accel[0] = smoothedAccelX;
			accel[1] = smoothedAccelY;
			accel[2] = smoothedAccelZ;

			plotgraph.addPoint(accel);

			if (accel[1] >= 0.1 && accel[1] <= 0.2) {
				rising = true;
			}
			if (rising == true && accel[1] >= 0.6 && accel[1] <= 1.5) {
				top = true;
			}
			if (rising == true && top == true && accel[1] >= 0.6
					&& accel[1] <= 1.2) {
				falling = true;
				step = true;
			}
			if (step == true) {
				stepcount++;
				stepsNorth += Math.cos(Math.toRadians(Rotation.degree));
				stepsEast += Math.sin(Math.toRadians(Rotation.degree));
				step = false;
				rising = false;
				top = false;
				falling = false;
			}
			steptv.setText("Step Count: " + stepcount + "\nNorth: "
					+ roundTwoDecimals(stepsNorth) + "\nEast: "
					+ roundTwoDecimals(stepsEast));
		}
	}

	double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
