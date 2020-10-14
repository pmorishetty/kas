package com.kollmorgen.kas.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
   private static final String HTTP = "http://";
   private static String ip_address = "10.50.67.67";
   private static Retrofit instance = null;

   public static KasDataService getKasDataService() {

      if (instance == null)
      {
         instance = new Retrofit
            .Builder()
            .baseUrl(HTTP + ip_address)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
      }
      return instance.create(KasDataService.class);
   }
}
