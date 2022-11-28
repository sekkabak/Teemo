package org.sekka.teemo.ui.compass;

import static android.content.Context.SENSOR_SERVICE;

import androidx.lifecycle.ViewModelProvider;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sekka.teemo.R;
import org.sekka.teemo.databinding.FragmentCompassBinding;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class CompassFragment extends Fragment {
    private ImageView imageView;
    private TextView textView;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] floatGravity = new float[3];
    private float[] floatGeoMagnetic = new float[3];

    private float[] floatOrientation = new float[3];
    private float[] floatRotationMatrix = new float[9];


    private FragmentCompassBinding binding;
    private CompassViewModel mViewModel;

    public static CompassFragment newInstance() {
        return new CompassFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CompassViewModel compassViewModel = new ViewModelProvider(this).get(CompassViewModel.class);
        binding = FragmentCompassBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        imageView = binding.compass;
        textView = binding.speed;
        setupCompass();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupCompass() {
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGravity = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));


                // obliczanie dlugosci wektora
//                double magnitude = Math.sqrt((floatGravity[0]*floatGravity[0]) + (floatGravity[1]*floatGravity[1]) + (floatGravity[1]*floatGravity[1]));

                DecimalFormat df = new DecimalFormat("0.00");
                textView.setText("Akcelerometr:\n " + df.format(floatGravity[0]) + "x " + df.format(floatGravity[1]) + "y "+ df.format(floatGravity[2]) +"z");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatGeoMagnetic = event.values;

                SensorManager.getRotationMatrix(floatRotationMatrix, null, floatGravity, floatGeoMagnetic);
                SensorManager.getOrientation(floatRotationMatrix, floatOrientation);

                imageView.setRotation((float) (-floatOrientation[0]*180/3.14159));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }
}