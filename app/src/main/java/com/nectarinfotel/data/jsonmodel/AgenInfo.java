
package com.nectarinfotel.data.jsonmodel;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgenInfo implements Parcelable
{

    @SerializedName("agent")
    @Expose
    private String agent;
    @SerializedName("agent_tickets")
    @Expose
    private String tickets;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    public final static Creator<AgenInfo> CREATOR = new Creator<AgenInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AgenInfo createFromParcel(Parcel in) {
            return new AgenInfo(in);
        }

        public AgenInfo[] newArray(int size) {
            return (new AgenInfo[size]);
        }

    }
    ;

    protected AgenInfo(Parcel in) {
        this.agent = ((String) in.readValue((String.class.getClassLoader())));
        this.tickets = ((String) in.readValue((String.class.getClassLoader())));
        this.agentId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AgenInfo() {
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(agent);
        dest.writeValue(tickets);
        dest.writeValue(agentId);
    }

    public int describeContents() {
        return  0;
    }

}
