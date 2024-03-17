package com.bbb.koha.common

import android.Manifest
import android.os.Build

/**
 *Created by Gaurav Kumar on 7/11/2022
 * QUYTECH
 */
object Constant {
    const val  LOCATION_SERVICE_NOTIF_ID = 1001
    const val BASE_URL ="http://dspace.bestbookbuddies.com:8081/api/v1/"
    const val GRANT_TYPE ="client_credentials"
    const val CLIENT_ID ="dc457ada-085d-4800-81a5-2411bed75009"
    const val CLIENT_SECRET  ="53c139ae-6d38-436f-98cd-ececf05cce5a"
    const val PREFS_NAME = "koha"
    const val MAC_ADDRESS = "mac_address"
    const val REQUEST_PERMISION =101
    const val PERMISSIONS_REQUEST_CODE = 102
    const val MOBILE ="mobile"
    const val VEHICLE_NO ="vehicle_no"
    const val IS_LOGIN ="is_login"
    const val ACCESS_TOKEN ="access_token"
    const val TOKEN_TYPE ="token_type"
    const val EXPIRES_IN ="expires_in"
    const val FIRST_NAME ="first_name"
    const val LAST_NAME ="last_name"
    const val ADDRESS ="address"
    const val DOB ="dob"
    const val GENDER ="gender"
    const val USER_PASSWORD ="password"
    const val PATRON_ID ="patron"
    const val LIBRARY_NAME ="library_name"
    const val USER_DETAIL ="user_detail"
    const val OTP ="otp"
    const val EMAIL ="gkgauravsah@gmail.com"
    const val PASSWORD ="vayqtbsubwhtzlpn"
    const val NEW_ARRIVALS ="new_arrival"
    const val TOP_CIRCULATING ="top_circulating"
    const val RECENTLY_BORROWED ="recently_borrowed"
    const val TOTAL_BOOK ="total_book"
    const val TOTAL_PATRON ="total_patron"

    const val LIBRARY ="library"
    const val CATEGORY ="category"
    const val SEARCH_BY ="search_by"
    const val SEARCH ="search"
    const val DATE_RANGE ="date_range"
    const val IS_AVAILABLE ="is_available"
    const val FRAGMENT_TYPE ="fragment_type"
    const val START_PAGE ="start_page"
    const val TOTAL_AMOUNT ="total_amount"
    const val VERIFICATION_TYPE ="verification_type"
    const val ID ="id"

    //mobilPay
    const val END_POINT = "getUserBillData"
    const val KEY = "1bb842b608261053c2993ae70ebb607cdb36bbf874159e8c8e6237eff5d758d5"
    const val MERCHANT_ID = "V10004"
    const val PUBLIC_KEY = "791E14FF-6243-4A73-A6E1"
    const val MERCHANT_AUTH_URL = "https://pay.mobilpay.in/index.php/getmerchantauthtoken"
    const val BILL_DETAIL = "https://mobylpe.com/index.php/getUserBillData"

    //MSG91
    const val MSG_URL = "https://control.msg91.com/api/v5/otp"
    const val AUTH_KEY = "344904AF5EqBN463202df4P1"
    const val TEMPLATE_ID = "63201e81d6fc05328e7839d2"
    const val SENDER_ID = "BESTBB"
    const val COUNTRY_CODE = "91"

    object FragmentType{
        const val BARCODE_SCAN = 1
        const val FILTER = 2
        const val ADVANCE_SEARCH = 3
        const val SEARCH = 4
    }

    object BookListType{
        const val NEW_ARRIVAL = 1
        const val TOP_CIRCULATING = 2
        const val BORROWED_BOOK = 3
    }

    object StartScreen{
        const val LIBRARY_HOME = 1
        const val USER_SUMMARY = 2
    }

    object VerificationType{
        const val EMAIL = "Email"
        const val MOBILE = "Mobile"
    }

    object UserType{
        const val ADMIN = 1
        const val RETAILER = 2
        const val DISTRIBUTOR = 3
        const val MECHANIC = 4
        const val VEHICLE_OWNER = 5
    }

    object OTPVerificationIntentType{
        const val FORGOT_ACTIVITY = "forgot_activity"
        const val REGISTRATION_ACTIVITY = "registration_activity"
    }

    val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

}