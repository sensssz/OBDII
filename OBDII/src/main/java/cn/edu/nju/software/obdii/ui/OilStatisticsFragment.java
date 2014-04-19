package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.graphics.Color;
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
 * Created by rogers on 2014/4/17.
 */
public class OilStatisticsFragment extends Fragment {

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    private GraphicalView mChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oil, container, false);

        if (savedInstanceState == null) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.oil_chart);


            // set some properties on the main renderer
            mRenderer.setApplyBackgroundColor(true);
            mRenderer.setBackgroundColor(getResources().getColor(R.color.char_background));
            mRenderer.setAxisTitleTextSize(20);
            mRenderer.setAxesColor(getResources().getColor(R.color.axis_color));

            mRenderer.setXTitle(getString(R.string.oil_x_label));
            mRenderer.setYTitle(getString(R.string.oil_y_label));
            mRenderer.setXAxisMax(10);
            mRenderer.setXAxisMin(0);
            mRenderer.setYAxisMax(25);
            mRenderer.setYAxisMin(0);
            mRenderer.setMarginsColor(getResources().getColor(R.color.background_color));
            mRenderer.setXLabelsColor(getResources().getColor(R.color.axis_color));
            mRenderer.setYLabelsColor(0, getResources().getColor(R.color.axis_color));


            mRenderer.setChartTitleTextSize(30);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setLabelsColor(getResources().getColor(R.color.axis_color));
            mRenderer.setLegendTextSize(20);
            mRenderer.setMargins(new int[] { 100, 60, 100, 60});
            mRenderer.setZoomButtonsVisible(false);
            mRenderer.setPointSize(5);

            mCurrentSeries = new XYSeries(getString(R.string.oil_consume_average));

            mDataset.addSeries(mCurrentSeries);

            mCurrentRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(mCurrentRenderer);

            mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
            mCurrentRenderer.setFillPoints(true);
            mCurrentRenderer.setDisplayChartValues(true);
            mCurrentRenderer.setColor(getResources().getColor(R.color.series_color));
            mCurrentRenderer.setLineWidth(5);
            mCurrentRenderer.setChartValuesTextSize(15);
            mCurrentRenderer.setDisplayChartValuesDistance(10);

            refreshData();

            mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));


        }
        return view;
    }

    private void refreshData() {
        mCurrentSeries.add(1, 20);
        mCurrentSeries.add(2, 13);
        mCurrentSeries.add(3, 14);
        mCurrentSeries.add(4, 21);
        mCurrentSeries.add(5, 10);
        mCurrentSeries.add(6, 11);
        mCurrentSeries.add(7, 16);
    }

}
