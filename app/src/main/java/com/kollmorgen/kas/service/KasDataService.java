package com.kollmorgen.kas.service;

import com.kollmorgen.kas.data.AxisData;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface KasDataService {

   @GET("kas/monitoring/axis")
   Call<List<AxisData>> getAxes();
}
