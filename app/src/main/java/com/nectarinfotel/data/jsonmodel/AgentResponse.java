
package com.nectarinfotel.data.jsonmodel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgentResponse implements Parcelable
{

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("info")
    @Expose
    private List<AgenInfo> info = null;
    public final static Creator<AgentResponse> CREATOR = new Creator<AgentResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AgentResponse createFromParcel(Parcel in) {
            return new AgentResponse(in);
        }

        public AgentResponse[] newArray(int size) {
            return (new AgentResponse[size]);
        }

    }
    ;

    protected AgentResponse(Parcel in) {
        this.flag = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.info, (AgenInfo.class.getClassLoader()));
    }

    public AgentResponse() {
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

    public List<AgenInfo> getInfo() {
        return info;
    }

    public void setInfo(List<AgenInfo> info) {
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
