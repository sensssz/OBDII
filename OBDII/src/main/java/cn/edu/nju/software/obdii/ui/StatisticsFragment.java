package cn.edu.nju.software.obdii.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.List;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfo;
import cn.edu.nju.software.obdii.data.TravelInfo.TravelInfoManager;

/**
 * Show lines charts of average oil consumption, average speed and distance
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

    private TravelInfoManager mTravelInfoManager;
    private List<TravelInfo> mTravelInfoList;



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

        mTravelInfoManager = DataDispatcher.getInstance().getTravelInfoManager();
        mTravelInfoList = mTravelInfoManager.getTravelInfoList();

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
            mOilRenderer.setYAxisMax(3000);
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
            mSpeedRenderer.setYAxisMax(100);
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
            mMileageRenderer.setYAxisMax(1000);
            mMileageRenderer.setYAxisMin(0);
            //add line series
            mMileageCurrentSeries = new XYSeries(getString(R.string.mileage));
            mMileageDataset.addSeries(mMileageCurrentSeries);
            loadMileageData();
            mMileageChartView = ChartFactory.getLineChartView(getActivity(), mMileageDataset, mMileageRenderer);
            mileageLayout.addView(mMileageChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            mTravelInfoManager.addTravelInfoListener(new TravelInfoManager.OnTravelInfoListener() {
                @Override
                public void onTravelInfoReceived(TravelInfo travelInfo) {
                    addData(travelInfo);
                }
            });
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
        renderer.setPanEnabled(true, false);//block moving on both x and y side
        renderer.setZoomEnabled(false, false);//block zoom
        renderer.setXLabelsPadding(5);
        renderer.setYLabelsPadding(30);//set the padding between label and axis

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
        //read file to load history data
        int i = 1;
        for (TravelInfo t : mTravelInfoList) {
            double oilAverage = Double.parseDouble(t.getmAverageOilConsumption());
            mOilCurrentSeries.add(i, oilAverage);
            ++i;
        }


    }

    protected void loadSpeedData() {
        //should read file to load history data
        int i = 1;
        for (TravelInfo t : mTravelInfoList) {
            double speedAverage = Double.parseDouble(t.getmAverageSpeed());
            mSpeedCurrentSeries.add(i, speedAverage);
            ++i;
        }
    }

    protected void loadMileageData() {
        //should read file to load history data
        int i = 1;
        for (TravelInfo t : mTravelInfoList) {
            double mileage = Double.parseDouble(t.getmDistance());
            mMileageCurrentSeries.add(i, mileage);
            ++i;
        }
    }

    private void addData(TravelInfo t) {
        double oilAverage = Double.parseDouble(t.getmAverageOilConsumption());
        double speedAverage = Double.parseDouble(t.getmAverageSpeed());
        double mileage = Double.parseDouble(t.getmDistance());

        if (mTravelInfoList.size() < 30) {
            mTravelInfoList.add(t);
            int i = mTravelInfoList.size();

            mOilCurrentSeries.add(i, oilAverage);
            mSpeedCurrentSeries.add(i, speedAverage);
            mMileageCurrentSeries.add(i, mileage);

            mOilChartView.repaint();
            mSpeedChartView.repaint();
            mMileageChartView.repaint();
        }
        else {
            mTravelInfoList.remove(0);
            mTravelInfoList.add(t);
            int i = mTravelInfoList.size();

            mOilCurrentSeries.remove(1);
            mSpeedCurrentSeries.remove(1);
            mMileageCurrentSeries.remove(1);

            mOilCurrentSeries.add(i, oilAverage);
            mSpeedCurrentSeries.add(i, speedAverage);
            mMileageCurrentSeries.add(i, mileage);

            mOilChartView.repaint();
            mSpeedChartView.repaint();
            mMileageChartView.repaint();
        }

    }




}
