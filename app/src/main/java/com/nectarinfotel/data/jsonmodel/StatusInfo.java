
package com.nectarinfotel.data.jsonmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusInfo implements Parcelable
{

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<StatusInfo> CREATOR = new Creator<StatusInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public StatusInfo createFromParcel(Parcel in) {
            return new StatusInfo(in);
        }

        public StatusInfo[] newArray(int size) {
            return (new StatusInfo[size]);
        }

    }
    ;

    protected StatusInfo(Parcel in) {
        this.total = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StatusInfo() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(total);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}