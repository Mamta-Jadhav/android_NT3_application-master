
package com.nectarinfotel.data.jsonmodel;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailResponse implements Parcelable
{

    @SerializedName("flag")
    @Expose
    private Boolean flag;
    @SerializedName("ticketid")
    @Expose
    private String ticketid;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("info")
    @Expose
    private List<DetailInfo> info = null;
    @SerializedName("province")
    @Expose
    private List<String> province = null;
    @SerializedName("name")
    @Expose
    private List<String> name = null;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("event")
    @Expose
    private List<String> event = null;
    @SerializedName("reason")
    @Expose
    private List<String> reason = null;
    @SerializedName("site_name")
    @Expose
    private List<String> site_name = null;
    public final static Creator<DetailResponse> CREATOR = new Creator<DetailResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DetailResponse createFromParcel(Parcel in) {
            return new DetailResponse(in);
        }

        public DetailResponse[] newArray(int size) {
            return (new DetailResponse[size]);
        }

    }
    ;

    protected DetailResponse(Parcel in) {
        this.flag = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.ticketid = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.info, (DetailInfo.class.getClassLoader()));
        in.readList(this.province, (DetailInfo.class.getClassLoader()));
        in.readList(this.name, (DetailInfo.class.getClassLoader()));
        in.readList(this.category, (DetailInfo.class.getClassLoader()));
        in.readList(this.event, (DetailInfo.class.getClassLoader()));
        in.readList(this.reason, (DetailInfo.class.getClassLoader()));
        in.readList(this.site_name, (DetailInfo.class.getClassLoader()));

    }

    public DetailResponse() {
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DetailInfo> getInfo() {
        return info;
    }

    public void setInfo(List<DetailInfo> info) {
        this.info = info;
    }

    public List<String> getProvince() {
        return province;
    }

    public void setProvince(List<String> province) {
        this.province = province;
    }

    public static Creator<DetailResponse> getCREATOR() {
        return CREATOR;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getEvent() {
        return event;
    }

    public void setEvent(List<String> event) {
        this.event = event;
    }

    public List<String> getReason() {
        return reason;
    }

    public void setReason(List<String> reason) {
        this.reason = reason;
    }

    public List<String> getSite_name() {
        return site_name;
    }

    public void setSite_name(List<String> site_name) {
        this.site_name = site_name;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(flag);
        dest.writeValue(ticketid);
        dest.writeValue(msg);
        dest.writeList(info);
        dest.writeList(province);
        dest.writeList(name);
        dest.writeList(category);
        dest.writeList(event);
        dest.writeList(reason);
        dest.writeList(site_name);
    }

    public int describeContents() {
        return  0;
    }

}
