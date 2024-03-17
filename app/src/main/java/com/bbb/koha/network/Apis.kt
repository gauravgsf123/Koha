package com.bbb.koha.network


import com.bbb.koha.common.Constant
import com.bbb.koha.dashboard.TripSheetResponse
import com.bbb.koha.login.RegisterResponseModel
import com.bbb.koha.login.model.LibraryDetailResponseModel
import com.bbb.koha.login.model.UserDetailResponseModel
import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.login.model.ValidateUserResponseModel
import com.bbb.koha.module.dashboard.model.*
import com.bbb.koha.module.my_account.change_password.ChangePasswordRequest
import com.bbb.koha.module.my_account.charges.ChargesResponseModel
import com.bbb.koha.module.my_account.personal_detail.PersonalDetailRequestModel
import com.bbb.koha.module.my_account.personal_detail.PersonalDetailResponseModel
import com.bbb.koha.module.my_account.purchase_suggestions.model.ItemResponseModel
import com.bbb.koha.module.my_account.purchase_suggestions.model.SuggestionListResponseModel
import com.bbb.koha.module.my_account.purchase_suggestions.model.SuggestionModel
import com.bbb.koha.module.my_account.summary.model.*
import com.bbb.koha.module.registration.model.AllCategoryResponseModel
import com.bbb.koha.module.registration.model.AllLibraryResponseModel
import com.bbb.koha.module.registration.model.RegisterUserRequestModel
import com.bbb.koha.module.registration.model.RegisterUserResponseModel
import com.bbb.koha.module.set_new_password.SetNewPasswordRequest
import com.bbb.koha.module.splash_screen.TokenResponse
import com.bbb.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbb.koha.module.my_account.charges.model.MerchantauthtokenResponse
import com.bbb.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbb.koha.module.my_account.charges.model.UserBillDataResponse
import com.bbb.koha.module.otp.OTPResponse
import com.bbb.koha.tracking.MobileTrackResponseModel
import retrofit2.Response
import retrofit2.http.*

interface Apis {

    @POST("mMobileRegister.htm")
    suspend fun register(@QueryMap body: Map<String, String>): Response<List<RegisterResponseModel>>

    @GET("api/Tripsheet/DocketCartonListByTripsheet")
    suspend fun pickupRequest(@QueryMap body: Map<String, String>): TripSheetResponse

    @POST("mMobileTrack.htm")
    suspend fun trackUser(@QueryMap loginMap: Map<String, String>): Response<List<MobileTrackResponseModel>>

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getToken(@Field("grant_type")  grantType: String = Constant.GRANT_TYPE,
                         @Field("client_id")  clientId: String = Constant.CLIENT_ID,
                         @Field("client_secret")  clientSecret: String = Constant.CLIENT_SECRET): Response<TokenResponse>

    @POST("auth/password/validation")
    suspend fun validateUser(@Body validateUserRequestModel: ValidateUserRequestModel): Response<ValidateUserResponseModel>

    @GET("patrons/{patronId}")
    suspend fun getUserDetail(@Path("patronId") patronId: Int?): Response<PersonalDetailResponseModel>

    @GET("libraries/{libraryId}")
    suspend fun getLibraryDetail(@Path("libraryId") libraryId: String?): Response<LibraryDetailResponseModel>

    @GET("patrons")
    suspend fun getUserDetailByEmail(@Header("x-koha-query") patronId: String?): Response<List<UserDetailResponseModel>>

    @GET("patrons")
    suspend fun getAllPatrons(): Response<List<UserDetailResponseModel>>

    @GET("libraries")
    suspend fun getLibraries(): Response<List<AllLibraryResponseModel>>

    @GET("authorised_value_categories/app_patcat/authorised_values")
    suspend fun getCategory(): Response<List<AllCategoryResponseModel>>

    @GET("authorised_value_categories/app_itemtype/authorised_values")
    suspend fun getItem(): Response<List<ItemResponseModel>>

    @POST("patrons")
    suspend fun registerUser(@Body validateUserRequestModel: RegisterUserRequestModel): Response<RegisterUserResponseModel>

    @POST("patrons/{patronId}/password")
    suspend fun resetPassword(@Path("patronId") patronId: Int?,
                              @Body changePasswordRequest: ChangePasswordRequest): Response<String>

    @POST("patrons/{patronId}/password")
    suspend fun setNewPassword(@Path("patronId") patronId: Int?,
                               @Body setNewPasswordRequest: SetNewPasswordRequest): Response<String>

    @PUT("patrons/{patronId}")
    suspend fun updatePersonalDetail(@Path("patronId") patronId: Int?,
                                     @Body personalDetailResponseModel: PersonalDetailRequestModel): Response<PersonalDetailResponseModel>

    @GET("checkouts")
    suspend fun getCheckout(@Query("patron_id") patronId: String?): Response<List<CheckoutResponseModel>>

    @GET("checkouts/{checkout_id}/allows_renewal")
    suspend fun checkRenewal(@Path("checkout_id") checkout_id: Int): Response<AllowRenewalResponseModel>

    @POST("checkouts/{checkout_id}/renewals")
    suspend fun renewal(@Path("checkout_id") checkoutId: Int): Response<RenewalResponseModel>

    @GET("items/{item_id}")
    suspend fun getItemDetail(@Path("item_id") itemId: Int): ItemDetailResponseModel

    @GET("biblios/{biblio_id}")
    suspend fun getBookDetail(@Path("biblio_id") biblioId: Int): BookDetailResponseModel

    @GET("volumes")
    suspend fun getBookImageDetail(@Query("q") isbnNumber: String?): BookDetailModel

    @GET("holds")
    suspend fun getHolds(@Query("patron_id") patronId: String?): Response<List<HoldsResponseModel>>

    @DELETE("holds/{holds_id}")
    suspend fun cancelHoldItem(@Path("holds_id") checkoutId: Int): Response<String>

    @GET("checkouts")
    suspend fun getCheckoutHistory(@Query("patron_id") patronId: String?,
                                   @Query("checked_in") checkedIn: Boolean?,
                                   @Query("_page") page: Int?,
                                   @Query("_per_page") perPage: Int?): Response<List<CheckoutResponseModel>>

    @GET("patrons/{patronId}/account/debits")
    suspend fun getCharges(@Path("patronId") patronId: Int): Response<List<ChargesResponseModel>>

    @GET("suggestions")
    suspend fun getSuggestions(@Query("q") suggestedBy:String?): Response<List<SuggestionListResponseModel>>

    @POST("suggestions")
    suspend fun addSuggestions(@Body suggestionModel: SuggestionModel): Response<SuggestionModel>

    @DELETE("suggestions/{suggestions_id}")
    suspend fun deleteSuggestions(@Path("suggestions_id") suggestionsId: Int): Response<String>

    @GET("biblios")
    suspend fun getBookList(@Query("_order_by") orderBy:String?,
                            @Query("_page") page:Int?,
                            @Query("_per_page") perPage:Int): Response<List<BookDetailResponseModel>>

    @GET("items")
    suspend fun getCirculatingBookList(@Query("_order_by") orderBy:String?,
                            @Query("_page") page:Int?,
                            @Query("_per_page") perPage:Int): Response<List<CirculatingBooksResponseModel>>

    @GET("biblios/{biblio_id}/items")
    suspend fun getItemListForBook(@Path("biblio_id") biblioId:Int?,
                                   @Query("_page") page:Int?,
                                   @Query("_per_page") perPage:Int): Response<List<ItemListOfBookResponseModel>>

    @POST("holds")
    suspend fun placeHold(@Body placeHoldRequestModel: PlaceHoldRequestModel): Response<PlaceHoldResponseModel>

    @GET("checkouts")
    suspend fun getBorrowedBook(@Query("patron_id") patronId: Int?,
                                @Query("_order_by") orderBy:String?,
                                @Query("checked_in") checkedIn: Boolean?,
                                @Query("_page") page: Int?,
                                @Query("_per_page") perPage: Int?): Response<List<CheckoutResponseModel>>

    @GET("biblios")
    suspend fun searchBookList(@Query("q") query: String?,
                               @Query("_page") page: Int?,
                               @Query("_per_page")perPage: Int?): Response<List<BookDetailResponseModel>>

    @GET("biblios/{biblio_id}/items")
    suspend fun getItemOfBiblio(@Path("biblio_id") biblioId:Int?,
                                @Query("q") query: String?): Response<List<ItemListOfBookResponseModel>>

    @GET("items")
    suspend fun searchBookItem(@Query("q") query: String?,@Query("_page") page: Int?,
                               @Query("_per_page")perPage: Int?): Response<List<CirculatingBooksResponseModel>>

    @GET("biblios/{biblio_id}/checkouts")
    suspend fun getCheckoutOfBiblio(@Path("biblio_id") biblioId:Int?): List<CheckoutResponseModel>

    @POST
    suspend fun getMerchantAuthToken(@Url url:String,@Body merchantauthtokenRequest: MerchantauthtokenRequest): Response<MerchantauthtokenResponse>

    @POST
    suspend fun getUserBillData(@Url url:String,@Body userBillDataRequest: UserBillDataRequest): Response<UserBillDataResponse>

    @POST
    suspend fun sendOTP(@Url url:String,@QueryMap body: Map<String, String>): Response<OTPResponse>

    /*@POST("signup")
    suspend fun signup(@Body signupRequestModel: SignupRequestModel): Response<SignUpResponseModel>

    @POST("verify-otp")n
    suspend fun verifyOTP(@Body otpVerificationRequestModel: OTPVerificationRequestModel): Response<OTPVerificationResponseModel>

    @POST("login")
    suspend fun login(@Body request: LoginRequestModel): Response<LoginResponseModel>

    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotRequestModel): Response<ForgotResponseModel>

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequestModel): Response<ResetPasswordResponseModel>

    @POST("resend-otp")
    fun resendOTP(@Body request: ResendOTPRequestModel): Response<ResendOTPResponseModel>

    @GET("")
    suspend fun getHomePageData(): Response<DashboardResponseModel>*/
}