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

   public void setCurrentFBPosition(Double currentFBPosition) {
      this.currentFBPosition = currentFBPosition;
   }

   public Double getCurrentPosition() {
      return currentPosition;
   }

   public void setCurrentPosition(Double currentPosition) {
      this.currentPosition = currentPosition;
   }

   public Integer getFBUnitsPerTurn() {
      return fBUnitsPerTurn;
   }

   public void setFBUnitsPerTurn(Integer fBUnitsPerTurn) {
      this.fBUnitsPerTurn = fBUnitsPerTurn;
   }

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      this.status = status;
   }

   public Boolean getBPresent() {
      return bPresent;
   }

   public void setBPresent(Boolean bPresent) {
      this.bPresent = bPresent;
   }

   public Integer getDriveID() {
      return driveID;
   }

   public void setDriveID(Integer driveID) {
      this.driveID = driveID;
   }

   public String getSzName() {
      return szName;
   }

   public void setSzName(String szName) {
      this.szName = szName;
   }

}


