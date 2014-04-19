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
            //mRenderer.setBackgroundColor(Color.argb(251, 233, 220, 1));
            mRenderer.setAxisTitleTextSize(20);
            mRenderer.setAxesColor(getResources().getColor(R.color.axis_color));
            mRenderer.setXTitle("次数");
            mRenderer.setYTitle("耗油（升）");
            mRenderer.setXAxisMax(10);
            mRenderer.setXAxisMin(1);
            mRenderer.setYAxisMax(25);
            mRenderer.setYAxisMin(0);
            mRenderer.setMarginsColor(Color.argb(1, 255, 255, 255));

            mRenderer.setChartTitleTextSize(30);
            mRenderer.setLabelsTextSize(20);
            mRenderer.setLegendTextSize(20);
            mRenderer.setMargins(new int[] { 40, 40, 100, 0 });
            mRenderer.setZoomButtonsVisible(false);
            mRenderer.setPointSize(5);

            mCurrentSeries = new XYSeries(getString(R.string.oil_consume_average));
            mDataset.addSeries(mCurrentSeries);
            mCurrentRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(mCurrentRenderer);

            mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);
            mCurrentRenderer.setFillPoints(true);
            mCurrentRenderer.setDisplayChartValues(true);
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
