package com.kollmorgen.kas.actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AxisOverview extends AppCompatActivity {

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

      Button refreshButton = findViewById(R.id.btnRefresh);
      refreshButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            refresh();
         }
      });

      refresh();
   }

   public int getAxisId() {
      return axisId;
   }

   public void setAxisId(int axisId) {
      this.axisId = axisId;
   }

   private int axisId = 0;

   public void refresh() {
      KasDataService kasDataService = RetrofitInstance.getKasDataService();
      Call<List<AxisData>> getAxes = kasDataService.getAxes();

      getAxes.enqueue(new Callback<List<AxisData>>() {
         @SuppressLint("DefaultLocale")
         @Override
         public void onResponse(Call<List<AxisData>> call, Response<List<AxisData>> response) {
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

            if (getAxisId() < axes.size()) {
               AxisData data = axes.get(getAxisId());

               final double actualFBPosInUserUnits = (data.getCurrentFBPosition() / data.getFBUnitsPerTurn()) * 360.0;
               final double cmdPosInUserUnits = (data.getCurrentPosition() / data.getFBUnitsPerTurn()) * 360.0;

               EditText actualPosText = findViewById(R.id.editActualPosition);
               actualPosText.setText(String.format("%.2f", actualFBPosInUserUnits));

               EditText cmdPosText = findViewById(R.id.editCommandPosition);
               cmdPosText.setText(String.format("%.2f", cmdPosInUserUnits));

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

         @Override
         public void onFailure(Call<List<AxisData>> call, Throwable t) {
            Log.d("KAS", "onFailure: " + t.getMessage());
            Toast.makeText(AxisOverview.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
         }
      });
   }
}
