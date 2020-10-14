package com.kollmorgen.kas.actvities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.kollmorgen.kas.data.AxisData;
import com.kollmorgen.kas.service.KasDataService;
import com.kollmorgen.kas.service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayRequestThread extends Thread {

    final long Update_Rate_Ms = 250;
    private boolean m_active = false;

    private AxisOverview m_main_activity;

    DisplayRequestThread(AxisOverview m_main_activity) {
        this.m_main_activity = m_main_activity;
    }

    void SetActive(boolean active) {
        m_active = active;
    }

    void RefreshAxisData() {
        KasDataService kasDataService = RetrofitInstance.getKasDataService();
        Call<List<AxisData>> getAxes = kasDataService.getAxes();

        getAxes.enqueue(new Callback<List<AxisData>>() {

            @Override
            public void onResponse(@NonNull Call<List<AxisData>> call, @NonNull Response<List<AxisData>> response) {
                if (response.body() == null) {
                    return;
                }

                List<AxisData> axes = response.body();
                final int currentNumberOfAxis = axes.size();
                if (currentNumberOfAxis == 0) {
                    return;
                }

                m_main_activity.setAxisData(axes);
            }

            @Override
            public void onFailure(@NonNull Call<List<AxisData>> call, @NonNull Throwable t) {
                Log.d("KAS", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void run() {
        while (m_active) {
            long start_time_ms = System.currentTimeMillis();

            RefreshAxisData();

            long end_time_ms = System.currentTimeMillis();
            long time_elapsed_ms = end_time_ms - start_time_ms;
            long delay = Update_Rate_Ms - time_elapsed_ms;

            if (delay > 0) {
                //Log.d("Time", "sleep time: "+ delay + " ms");
                try {
                    sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

