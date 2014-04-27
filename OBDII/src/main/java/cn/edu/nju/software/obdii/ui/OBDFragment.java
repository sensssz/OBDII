package cn.edu.nju.software.obdii.ui;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.obd.OBDData;

/**
 * Display OBD information
 */
public class OBDFragment extends Fragment {
    private static final long TOTAL_ANIMATION_TIME = 4000;
    private static final float SPEED_MIN_VALUE = 0;
    private static final float SPEED_MAX_VALUE = 200;
    private static final float SPEED_MAX_ANGLE = 244f;
    private static final float DEGREE_PER_SPEED =
            SPEED_MAX_ANGLE / (SPEED_MAX_VALUE - SPEED_MIN_VALUE);
    private static final float TIME_PER_DEGREE =
            TOTAL_ANIMATION_TIME / SPEED_MAX_ANGLE;

    private ImageView mPointer;
    private TextView mSpeedView;

    private float mSpeedAngle = 0;
    private float mSpeed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obd, container, false);

        mPointer = (ImageView) view.findViewById(R.id.pointer);
        mSpeedView = (TextView) view.findViewById(R.id.speed);

        updateSpeed();

        DataDispatcher.getInstance().getOBDData().setOnOBDUpdateListener(new OBDData.OnOBDUpdateListener() {
            @Override
            public void onSpeedUpdate(int speed) {
                updateSpeed(speed);
            }

            @Override
            public void onVoltageUpdate(int voltage) {

            }

            @Override
            public void onCoolantTemperatureUpdate(int coolantTemperature) {

            }

            @Override
            public void onRotateSpeedUpdate(int rotateSpeed) {

            }

            @Override
            public void onOilLeftUpdate(int oilLeft) {

            }

            @Override
            public void onPressureUpdate(int pressure) {

            }

            @Override
            public void onAirTemperatureUpdate(int airTemperature) {

            }
        });

        return view;
    }

    private void updateSpeed() {
        final int currentSpeed = DataDispatcher.getInstance().getOBDData().getSpeed();
        updateSpeed(currentSpeed);
    }

    private void updateSpeed(final int currentSpeed) {
        mPointer.post(new Runnable() {
            @Override
            public void run() {
                float speed = currentSpeed;
                if (speed > SPEED_MAX_VALUE) {
                    speed = SPEED_MAX_VALUE;
                }
                float toDegree = speed * DEGREE_PER_SPEED;
                long duration = (long) (Math.abs(toDegree - mSpeedAngle) * TIME_PER_DEGREE);
                Animation pointerAnimation = getSpeedPointerRotateAnimation(toDegree, duration);
                ValueAnimator speedViewAnimator = getSpeedTextAnimator(speed, toDegree, duration);
                mPointer.startAnimation(pointerAnimation);
                speedViewAnimator.start();
            }
        });
    }

    private RotateAnimation getSpeedPointerRotateAnimation(float toDegree, long duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(mSpeedAngle, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    private ValueAnimator getSpeedTextAnimator(float toSpeed, float toDegree, long duration) {
        PropertyValuesHolder speedHolder = PropertyValuesHolder.ofInt("speed", (int) mSpeed, (int) toSpeed);
        PropertyValuesHolder angleHolder = PropertyValuesHolder.ofFloat("angle", mSpeedAngle, toDegree);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(speedHolder, angleHolder);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int speed = (Integer) valueAnimator.getAnimatedValue("speed");
                float angle = (Float) valueAnimator.getAnimatedValue("angle");
                String speedText = speed + getString(R.string.speed_unit);
                mSpeedView.setText(speedText);
                mSpeed = speed;
                mSpeedAngle = angle;
            }
        });
        return animator;
    }
}
