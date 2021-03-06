package com.rns.farmerexpress.apihandler

import com.google.gson.JsonObject
import com.rns.farmerexpress.adapter.BuyItemAdapter
import com.rns.farmerexpress.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @POST("user/locate")
    fun getAllStates(): Call<List<StateData>>

    @FormUrlEncoded
    @POST("user/locate")
    fun getDisData(@Field("StateCode") stateCode: Int): Call<List<DisData>>


    @FormUrlEncoded
    @POST("user/locate")
    fun getSubDisData(@Field("DisttCode") distCode: Int): Call<List<SubDist>>


    @FormUrlEncoded
    @POST("user/locate")
    fun getVillageData(@Field("SubdistCode") subDistCode: Int): Call<List<Village>>


    @FormUrlEncoded
    @POST("user/login")
    fun userLogin(@Field("mobile") mobile: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/verify")
    fun otpVeriy(
        @Field("otp") otp: String,
        @Field("mobile") mobile: String): Call<OTPVeri>

 @FormUrlEncoded
    @POST("user/profile")
    fun addProfileData(
        @Field("session") session: String,
        @Field("name") name: String,
        @Field("location") locationID: String,
        @Field("village") village: String)
    : Call<SessionRes>

    @FormUrlEncoded
    @POST("user/profile")
    fun verifyNumber(@Field("session") session: String): Call<SessionRes>


    @FormUrlEncoded
    @POST("user/profile")
    fun addProfileFragData(
        @Field("session") session: String,
        @Field("name") name: String,
        @Field("photo") photo: String,
        @Field("email") email: String,
        @Field("sex") sex: String,
        @Field("dob") dob: String,
        @Field("profession") pro: String,
        @Field("about") about: String
    ): Call<SessionRes>


    @GET("post.php?getAll")
    fun getAllNews(@Query("limit") limit : Int,
                    @Query("page") page : Int): Call<List<NewsModel>>

    @GET("post.php?")
    fun getCatNews(@Query("category") cat : Int,
                    @Query("limit") limit : Int,
                    @Query("page") page : Int): Call<List<NewsModel>>

    @GET("post.php?")
    fun getNewsData(@Query("get") id : String): Call<List<NewsDataModel>>


    @GET("operator_codes.php?api_key=$PAY_KEY")
    fun getOperatorData(): Call<OperatorResModel>


    @GET("circle_codes.php?api_key=$PAY_KEY")
    fun getCircleData(): Call<CircleResModel>

    @FormUrlEncoded
    @POST("recharge_plans.php")
    fun getPlanData(
        @Field("api_key") api_key: String,
        @Field("state_code") state_code: Int,
        @Field("opid") op_id: Int
    ): Call<RechargePlan>


    @FormUrlEncoded
    @POST("post/create")
    fun createPost(
        @Field("session") session : String,
        @Field("type") type : String,
        @Field("discription") desc : String,
        @Field("image[]")  image :ArrayList<String>,
        @Field("poll[]")  poll :ArrayList<String>,
        @Field("tags[]")  tags :ArrayList<String>,
        @Field("contact")  contact : String,
        @Field("color")  color : String,
    ): Call<PostData>


    @FormUrlEncoded
    @POST("post/get")
    fun getPost(
        @Field("session") session : String,
        @Field("page") page : Int,
        @Field("limit") limit : Int,
    ): Call<GetPostData>

    @FormUrlEncoded
    @POST("post/lcs")
    fun likeShare(
        @Field("session") session : String,
        @Field("post_id") postId : Int,
        @Field("type") type : String,
    ): Call<LikeCommentShareModel>

 @FormUrlEncoded
    @POST("post/lcs")
    fun comment(
        @Field("session") session : String,
        @Field("post_id") postId : String?,
        @Field("type") type : String,
        @Field("detail") detail : String?,
    ): Call<Comment>

 @FormUrlEncoded
    @POST("post/poll")
    fun pollSelect(
        @Field("session") session : String,
        @Field("post_id") postId : String?,
        @Field("poll") pollDes : String,
    ): Call<PollSelectedModel>


    @GET("other/weather")
    fun weathers(
        @Query("language") language : String,
        @Query("latitude") latitude : String?,
        @Query("longitude") longitude : String,
    ): Call<WeatherModel>

    @FormUrlEncoded
    @POST("post/pick")
    fun getSinglePost(
        @Field("session") session : String,
        @Field("post_id") postId : String?,
    ): Call<GetSinglePost>


    @GET("other/getBhaw")
    fun mandiData(
        @Query("offset") offset : String,
        @Query("state") state : String,
    ): Call<MandiListModal>


    @FormUrlEncoded
    @POST("admin/category")
    fun getCatData(
        @Field("session") session : String,
        @Field("action") action : String?,
    ): Call<SellModel>

    @FormUrlEncoded
    @POST("admin/subCategory")
    fun getSubCatData(
        @Field("session") session : String,
        @Field("action") action : String?,
        @Field("id") id : String?,
    ): Call<SellModel>

  @FormUrlEncoded
    @POST("store/create")
    fun postSellData(
        @Field("session") session : String,
        @Field("type") action : String?,
        @Field("sub_category") sub_cat : String?,
        @Field("description") desc : String?,
        @Field("image") image : String,
        @Field("contact") contact : String?,
        @Field("latitude") latitude : String?,
        @Field("longitude") longitude : String?,
    ): Call<SellItemModel>

 @FormUrlEncoded
    @POST("post/delete")
    fun deletePost(
        @Field("session") session : String,
        @Field("post_id") post_id : String?,
    ): Call<PostDelete>


    @FormUrlEncoded
    @POST("store/get")
    fun getBuyData(
        @Field("session") session : String?,
        @Field("type") action : String?,
        @Field("page") page : String,
        @Field("limit") limit : String,
        @Field("latitude") latitude : String,
        @Field("longitude") longitude : String,
        @Field("minDistance") minDistance : String,
        @Field("category") category : String?,
        @Field("search") search : String,
    ): Call<SubBuyModel>

    @FormUrlEncoded
    @POST("store/like")
    fun likeSubBuy(
        @Field("session") session : String,
        @Field("store_id") storeid : String?,
    ):Call<LikeSubBuyModel>


    @FormUrlEncoded
    @POST("store/delete")
    fun deleteSubBuyPost(
        @Field("session") session : String,
        @Field("store_id") store_id : String?,
        @Field("type") type : String?,
    ): Call<PostDelete>

    companion object{
    const val PAY_KEY = "de41dd-569a6e-f86ef6-0ec821-4c8785"
}

}

