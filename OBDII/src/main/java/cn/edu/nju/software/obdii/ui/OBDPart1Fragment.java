package cn.edu.nju.software.obdii.ui;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;

/**
 * Show the following OBD information:
 * Voltage, coolant temperature, rotate speed of the engine
 */
public class OBDPart1Fragment extends Fragment {
    private static final long TOTAL_ANIMATION_TIME = 2000;
    private static final int MAX_VOLTAGE = 20;
    private static final int MAX_COOLANT_TEMPERATURE = 120;
    private static final int MAX_ROTATE_SPEED = 167;

    private TextView mVoltageText;
    private View mVoltageDiagram;
    private TextView mCoolantTemperatureText;
    private View mCoolantTemperatureDiagram;
    private TextView mRotateSpeedText;
    private View mRotateSpeedDiagram;

    private ImageView mArrowRight;

    private int mVoltage;
    private float mVoltageScale;
    private int mCoolantTemperature;
    private float mCoolantTemperatureScale;
    private int mRotateSpeed;
    private float mRotateSpeedScale;

    public OBDPart1Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obd_info_1, container, false);

        mVoltageText = (TextView) view.findViewById(R.id.voltage_value);
        mVoltageDiagram = view.findViewById(R.id.voltage_diagram);
        mCoolantTemperatureText = (TextView) view.findViewById(R.id.coolant_temperature_value);
        mCoolantTemperatureDiagram = view.findViewById(R.id.coolant_temperature_diagram);
        mRotateSpeedText = (TextView) view.findViewById(R.id.rotate_speed_value);
        mRotateSpeedDiagram = view.findViewById(R.id.rotate_speed_diagram);

        mArrowRight = (ImageView) view.findViewById(R.id.arrow_right);

        return view;
    }

    private void updateVoltage() {
        int currentVoltage = DataDispatcher.getInstance().getOBDData().getVoltage();
        updateOBDInfo(mVoltageText, getString(R.string.voltage_unit), mVoltageDiagram,
                mVoltageScale, mVoltage, currentVoltage, MAX_VOLTAGE,
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mVoltage = (Integer) valueAnimator.getAnimatedValue("value");
                        mVoltageScale = (Float) valueAnimator.getAnimatedValue("scale");
                    }
                }
        );
    }

    private void updateCoolantTemperature() {
        int currentCoolantTemperature = DataDispatcher.getInstance().getOBDData().getCoolantTemperature();
        updateOBDInfo(mCoolantTemperatureText, getString(R.string.temperature_unit), mCoolantTemperatureDiagram,
                mCoolantTemperatureScale, mCoolantTemperature, currentCoolantTemperature,
                MAX_COOLANT_TEMPERATURE, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mCoolantTemperature = (Integer) valueAnimator.getAnimatedValue("value");
                        mCoolantTemperatureScale = (Float) valueAnimator.getAnimatedValue("scale");
                    }
                }
        );
    }

    private void updateRotateSpeed() {
        int currentRotateSpeed = DataDispatcher.getInstance().getOBDData().getRotateSpeed();
        updateOBDInfo(mRotateSpeedText, getString(R.string.rotate_speed_unit), mRotateSpeedDiagram,
                mRotateSpeedScale, mRotateSpeed, currentRotateSpeed, MAX_ROTATE_SPEED,
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        mRotateSpeed = (Integer) valueAnimator.getAnimatedValue("value");
                        mRotateSpeedScale = (Float) valueAnimator.getAnimatedValue("scale");
                    }
                }
        );
    }

    private void updateOBDInfo(final TextView textView, final String unit, View diagram,
                               float fromScale, int fromValue, int toValue, int maxValue,
                               ValueAnimator.AnimatorUpdateListener listener) {
        if (toValue > maxValue) {
            toValue = maxValue;
        }
        float toScale = toValue * 1.0f / maxValue;
        long duration = (long) (Math.abs(toScale - fromScale) * TOTAL_ANIMATION_TIME);

        ScaleAnimation scaleAnimation = getScaleAnimation(diagram, fromScale, toScale, duration);
        ValueAnimator valueAnimator = getValueAnimator(textView, unit, fromScale,
                fromValue, toValue, toScale, duration);
        valueAnimator.addUpdateListener(listener);

        scaleAnimation.start();
        valueAnimator.start();
    }

    private ValueAnimator getValueAnimator(final TextView textView, final String unit, float fromScale,
                                           int fromValue, int toValue, float toScale, long duration) {
        PropertyValuesHolder valueHolder = PropertyValuesHolder.ofInt("value", fromValue, toValue);
        PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("scale", fromScale, toScale);
        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(valueHolder, scaleHolder);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue("value");
                String text = value + unit;
                textView.setText(text);
            }
        });
        return valueAnimator;
    }

    private ScaleAnimation getScaleAnimation(View diagram, float fromScale, float toScale, long duration) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, fromScale, toScale);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        diagram.startAnimation(scaleAnimation);
        return scaleAnimation;
    }
}
