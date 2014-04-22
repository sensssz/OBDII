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
    private XYMultipleSeriesDataset mOilDataset;
    private XYMultipleSeriesRenderer mOilRenderer;
    private XYSeries mOilCurrentSeries;
    private XYSeriesRenderer mOilCurrentRenderer;
    private GraphicalView mOilChartView;

    private XYMultipleSeriesDataset mSpeedDataset;
    private XYMultipleSeriesRenderer mSpeedRenderer;
    private XYSeries mSpeedCurrentSeries;
    private XYSeriesRenderer mSpeedCurrentRenderer;
    private GraphicalView mSpeedChartView;

    private XYMultipleSeriesDataset mMileageDataset;
    private XYMultipleSeriesRenderer mMileageRenderer;
    private XYSeries mMileageCurrentSeries;
    private XYSeriesRenderer mMileageCurrentRenderer;
    private GraphicalView mMileageChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        mOilDataset = new XYMultipleSeriesDataset();
        mOilRenderer = new XYMultipleSeriesRenderer();

        mSpeedDataset = new XYMultipleSeriesDataset();
        mSpeedRenderer = new XYMultipleSeriesRenderer();

        mMileageDataset = new XYMultipleSeriesDataset();
        mMileageRenderer = new XYMultipleSeriesRenderer();

        if (savedInstanceState == null) {
            //oil chart
            LinearLayout oilLayout = (LinearLayout) view.findViewById(R.id.oil_layout);
            mOilCurrentRenderer = new XYSeriesRenderer();
            mOilRenderer.addSeriesRenderer(mOilCurrentRenderer);
            setRendererStyle(mOilRenderer, mOilCurrentRenderer, getResources().getColor(R.color.oil_series_color));
            //set title for axis
            mOilRenderer.setXTitle(getString(R.string.oil_x_label));
            mOilRenderer.setYTitle(getString(R.string.oil_y_label));
            //set limit for axis
            mOilRenderer.setXAxisMax(10);
            mOilRenderer.setXAxisMin(0);
            mOilRenderer.setYAxisMax(25);
            mOilRenderer.setYAxisMin(0);
            //add line series
            mOilCurrentSeries = new XYSeries(getString(R.string.oil_consume_average));
            mOilDataset.addSeries(mOilCurrentSeries);
            loadOilData();
            mOilChartView = ChartFactory.getLineChartView(getActivity(), mOilDataset, mOilRenderer);
            oilLayout.addView(mOilChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            //speed chart
            LinearLayout speedLayout = (LinearLayout) view.findViewById(R.id.speed_layout);
            mSpeedCurrentRenderer = new XYSeriesRenderer();
            mSpeedRenderer.addSeriesRenderer(mSpeedCurrentRenderer);
            setRendererStyle(mSpeedRenderer, mSpeedCurrentRenderer, getResources().getColor(R.color.speed_series_color));
            //set title for axis
            mSpeedRenderer.setXTitle(getString(R.string.speed_x_label));
            mSpeedRenderer.setYTitle(getString(R.string.speed_y_label));
            //set limit for axis
            mSpeedRenderer.setXAxisMax(10);
            mSpeedRenderer.setXAxisMin(0);
            mSpeedRenderer.setYAxisMax(300);
            mSpeedRenderer.setYAxisMin(0);
            //add line series
            mSpeedCurrentSeries = new XYSeries(getString(R.string.speed_average));
            mSpeedDataset.addSeries(mSpeedCurrentSeries);
            loadSpeedData();
            mSpeedChartView = ChartFactory.getLineChartView(getActivity(), mSpeedDataset, mSpeedRenderer);
            speedLayout.addView(mSpeedChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            //mileage chart
            LinearLayout mileageLayout = (LinearLayout) view.findViewById(R.id.mileage_layout);
            mMileageCurrentRenderer = new XYSeriesRenderer();
            mMileageRenderer.addSeriesRenderer(mMileageCurrentRenderer);
            setRendererStyle(mMileageRenderer, mMileageCurrentRenderer, getResources().getColor(R.color.mileage_series_color));
            //set title for axis
            mMileageRenderer.setXTitle(getString(R.string.mileage_x_label));
            mMileageRenderer.setYTitle(getString(R.string.mileage_y_label));
            //set limit for axis
            mMileageRenderer.setXAxisMax(10);
            mMileageRenderer.setXAxisMin(0);
            mMileageRenderer.setYAxisMax(200);
            mMileageRenderer.setYAxisMin(0);
            //add line series
            mMileageCurrentSeries = new XYSeries(getString(R.string.mileage));
            mMileageDataset.addSeries(mMileageCurrentSeries);
            loadMileageData();
            mMileageChartView = ChartFactory.getLineChartView(getActivity(), mMileageDataset, mMileageRenderer);
            mileageLayout.addView(mMileageChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

        }
        return view;
    }

    private void setRendererStyle(XYMultipleSeriesRenderer renderer, XYSeriesRenderer currentRenderer, int color) {
        // set some properties on the main renderer
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(getResources().getColor(R.color.card_background));
        renderer.setAxisTitleTextSize(20);
        renderer.setAxesColor(getResources().getColor(R.color.axis_color));

        renderer.setMarginsColor(getResources().getColor(R.color.card_background));
        renderer.setXLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setYLabelsColor(0, getResources().getColor(R.color.axis_color));
        renderer.setInScroll(true);//in order to use in the scroll view

        renderer.setChartTitleTextSize(30);
        renderer.setLabelsTextSize(25);
        renderer.setLabelsColor(getResources().getColor(R.color.axis_color));
        renderer.setLegendTextSize(20);
        renderer.setMargins(new int[] { 30, 100, 30, 50});
        renderer.setZoomButtonsVisible(false);
        renderer.setPointSize(5);
        renderer.setPanEnabled(false,false);//block moving on both x and y side
        renderer.setXLabelsPadding(20);
        renderer.setYLabelsPadding(20);//set the padding between label and axis

        // set some properties on the current renderer
        currentRenderer.setPointStyle(PointStyle.CIRCLE);
        currentRenderer.setFillPoints(true);
        currentRenderer.setDisplayChartValues(true);
        currentRenderer.setColor(color);
        currentRenderer.setLineWidth(5);
        currentRenderer.setChartValuesTextSize(20);
        currentRenderer.setDisplayChartValuesDistance(10);
        currentRenderer.setShowLegendItem(false);
    }


    protected void loadOilData() {
        //should read file to load history data
        mOilCurrentSeries.add(1, 20);
        mOilCurrentSeries.add(2, 13);
        mOilCurrentSeries.add(3, 12);
        mOilCurrentSeries.add(4, 15);
        mOilCurrentSeries.add(5, 10);
        mOilCurrentSeries.add(6, 18);
        mOilCurrentSeries.add(7, 9);
    }

    protected void loadSpeedData() {
        mSpeedCurrentSeries.add(1, 78);
        mSpeedCurrentSeries.add(2, 80);
        mSpeedCurrentSeries.add(3, 120);
        mSpeedCurrentSeries.add(4, 60);
        mSpeedCurrentSeries.add(5, 90);
        mSpeedCurrentSeries.add(6, 170);
        mSpeedCurrentSeries.add(7, 100);
    }

    protected void loadMileageData() {
        mMileageCurrentSeries.add(1, 20);
        mMileageCurrentSeries.add(2, 40);
        mMileageCurrentSeries.add(3, 120);
        mMileageCurrentSeries.add(4, 70);
        mMileageCurrentSeries.add(5, 10);
        mMileageCurrentSeries.add(6, 180);
        mMileageCurrentSeries.add(7, 80);
    }




}
