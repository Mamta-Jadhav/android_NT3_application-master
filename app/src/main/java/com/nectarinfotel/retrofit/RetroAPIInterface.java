package com.nectarinfotel.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//WHAKM89QBJ
public interface RetroAPIInterface {

   // @FormUrlEncoded
    @GET( "login.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/login.php")
    Call<JsonObject> callLoginAPI(@Query("auth_user") String username,
                                  @Query("auth_pwd") String password,
                                  @Query("device_token") String device_token);

    @GET( "dashboard.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/dashboard.php")
    Call<JsonObject> callDashboardAPI(@Query("category") String category,
                                      @Query("subcategory") String subcategory,
                                      @Query("userid") String userid);
    @GET( "ticket_list.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/ticket_list.php")
    Call<JsonObject> callStatusDetailAPI(@Query("category") int category,
                                         @Query("subcategory") String subcategory,
                                         @Query("status") String status,
                                         @Query("userid") String userid,
                                         @Query("urgency") int urgency);

    @GET( "updateincident.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/updateincident.php")
    Call<JsonObject> callTicketModify(@Query("ticketid") String subcategory,
                                         @Query("operational_status") String status);

    @GET( "ticket_list.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/ticket_list.php")
    Call<JsonObject> callAgentDetailAPI(@Query("category") int category,
                                        @Query("subcategory") String subcategory,
                                        @Query("agent") String agent,
                                        @Query("userid") String userid,
                                        @Query("urgency") int urgency);

    @GET( "ticket_list.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/ticket_list.php")
    Call<JsonObject> callDepartmentDetailAPI(@Query("category") int category,
                                             @Query("subcategory") String subcategory,
                                             @Query("department") String department,
                                             @Query("userid") String userid,
                                             @Query("urgency") int urgency);

    @GET( "ticket_info.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/ticket_info.php")
    Call<JsonObject> callTicketDetailAPI(@Query("category") int category,
                                             @Query("ticketid") String subcategory,
                                             @Query("userid") String department);

    @GET( "province.php")
  //  @GET("https://nt3test.nectarinfotel.com/webservices/province.php")
    Call<JsonObject> callProvinceListAPI(@Query("category") int category);

    @GET( "organization.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/organization.php")
    Call<JsonObject> callAreaListAPI(@Query("category") int category);

    @GET( "catagory.php")
    //@GET("https://nt3test.nectarinfotel.com/webservices/catagory.php")
    Call<JsonObject> callCategoriesListAPI(@Query("category") int category);

    @GET( "event.php")
 //  @GET("https://nt3test.nectarinfotel.com/webservices/event.php")
    Call<JsonObject> callEventListAPI(@Query("category") int category);

    @GET( "reason.php")
    //@GET("https://nt3test.nectarinfotel.com/webservices/reason.php")
    Call<JsonObject> callReasonsListAPI(@Query("category") int category);

    @GET( "affectedlist.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/affectedlist.php")
    Call<JsonObject> callAffactedSitesListAPI(@Query("category") int category);

    @GET( "callerlist.php")
    //@GET("https://nt3test.nectarinfotel.com/webservices/callerlist.php?category=1")
    Call<JsonObject> callCallerListAPI(@Query("category") int category);

    @GET( "subreason.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/subreason.php?")
    Call<JsonObject> callSubReasonListAPI(@Query("category") int category,@Query("reason_id") String reason_id);

    @GET( "services.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/services.php?")
    Call<JsonObject> callServiceListAPI(@Query("category") int category,@Query("org_id") String reason_id);

    @GET( "ctratenewincident.php")
   // @GET("https://nt3test.nectarinfotel.com/webservices/ctratenewincident.php?")
    Call<JsonObject> callIncidentCretaeAPI(@Query("org_id") String org_id,
                                        @Query("caller_id") String caller_id,
                                        @Query("title") String title,
                                        @Query("description") String description,
                                        @Query("description_format") String description_format,
                                        @Query("province_id") String province_id,
                                        @Query("reason_id") String reason_id,
                                        @Query("sub_reason_id") String sub_reason_id,
                                        @Query("event_id") String event_id,
                                        @Query("category_id") String category_id,
                                        @Query("urgency") String urgency,
                                        @Query("service_aftd_id") String service_aftd_id,
                                        @Query("site_id") String site_id,
                                        @Query("network") String network,
                                        @Query("userid") String userid,
                                           @Query("service_id") String service_id

                                        );
}
//https://nt3.nectarinfotel.com/webservices