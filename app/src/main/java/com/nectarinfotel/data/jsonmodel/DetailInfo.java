
package com.nectarinfotel.data.jsonmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailInfo implements Parcelable
{
    @SerializedName("sub_reason_id")
    @Expose
    private String sub_reason_id;

     @SerializedName("parent_site")
    @Expose
    private String parent_site;

    @SerializedName("sub_reason")
    @Expose
    private String sub_reason;

    @SerializedName("site_id")
    @Expose
    private String site_id;
    @SerializedName("site_name")
    @Expose
    private String site_name;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("site_code")
    @Expose
    private String site_code;
    @SerializedName("reason_id")
    @Expose
    private String reason_id;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("orgnid")
    @Expose
    private String orgnid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ticketid")
    @Expose
    private String ticketid;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("caller")
    @Expose
    private String caller;
    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<DetailInfo> CREATOR = new Creator<DetailInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DetailInfo createFromParcel(Parcel in) {
            return new DetailInfo(in);
        }

        public DetailInfo[] newArray(int size) {
            return (new DetailInfo[size]);
        }

    }
    ;

    protected DetailInfo(Parcel in) {
        this.ticketid = ((String) in.readValue((String.class.getClassLoader())));
        this.department = ((String) in.readValue((String.class.getClassLoader())));
        this.ref = ((String) in.readValue((String.class.getClassLoader())));
        this.caller = ((String) in.readValue((String.class.getClassLoader())));
        this.agent = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.priority = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.orgnid = ((String) in.readValue((String.class.getClassLoader())));
        this.reason = ((String) in.readValue((String.class.getClassLoader())));
        this.reason_id = ((String) in.readValue((String.class.getClassLoader())));
        this.site_id = ((String) in.readValue((String.class.getClassLoader())));
        this.site_name = ((String) in.readValue((String.class.getClassLoader())));
        this.site_code = ((String) in.readValue((String.class.getClassLoader())));
        this.province = ((String) in.readValue((String.class.getClassLoader())));
        this.sub_reason = ((String) in.readValue((String.class.getClassLoader())));
        this.sub_reason_id = ((String) in.readValue((String.class.getClassLoader())));
        this.parent_site = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DetailInfo() {
    }

    public String getSub_reason_id() {
        return sub_reason_id;
    }

    public void setSub_reason_id(String sub_reason_id) {
        this.sub_reason_id = sub_reason_id;
    }

    public String getSub_reason() {
        return sub_reason;
    }

    public void setSub_reason(String sub_reason) {
        this.sub_reason = sub_reason;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

     public String getParent_site() {
        return parent_site;
    }

    public void setParent_site(String parent_site) {
        this.parent_site = parent_site;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrgnid() {
        return orgnid;
    }

    public void setOrgnid(String orgnid) {
        this.orgnid = orgnid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ticketid);
        dest.writeValue(department);
        dest.writeValue(ref);
        dest.writeValue(caller);
        dest.writeValue(agent);
        dest.writeValue(title);
        dest.writeValue(startDate);
        dest.writeValue(priority);
        dest.writeValue(status);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(orgnid);
        dest.writeValue(reason);
        dest.writeValue(reason_id);
        dest.writeValue(site_name);
        dest.writeValue(site_code);
        dest.writeValue(site_id);
        dest.writeValue(province);
        dest.writeValue(sub_reason);
        dest.writeValue(sub_reason_id);
        dest.writeValue(parent_site);
    }

    public int describeContents() {
        return  0;
    }

}
