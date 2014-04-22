package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import cn.edu.nju.software.obdii.R;

/**
 * Created by rogers on 4/22/14.
 */
public class StatisticsFragment extends Fragment {
    private XYMultipleSeriesDataset mOilDataSet;
    private XYMultipleSeriesRenderer mOilRenderer;
    private XYSeries mOilSeries;
    private XYSeriesRenderer mOilCurrentRenderer;
    private GraphicalView mOilChartView;

    private XYMultipleSeriesDataset mSpeedDataSet;
    private XYMultipleSeriesRenderer mSpeedRenderer;
    private XYSeries mSpeedSeries;
    private XYSeriesRenderer mSpeedCurrentRenderer;
    private GraphicalView mSpeedChartView;

    private XYMultipleSeriesDataset mMileageDataSet;
    private XYMultipleSeriesRenderer mMileageRenderer;
    private XYSeries mMileageSeries;
    private XYSeriesRenderer mMileageCurrentRenderer;
    private GraphicalView mMileageChartView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container,false);

        if(savedInstanceState != null) {
            //initialize oil chart
            mOilDataSet = new XYMultipleSeriesDataset();
            mOilRenderer = new XYMultipleSeriesRenderer();
            LinearLayout oilLayout = (LinearLayout) view.findViewById(R.id.oil_layout);
            mOilCurrentRenderer = new XYSeriesRenderer();
            mOilRenderer.addSeriesRenderer(mOilCurrentRenderer);
            setRendererStyle(mOilRenderer, mOilCurrentRenderer, getResources().getColor(R.color.oil_series_color));
            mOilRenderer.setXTitle(getString(R.string.oil_x_label));
            mOilRenderer.setYTitle(getString(R.string.oil_y_label));
            mOilRenderer.setXAxisMax(10);
            mOilRenderer.setXAxisMin(0);
            mOilRenderer.setYAxisMax(25);
            mOilRenderer.setYAxisMin(0);
            mOilSeries = new XYSeries(getString(R.string.oil_consume_average));
            mOilDataSet.addSeries(mOilSeries);
            loadOilData();
            mOilChartView = ChartFactory.getLineChartView(getActivity(), mOilDataSet, mOilRenderer);
            oilLayout.addView(mOilChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            //initialize speed chart
            mSpeedDataSet = new XYMultipleSeriesDataset();
            mSpeedRenderer = new XYMultipleSeriesRenderer();
            LinearLayout speedLayout = (LinearLayout) view.findViewById(R.id.speed_layout);
            mSpeedCurrentRenderer = new XYSeriesRenderer();
            mSpeedRenderer.addSeriesRenderer(mSpeedCurrentRenderer);
            setRendererStyle(mSpeedRenderer, mSpeedCurrentRenderer, getResources().getColor(R.color.speed_series_color));
            mSpeedRenderer.setXTitle(getString(R.string.speed_x_label));
            mSpeedRenderer.setYTitle(getString(R.string.speed_y_label));
            mSpeedRenderer.setXAxisMax(10);
            mSpeedRenderer.setXAxisMin(0);
            mSpeedRenderer.setYAxisMax(300);
            mSpeedRenderer.setYAxisMin(0);
            mSpeedSeries = new XYSeries(getString(R.string.speed_average));
            mSpeedDataSet.addSeries(mSpeedSeries);
            loadSpeedData();
            mSpeedChartView = ChartFactory.getLineChartView(getActivity(), mSpeedDataSet, mSpeedRenderer);
            speedLayout.addView(mSpeedChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            //initialize mileage chart
            mMileageDataSet = new XYMultipleSeriesDataset();
            mMileageRenderer = new XYMultipleSeriesRenderer();
            LinearLayout mileageLayout = (LinearLayout) view.findViewById(R.id.mileage_layout);
            mMileageCurrentRenderer = new XYSeriesRenderer();
            mMileageRenderer.addSeriesRenderer(mMileageCurrentRenderer);
            setRendererStyle(mMileageRenderer, mMileageCurrentRenderer, getResources().getColor(R.color.mileage_series_color));
            mMileageRenderer.setXTitle(getString(R.string.mileage_x_label));
            mMileageRenderer.setYTitle(getString(R.string.mileage_y_label));
            mMileageRenderer.setXAxisMax(10);
            mMileageRenderer.setXAxisMin(0);
            mMileageRenderer.setYAxisMax(200);
            mMileageRenderer.setYAxisMin(0);
            mMileageSeries = new XYSeries(getString(R.string.mileage));
            mMileageDataSet.addSeries(mMileageSeries);
            loadMileageData();
            mMileageChartView = ChartFactory.getLineChartView(getActivity(), mMileageDataSet, mMileageRenderer);
            mileageLayout.addView(mMileageChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        return view;
    }

    private void setRendererStyle(XYMultipleSeriesRenderer renderer, XYSeriesRenderer currentRenderer, int color ) {
        // set some properties on the main renderer
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(getResources().getColor(R.color.char_background));
        renderer.setAxisTitleTextSize(20);
        renderer.setAxesColor(getResources().getColor(R.color.axis_color));

        renderer.setMarginsColor(getResources().getColor(R.color.background_color));
        renderer.setXLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setYLabelsColor(0, getResources().getColor(R.color.axis_color));

        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(30);
        renderer.setLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setLegendTextSize(20);
        renderer.setMargins(new int[] { 10, 10, 10, 10});
        renderer.setZoomButtonsVisible(false);
        renderer.setPointSize(5);

        // set some properties on the current renderer
        currentRenderer.setPointStyle(PointStyle.CIRCLE);
        currentRenderer.setFillPoints(true);
        currentRenderer.setDisplayChartValues(true);
        currentRenderer.setColor(color);
        currentRenderer.setLineWidth(5);
        currentRenderer.setChartValuesTextSize(20);
        currentRenderer.setDisplayChartValuesDistance(10);
    }

    protected void loadOilData() {
        //should read file to load history data
        mOilSeries.add(1, 20);
        mOilSeries.add(2, 13);
        mOilSeries.add(3, 14);
        mOilSeries.add(4, 21);
        mOilSeries.add(5, 10);
        mOilSeries.add(6, 11);
        mOilSeries.add(7, 16);
    }
    protected void loadSpeedData() {
        //should read file to load history data
        mSpeedSeries.add(1, 78);
        mSpeedSeries.add(2, 80);
        mSpeedSeries.add(3, 120);
        mSpeedSeries.add(4, 60);
        mSpeedSeries.add(5, 90);
        mSpeedSeries.add(6, 170);
        mSpeedSeries.add(7, 100);
    }

    protected void loadMileageData() {
        //should read file to load history data
        mMileageSeries.add(1, 20);
        mMileageSeries.add(2, 40);
        mMileageSeries.add(3, 120);
        mMileageSeries.add(4, 70);
        mMileageSeries.add(5, 10);
        mMileageSeries.add(6, 180);
        mMileageSeries.add(7, 80);
    }


}
