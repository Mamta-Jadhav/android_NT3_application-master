
package com.nectarinfotel.data.jsonmodel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusResponse implements Parcelable
{

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("info")
    @Expose
    private List<StatusInfo> info = null;
    public final static Creator<StatusResponse> CREATOR = new Creator<StatusResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public StatusResponse createFromParcel(Parcel in) {
            return new StatusResponse(in);
        }

        public StatusResponse[] newArray(int size) {
            return (new StatusResponse[size]);
        }

    }
    ;

    protected StatusResponse(Parcel in) {
        this.flag = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.info, (StatusInfo.class.getClassLoader()));
    }

    public StatusResponse() {
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<StatusInfo> getInfo() {
        return info;
    }

    public void setInfo(List<StatusInfo> info) {
        this.info = info;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(flag);
        dest.writeValue(msg);
        dest.writeList(info);
    }

    public int describeContents() {
        return  0;
    }

}
