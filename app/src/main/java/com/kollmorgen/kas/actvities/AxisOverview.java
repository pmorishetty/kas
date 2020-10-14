package com.kollmorgen.kas.actvities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.kollmorgen.kas.R;
import com.kollmorgen.kas.data.AxisData;
import com.kollmorgen.kas.service.KasDataService;
import com.kollmorgen.kas.service.RetrofitInstance;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AxisOverview extends AppCompatActivity {

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

   private int axisId = 1;

   public void refresh() {
      KasDataService kasDataService = RetrofitInstance.getKasDataService();
      Call<List<AxisData>> getAxes = kasDataService.getAxes();

      getAxes.enqueue(new Callback<List<AxisData>>() {
         @Override
         public void onResponse(Call<List<AxisData>> call, Response<List<AxisData>> response) {
            List<AxisData> axes = response.body();
            if (getAxisId() < axes.size()) {
               AxisData data = axes.get(getAxisId());

               EditText actualPosText = findViewById(R.id.editActualPosition);
               actualPosText.setText(data.getCurrentFBPosition().toString());

               EditText cmdPosText = findViewById(R.id.editCommandPosition);
               cmdPosText.setText(data.getCurrentPosition().toString());
            }
         }

         @Override
         public void onFailure(Call<List<AxisData>> call, Throwable t) {
            Log.d("KAS", "onFailure: " + t.getMessage());
         }
      });
   }
}
