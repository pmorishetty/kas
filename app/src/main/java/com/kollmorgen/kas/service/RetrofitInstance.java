package com.kollmorgen.kas.service;

import android.util.Log;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
   private static final String HTTP = "http://";
   private static String ipAddress = "10.50.67.67";
   private static Retrofit instance = null;

   private static String getBaseUrl(){
      return HTTP + ipAddress; }

   private static Retrofit getInstance() {
      if (instance == null) {
         instance = new Retrofit
            .Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
      }
      return instance;
   }

   public static KasDataService getKasDataService() {

      return getInstance().create(KasDataService.class);
   }

   public static Boolean setIpAddress(String ip) {
      ipAddress = ip;
      boolean status = true;
      try {
         getInstance().newBuilder().baseUrl(getBaseUrl());
      }
      catch (Exception e) {
         Log.e("KAS", "setIpAddress: " + e.getMessage());
         status = false;
      }
      return status;
   }
}
