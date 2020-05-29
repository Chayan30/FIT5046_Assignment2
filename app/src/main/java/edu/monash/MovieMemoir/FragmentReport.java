package edu.monash.MovieMemoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReport extends Fragment {

    public FragmentReport() {
        // Required empty public constructor
    }

    PieChart pieChart;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList pieData;
    ArrayList pieLabelData;
    String start_date;
    String end_date;
    String personId;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    long pieTotalCount = 0;
    ArrayList barEntries;
    String[] barLabels = {"","January","February","March","April","May","June","July","August", "September", "October", "November", "December"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        pieChart = root.findViewById(R.id.chart);
        startDatePicker = root.findViewById(R.id.datePickerStartDate);
        endDatePicker = root.findViewById(R.id.datePickerEndDate);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PersonId", Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("pid","");
        Button get_chart = root.findViewById(R.id.button_make_chart);
        Button get_bar = root.findViewById(R.id.button_bar);
        final Spinner bar_spinner = root.findViewById(R.id.spinnerBar);
        pieChart.setVisibility(View.INVISIBLE);
        barChart = root.findViewById(R.id.barChart);
        barChart.setVisibility(View.INVISIBLE);
        get_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChart.setVisibility(View.INVISIBLE);
                int start_day = startDatePicker.getDayOfMonth();
                int start_month = startDatePicker.getMonth() + 1;
                int start_year = startDatePicker.getYear();
                int end_day = endDatePicker.getDayOfMonth();
                int end_month = endDatePicker.getMonth() + 1;
                int end_year = endDatePicker.getYear();
                start_date = ""+start_year+"-"+start_month+"-"+start_day+" 00:00:00.000";
                end_date = ""+end_year+"-"+end_month+"-"+end_day+" 00:00:00.000";
                getEntries();

            }
        });
        get_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.INVISIBLE);
                barEntries = new ArrayList<>();
                String year = bar_spinner.getSelectedItem().toString();
                getBarData getasyncTask = new getBarData();
                getasyncTask.execute("findByMonthnameAndPersonIdAndYear/"+personId+"/"+year);
            }
        });

        return root;
    }
    private void getEntries() {
        pieEntries = new ArrayList<>();
        getPieChartDate getasyncTask = new getPieChartDate();
        getasyncTask.execute("findByPersonIdandWatchTimestamp/"+personId+"/"+start_date+"/"+end_date);

    }
    private class getPieChartDate extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            JSONArray response = HttpRequests.httpGetRequest("memoir", params[0]);
            if (response != null) {
                pieTotalCount = 0;
                pieData = new ArrayList();
                pieLabelData = new ArrayList();
                for (int i = 0; i < response.length(); i++) {
                    int postcode;
                    long mcount;
                    JSONObject temp = null;
                    try {
                        temp = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        postcode = temp.getInt("PostCode");
                        mcount = temp.getLong("MovieCount");
                        pieData.add(mcount);
                        pieLabelData.add(postcode);
                        pieTotalCount += mcount;
                        //pieEntries.add(new PieEntry(mcount,String.valueOf(postcode)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }
        @Override
        protected void onPostExecute (Void response){
            for(int i = 0; i < pieData.size();i++)
            {
                long data1 = (long)pieData.get(i);
                double data = (((double)data1) / pieTotalCount) * 100;
                Log.d("data",String.valueOf(data));
                pieEntries.add(new PieEntry((long)data,String.valueOf(pieLabelData.get(i))));
            }
            pieDataSet = new PieDataSet(pieEntries, " ");
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            pieDataSet.setSliceSpace(2f);
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(10f);
            pieDataSet.setSliceSpace(5f);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.setVisibility(View.VISIBLE);
        }

    }
    void addBarGraphData(String month, long mcount)
    {
        switch (month)
        {
            case "January":
                barEntries.add(new BarEntry(1, mcount, barLabels[0]));
                break;
            case "February":
                barEntries.add(new BarEntry(2, mcount, barLabels[1]));
                break;
            case "March":
                barEntries.add(new BarEntry(3, mcount, barLabels[2]));
                break;
            case "April":
                barEntries.add(new BarEntry(4, mcount, barLabels[3]));
                break;
            case "May":
                barEntries.add(new BarEntry(5, mcount, barLabels[4]));
                break;
            case "June":
                barEntries.add(new BarEntry(6, mcount, barLabels[5]));
                break;
            case "July":
                barEntries.add(new BarEntry(7, mcount, barLabels[6]));
                break;
            case "August":
                barEntries.add(new BarEntry(8, mcount, barLabels[7]));
                break;
            case "September":
                barEntries.add(new BarEntry(9, mcount, barLabels[8]));
                break;
            case "October":
                barEntries.add(new BarEntry(10, mcount, barLabels[9]));
                break;
            case "November":
                barEntries.add(new BarEntry(11, mcount, barLabels[10]));
                break;
            case "December":
                barEntries.add(new BarEntry(12, mcount, barLabels[11]));
                break;
        }
    }
    private class getBarData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            JSONArray response = HttpRequests.httpGetRequest("memoir", params[0]);
            if (response != null) {

                for (int i = 0; i < response.length(); i++) {
                    String monthName;
                    long mcount;
                    JSONObject temp = null;
                    try {
                        temp = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        monthName = temp.getString("MonthName");
                        mcount = temp.getLong("MovieCount");
                        addBarGraphData(monthName,mcount);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }
        @Override
        protected void onPostExecute (Void response){
            barDataSet = new BarDataSet(barEntries,"");
            barData = new BarData(barDataSet);
            barChart.setData(barData);
            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(18f);
            barChart.setVisibility(View.VISIBLE);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(barLabels));
        }
    }
}
