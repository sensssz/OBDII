package cn.edu.nju.software.obdii.ui;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;

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
    private TextView mTimeView;

    private ViewPager mViewPager;
    private OBDAdapter mOBDAdapter;
    private OBDPart1Fragment mOBDPart1Fragment;
    private OBDPart2Fragment mOBDPart2Fragment;

    private float mSpeedAngle = 0;
    private float mSpeed = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOBDPart1Fragment = new OBDPart1Fragment();
        mOBDPart2Fragment = new OBDPart2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_obd, container, false);

        mPointer = (ImageView) view.findViewById(R.id.pointer);
        mSpeedView = (TextView) view.findViewById(R.id.speed);
        mViewPager = (ViewPager) view.findViewById(R.id.obd_info_pager);
        mTimeView = (TextView) view.findViewById(R.id.obd_time);

        mOBDAdapter = new OBDAdapter(getChildFragmentManager(), mOBDPart1Fragment, mOBDPart2Fragment);
        mOBDPart1Fragment.setViewPager(mViewPager);
        mOBDPart2Fragment.setViewPager(mViewPager);
        mViewPager.setAdapter(mOBDAdapter);

        updateSpeed();
        updateTime();

        DataDispatcher.getInstance().getOBDDataManager().getOBDData().setOnOBDUpdateListener(new OBDData.OnOBDUpdateListener() {
            @Override
            public void onSpeedUpdate(int speed) {
                updateSpeed(speed);
                updateTime();
            }

            @Override
            public void onVoltageUpdate(int voltage) {
                mOBDPart1Fragment.updateVoltage();
                updateTime();
            }

            @Override
            public void onCoolantTemperatureUpdate(int coolantTemperature) {
                mOBDPart1Fragment.updateCoolantTemperature();
                updateTime();
            }

            @Override
            public void onRotateSpeedUpdate(int rotateSpeed) {
                mOBDPart1Fragment.updateRotateSpeed();
                updateTime();
            }

            @Override
            public void onOilLeftUpdate(int oilLeft) {
                mOBDPart2Fragment.updateOilLeft();
                updateTime();
            }

            @Override
            public void onPressureUpdate(int pressure) {
                mOBDPart2Fragment.updatePressure();
                updateTime();
            }

            @Override
            public void onAirTemperatureUpdate(int airTemperature) {
                mOBDPart2Fragment.updateAirTemperature();
                updateTime();
            }
        });

        return view;
    }

    private void updateSpeed() {
        final int currentSpeed = DataDispatcher.getInstance().getOBDDataManager().getOBDData().getSpeed();
        updateSpeed(currentSpeed);
    }

    private void updateTime() {
        if (mTimeView != null) {
            String time = DataDispatcher.getInstance().getOBDDataManager().getOBDData().getTime();
            mTimeView.setText(time);
        }
    }

    private void updateSpeed(final int currentSpeed) {
        if (getActivity() != null) {
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

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
