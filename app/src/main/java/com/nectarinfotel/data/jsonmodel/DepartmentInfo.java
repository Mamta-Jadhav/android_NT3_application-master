
package com.nectarinfotel.data.jsonmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentInfo implements Parcelable
{

    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("tickets")
    @Expose
    private String tickets;
    @SerializedName("org_id")
    @Expose
    private String orgId;
    public final static Creator<DepartmentInfo> CREATOR = new Creator<DepartmentInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DepartmentInfo createFromParcel(Parcel in) {
            return new DepartmentInfo(in);
        }

        public DepartmentInfo[] newArray(int size) {
            return (new DepartmentInfo[size]);
        }

    }
    ;

    protected DepartmentInfo(Parcel in) {
        this.department = ((String) in.readValue((String.class.getClassLoader())));
        this.tickets = ((String) in.readValue((String.class.getClassLoader())));
        this.orgId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DepartmentInfo() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(department);
        dest.writeValue(tickets);
        dest.writeValue(orgId);
    }

    public int describeContents() {
        return  0;
    }

}
