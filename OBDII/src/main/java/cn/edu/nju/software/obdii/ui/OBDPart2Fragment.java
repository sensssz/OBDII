package cn.edu.nju.software.obdii.ui;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;

/**
 * Show the following OBD information:
 * OilLeft, coolant temperature, rotate speed of the engine
 */
public class OBDPart2Fragment extends Fragment {
    private static final long TOTAL_ANIMATION_TIME = 2000;
    private static final int MAX_OIL_LEFT = 20;
    private static final int MAX_PRESSURE = 120;
    private static final int MAX_AIR_TEMPERATURE = 167;

    private TextView mOilLeftText;
    private View mOilLeftDiagram;
    private TextView mPressureText;
    private View mPressureDiagram;
    private TextView mAirTemperatureText;
    private View mAirTemperatureDiagram;

    private ImageView mArrowLeft;
    private ViewPager mViewPager;

    private int mOilLeft;
    private float mOilLeftScale;
    private int mPressure;
    private float mPressureScale;
    private int mAirTemperature;
    private float mAirTemperatureScale;

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obd_info_2, container, false);

        mOilLeftText = (TextView) view.findViewById(R.id.oil_left_value);
        mOilLeftDiagram = view.findViewById(R.id.oil_left_diagram);
        mPressureText = (TextView) view.findViewById(R.id.pressure_value);
        mPressureDiagram = view.findViewById(R.id.pressure_diagram);
        mAirTemperatureText = (TextView) view.findViewById(R.id.air_temperature_value);
        mAirTemperatureDiagram = view.findViewById(R.id.air_temperature_diagram);

        mArrowLeft = (ImageView) view.findViewById(R.id.arrow_left);
        mArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0, true);
            }
        });

        updateOilLeft();
        updatePressure();
        updateAirTemperature();

        return view;
    }

    public void updateOilLeft() {
        final int currentOilLeft = DataDispatcher.getInstance().getOBDData().getOilLeft();
        mOilLeftDiagram.post(new Runnable() {
            @Override
            public void run() {
                updateOBDInfo(mOilLeftText, getString(R.string.oil_left_unit), mOilLeftDiagram,
                        mOilLeftScale, mOilLeft, currentOilLeft, MAX_OIL_LEFT,
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                mOilLeft = (Integer) valueAnimator.getAnimatedValue("value");
                                mOilLeftScale = (Float) valueAnimator.getAnimatedValue("scale");
                            }
                        }
                );
            }
        });
    }

    public void updatePressure() {
        final int currentPressure = DataDispatcher.getInstance().getOBDData().getPressure();
        mPressureDiagram.post(new Runnable() {
            @Override
            public void run() {
                updateOBDInfo(mPressureText, getString(R.string.pressure_unit), mPressureDiagram,
                        mPressureScale, mPressure, currentPressure,
                        MAX_PRESSURE, new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                mPressure = (Integer) valueAnimator.getAnimatedValue("value");
                                mPressureScale = (Float) valueAnimator.getAnimatedValue("scale");
                            }
                        }
                );
            }
        });
    }

    public void updateAirTemperature() {
        final int currentAirTemperature = DataDispatcher.getInstance().getOBDData().getAirTemperature();
        mAirTemperatureDiagram.post(new Runnable() {
            @Override
            public void run() {
                updateOBDInfo(mAirTemperatureText, getString(R.string.temperature_unit), mAirTemperatureDiagram,
                        mAirTemperatureScale, mAirTemperature, currentAirTemperature, MAX_AIR_TEMPERATURE,
                        new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                mAirTemperature = (Integer) valueAnimator.getAnimatedValue("value");
                                mAirTemperatureScale = (Float) valueAnimator.getAnimatedValue("scale");
                            }
                        }
                );
            }
        });
    }

    private void updateOBDInfo(final TextView textView, final String unit, View diagram,
                               float fromScale, int fromValue, int toValue, int maxValue,
                               ValueAnimator.AnimatorUpdateListener listener) {
        diagram.setBackgroundColor(Color.parseColor("#F9CDAD"));
        if (toValue > maxValue) {
            toValue = maxValue;
        }
        float toScale = toValue * 1.0f / maxValue;
        long duration = (long) (Math.abs(toScale - fromScale) * TOTAL_ANIMATION_TIME);

        ScaleAnimation scaleAnimation = getScaleAnimation(diagram, fromScale, toScale, duration);
        ValueAnimator valueAnimator = getValueAnimator(textView, unit, fromScale,
                fromValue, toValue, toScale, duration);
        valueAnimator.addUpdateListener(listener);

        diagram.startAnimation(scaleAnimation);
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
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, fromScale, toScale,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        diagram.startAnimation(scaleAnimation);
        return scaleAnimation;
    }
}
