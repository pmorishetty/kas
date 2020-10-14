package com.kollmorgen.kas.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kollmorgen.kas.R;
import com.kollmorgen.kas.data.AxisData;
import com.kollmorgen.kas.service.KasDataService;
import com.kollmorgen.kas.service.RetrofitInstance;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AxisOverview extends AppCompatActivity {

    private  DisplayRequestThread m_display_thread;

    private static final int ESCAxisStatus_Initialised = 1;
    private static final int ESCAxisStatus_PowerON = 2;
    private static final int ESCAxisStatus_Enabled = 4;
    private static final int ESCAxisStatus_FoundOnNetwork = 8;
    private static final int ESCAxisStatus_Configured = 16;
    private static final int ESCAxisStatus_Running = 32;
    private static final int ESCAxisStatus_Error = 64;
    private static final int ESCAxisStatus_Simulated = 128;
    private static final int ESCAxisStatus_Connected = 256;
    private static final int ESCAxisStatus_Warning = 512;
    private static final int ESCAxisStatus_Stopping = 1024;
    private static final int ESCAxisStatus_Stopped = 2048;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axis_overview);

        m_display_thread = new DisplayRequestThread(this);

        Button refreshButton = findViewById(R.id.btnRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        refresh();
    }

    @Override
    protected void onStart() {
        super.onStart();

        m_display_thread.SetActive(true);
        m_display_thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        Intent intent = getIntent();
        String ip_address = intent.getStringExtra("ip_address");
        if (ip_address != null) {
            SetIpAddress(ip_address);
        }
*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        m_display_thread.SetActive(false);

        boolean thread_stopped = false;
        while (!thread_stopped) {
            try { m_display_thread.join(); } catch (InterruptedException e) {
                Log.d("KAS", "onStop: " + e.getMessage());
            }
            thread_stopped = true;
        }
    }

    public int getAxisId() {
        return axisId;
    }

    public void setAxisId(int axisId) {
        this.axisId = axisId;
    }

    public void setAxisData(List<AxisData> axisDataList)
    {
       if (getAxisId() < axisDataList.size()) {
          AxisData data = axisDataList.get(getAxisId());

          final double actualFBPosInUserUnits = (data.getCurrentFBPosition() / data.getFBUnitsPerTurn()) * 360.0;
          final double cmdPosInUserUnits = (data.getCurrentPosition() / data.getFBUnitsPerTurn()) * 360.0;

          EditText actualPosText = findViewById(R.id.editActualPosition);
          actualPosText.setText(String.format(Locale.US, "%.2f", actualFBPosInUserUnits));

          EditText cmdPosText = findViewById(R.id.editCommandPosition);
          cmdPosText.setText(String.format(Locale.US, "%.2f", cmdPosInUserUnits));

          EditText axisName = findViewById(R.id.editAxisName);
          axisName.setText(data.getSzName());

          final int status = data.getStatus();
          final boolean axisInErrorState = (status & ESCAxisStatus_Error) > 0;
          final boolean axisPoweredOn = (status & ESCAxisStatus_PowerON) > 0;

          CheckBox cbErr = findViewById(R.id.checkBoxInError);
          cbErr.setChecked(axisInErrorState);

          CheckBox cbPwr = findViewById(R.id.checkBoxPoweredOn);
          cbPwr.setChecked(axisPoweredOn);
          CheckBox cb = findViewById(R.id.checkBoxPoweredOff);
          cb.setChecked(!axisPoweredOn);
       }
    }

    private int axisId = 0;

    public void refresh() {
        KasDataService kasDataService = RetrofitInstance.getKasDataService();
        Call<List<AxisData>> getAxes = kasDataService.getAxes();

        getAxes.enqueue(new Callback<List<AxisData>>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull Call<List<AxisData>> call, @NonNull Response<List<AxisData>> response) {
                if (response.body() == null) {
                    Toast.makeText(AxisOverview.this, "Axis data not available", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<AxisData> axes = response.body();
                final int currentNumberOfAxis = axes.size();
                if (currentNumberOfAxis == 0) {
                    Toast.makeText(AxisOverview.this, "KAS application must be started to see the monitoring of the axes", Toast.LENGTH_SHORT).show();
                    return;
                }

                setAxisData(axes);
            }

            @Override
            public void onFailure(@NonNull Call<List<AxisData>> call, @NonNull Throwable t) {
                Log.d("KAS", "onFailure: " + t.getMessage());
                Toast.makeText(AxisOverview.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
