package com.kollmorgen.kas.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AxisData {

   @SerializedName("CurrentFBPosition")
   @Expose
   private Double currentFBPosition;
   @SerializedName("CurrentPosition")
   @Expose
   private Double currentPosition;
   @SerializedName("FBUnitsPerTurn")
   @Expose
   private Integer fBUnitsPerTurn;
   @SerializedName("Status")
   @Expose
   private Integer status;
   @SerializedName("bPresent")
   @Expose
   private Boolean bPresent;
   @SerializedName("driveID")
   @Expose
   private Integer driveID;
   @SerializedName("szName")
   @Expose
   private String szName;

   public Double getCurrentFBPosition() {
      return currentFBPosition;
   }

   public Double getCurrentPosition() {
      return currentPosition;
   }

   public Integer getFBUnitsPerTurn() {
      return fBUnitsPerTurn;
   }

   public Integer getStatus() {
      return status;
   }

   public String getSzName() {
      return szName;
   }

}


