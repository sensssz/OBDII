package cn.edu.nju.software.obdii.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;

import java.util.ArrayList;

import cn.edu.nju.software.obdii.R;

/**
 * Created by rogers on 2014/4/17.
 */
public class OilStatisticsFragment extends Fragment {
    ArrayList<Double> mOilArray; //耗油量数据
    ChartFragment mOilChart;
    SimpleDataAdapter<Double, Double> mDataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oil, container, false);
        if (savedInstanceState == null) {
            mOilChart = (ChartFragment) getFragmentManager().findFragmentById(R.id.oil_chart);
            ShinobiChart shinobiChart = mOilChart.getShinobiChart();
            //set license key
            shinobiChart.setLicenseKey("FLd6X5Ib2fgqCOLMjAxNDA1MTdyb2dlcnMxNDBAZ21haWwuY29tXo4bH54Qev+tVhnCUvhcWYQNoETk/IApw01C69+NFglOQNhHti3bUi7SzciaQSNQELzmTqZJShEUGlAvQXh+Pjy1kipnfUQF6c45I1EiQPYHvfctG8dyHMwSZqa0N27AEcEMGnsWP56NL5pd7ix8BpTa85Ec=BQxSUisl3BaWf/7myRmmlIjRnMU2cA7q+/03ZX9wdj30RzapYANf51ee3Pi8m2rVW6aD7t6Hi4Qy5vv9xpaQYXF5T7XzsafhzS3hbBokp36BoJZg8IrceBj742nQajYyV7trx5GIw9jy/V6r0bvctKYwTim7Kzq+YPWGMtqtQoU=PFJTQUtleVZhbHVlPjxNb2R1bHVzPnh6YlRrc2dYWWJvQUh5VGR6dkNzQXUrUVAxQnM5b2VrZUxxZVdacnRFbUx3OHZlWStBK3pteXg4NGpJbFkzT2hGdlNYbHZDSjlKVGZQTTF4S2ZweWZBVXBGeXgxRnVBMThOcDNETUxXR1JJbTJ6WXA3a1YyMEdYZGU3RnJyTHZjdGhIbW1BZ21PTTdwMFBsNWlSKzNVMDg5M1N4b2hCZlJ5RHdEeE9vdDNlMD08L01vZHVsdXM+PEV4cG9uZW50PkFRQUI8L0V4cG9uZW50PjwvUlNBS2V5VmFsdWU+");

            shinobiChart.setTitle(getString(R.string.oil_consume_average));
            mDataAdapter = new SimpleDataAdapter<Double, Double>();
            //set axis
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setDefaultRange(new NumberRange(0.0, 30.0));
            shinobiChart.setXAxis(xAxis);
            shinobiChart.setYAxis(yAxis);

            refreshData();

            LineSeries series = new LineSeries();
            series.setDataAdapter(mDataAdapter);
            shinobiChart.addSeries(series);

            //set graph style
            LineSeriesStyle style1 = series.getStyle();
            style1.setFillStyle(SeriesStyle.FillStyle.GRADIENT);
            style1.setAreaColor(Color.argb(179, 94, 51, 95));
            style1.setAreaColorGradient(Color.argb(255,  94,  51,  95));
            style1.setAreaColorBelowBaseline(Color.argb(179,  94,  51,  95));
            style1.setAreaColorGradientBelowBaseline(Color.argb(255,  94,  51,  95));

        }
        return view;
    }

    private void refreshData() {
        //test
        for ( int i=0 ; i<100 ; i++) {
            double radians = i * Math.PI / 25.0;
            mDataAdapter.add(new DataPoint<Double, Double>(radians, Math.sin(radians)));
        }
        //
    }

}
