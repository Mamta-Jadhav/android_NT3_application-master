
package com.nectarinfotel.data.jsonmodel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentResponse implements Parcelable
{

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("info")
    @Expose
    private List<DepartmentInfo> info = null;
    public final static Creator<DepartmentResponse> CREATOR = new Creator<DepartmentResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DepartmentResponse createFromParcel(Parcel in) {
            return new DepartmentResponse(in);
        }

        public DepartmentResponse[] newArray(int size) {
            return (new DepartmentResponse[size]);
        }

    }
    ;

    protected DepartmentResponse(Parcel in) {
        this.flag = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.info, (DepartmentInfo.class.getClassLoader()));
    }

    public DepartmentResponse() {
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

    public List<DepartmentInfo> getInfo() {
        return info;
    }

    public void setInfo(List<DepartmentInfo> info) {
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
