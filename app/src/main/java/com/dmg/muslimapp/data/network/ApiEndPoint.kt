package com.dmg.muslimapp.data.network

import com.dmg.muslimapp.BuildConfig

object ApiEndPoint {
    //CEK VERSION
    const val ENDPOINT_VERSION = BuildConfig.BASE_URL_NEW + "/Version"

    //HOME SLIDER
    const val ENDPOINT_HOME_SLIDER = BuildConfig.BASE_URL_NEW + "Slider"

    //User Login
    const val ENDPOINT_LOGIN = BuildConfig.BASE_URL_NEW + "Login"

    //User Register
    val ENDPOINT_REGISTER = BuildConfig.BASE_URL_NEW + "Register"

    //User Profile
    val ENDPOINT_PROFILE = BuildConfig.BASE_URL_NEW + "Profile"

    val ENDPOINT_UPDATE_FCM = BuildConfig.BASE_URL_FCM + "Update_token"

    //AUTH
    val ENDPOINT_SERVER_LOGIN = BuildConfig.BASE_URL + "/login"

    val ENDPOINT_SERVER_REGISTER = BuildConfig.BASE_URL + "/register"
    val ENDPOINT_SERVER_FORGOT_PASS = BuildConfig.BASE_URL_NEW + "/Forgot"
    val ENDPOINT_SERVER_VERIFY_CODE = BuildConfig.BASE_URL_NEW + "/Forgot"
    val ENDPOINT_SERVER_UPDATE_PASSWORD = BuildConfig.BASE_URL + "/v2/forget_password/update_password"


    //USER
    val ENDPOINT_GET_PROFILE = BuildConfig.BASE_URL + "/v2/profile/{id_user}"

    val ENDPOINT_UPDATE_PROFILE = BuildConfig.BASE_URL + "/v2/profile/{id_user}"

    //GROUP
    val ENDPOINT_CREATE_GROUP = BuildConfig.BASE_URL_NEW + "/Group"

    val ENDPOINT_JOIN_GROUP = BuildConfig.BASE_URL_NEW + "/Group/Joingroup"

    val ENDPOINT_LIST_GROUP = BuildConfig.BASE_URL_NEW + "/Group/Listgroup"

    val ENDPOINT_LEAVE_GROUP = BuildConfig.BASE_URL_NEW + "/Group/Leavegroup"

    val ENDPOINT_DELETE_GROUP = BuildConfig.BASE_URL_NEW + "/Group/Deletegroup"

    val ENDPOINT_KICK_MEMBER = BuildConfig.BASE_URL_NEW + "/Group/Kickmember"

    //BANK
    val ENDPOINT_GET_BANK = BuildConfig.BASE_URL + "/bank"

    // QURBAN
    val ENDPOINT_PAKET_QURBAN = BuildConfig.BASE_URL + "/v2/qurban"
    val ENDPOINT_SUBMIT_QURBAN = BuildConfig.BASE_URL + "/v2/qurban"
    val ENDPOINT_UPDATE_BANK_QURBAN = BuildConfig.BASE_URL + "/v2/qurban/payment"

    //AQIQAH
    val ENDPOINT_ORDER_AQIQAH = BuildConfig.BASE_URL + "/v2/aqiqoh_order"

    val ENDPOINT_HISTORY_AQIQAH = BuildConfig.BASE_URL + "/v2/aqiqoh_order/get_list"

    val ENDPOINT_DETAIL_HISTORY_AQIQAH = BuildConfig.BASE_URL + "/aqiqoh_order/{id_aqiqoh}"

    val ENDPOINT_PAKET_AQIQAH = BuildConfig.BASE_URL + "/aqiqoh"

    val ENDPOINT_UPDATE_BANK = BuildConfig.BASE_URL + "/bank/aqiqoh"


    val ENDPOINT_UPDATE_DETAIL_AQIQAH = BuildConfig.BASE_URL + "/aqiqoh_order/update"

    val ENDPOINT_CONFIRM_PAYMENT_AQIQAH = BuildConfig.BASE_URL + "/aqiqoh_order/payment"

    val ENDPOINT_VOUCHER_AQIQAH = BuildConfig.BASE_URL + "/v2/aqiqoh_order/discount"

    //TRACKING
    val ENDPOINT_SEND_POSITION_USER = BuildConfig.BASE_URL_NEW + "/Tracking"

    //DOA
    val ENDPOINT_DOA = BuildConfig.BASE_URL_NEW + "/Doa"

    val ENDPOINT_DOA_DETAIL = BuildConfig.BASE_URL_NEW + "/Doa/Detail_doa/{id}"

    //MASJID
    val ENDPOINT_NEAR = BuildConfig.BASE_URL_NEW + "/Masjid"

    //HADITS
    val ENDPOINT_HADIST = BuildConfig.BASE_URL_NEW + "/Hadits/List_hadits"


    //GOOGLE MAP/PLACES API
    val BASE_URL_MAP_API = "https://maps.googleapis.com/maps/api/"
    val ENDPOINT_GOOGLE_GEOCODE = BASE_URL_MAP_API + "geocode/json"
    val ENDPOINT_GOOGLE_PLACE_DETAIL = BASE_URL_MAP_API + "place/details/json"
    val ENDPOINT_GOOGLE_PLACE_LIST = BASE_URL_MAP_API + "place/nearbysearch/json"


    val ENDPOINT_SEND_FEEDBACK = BuildConfig.BASE_URL_NEW + "/Feedback"

    val ENDPOINT_LIVE_MECCA = BuildConfig.BASE_URL_NEW + "/Live_video"

    //purchase
    val ENDPOINT_CHECK_PURCHASE = BuildConfig.BASE_URL_NEW + "Purchase/CekPurchase"
    val ENDPOINT_SUBMIT_PURCHASE = (BuildConfig.BASE_URL_NEW

            + "Purchase/SubmitPurchase")

    //wedding
    val ENDPOINT_WEDDING_LIST_PAKET = BuildConfig.BASE_URL_NEW + "Wedding/List_paket"
    val ENDPOINT_WEDDING_LIST_CITY = BuildConfig.BASE_URL_NEW + "Wedding/List_kota"
    val ENDPOINT_WEDDING_SET_MEETING = BuildConfig.BASE_URL_NEW + "Wedding/Order_wedding"
    val ENDPOINT_WEDDING_ORDER = BuildConfig.BASE_URL_NEW + "Wedding/Order_wedding"

    //aqiqah

    val ENDPOINT_AQIQAH_LIST_PAKET = BuildConfig.BASE_URL_NEW + "Aqiqah/List_paket"
    val ENDPOINT_AQIQAH_LIST_CITY = BuildConfig.BASE_URL_NEW + "Aqiqah/List_kota"
    val ENDPOINT_AQIQAH_ORDER = BuildConfig.BASE_URL_NEW + "Aqiqah/Order_aqiqah"

    //qurban

    val ENDPOINT_QURBAN_LIST_CITY = BuildConfig.BASE_URL_NEW + "Qurban/List_kota"
    //

    val ENDPOINT_LIST_BANK = BuildConfig.BASE_URL_NEW + "Bank/List_bank"

    //HELP
    val BASE_URL_DASHBOARD = BuildConfig.BASE_URL_DASHBOARD + "/Help/view/"


    val ENDPOINT_CHECK_VA_DOKU = "https://doku.muslimapp.id/DokuVA/Cek"
    val ENDPOINT_REQUEST_VA_DOKU = "https://doku.muslimapp.id/DokuVA/Payments"

    //NOTIFICATIONS
    val ENDPOINT_NOTIFICATIONS = BuildConfig.BASE_URL_FCM + "List_notif/ListMessage"

    val ENDPOINT_NOTIFICATIONS_DETAIL = BuildConfig.BASE_URL_FCM + "Notif/{notif_id}"

    //BURSA SAJADAH
    val ENDPOINT_HOME_SLIDER_BURSA_SAJADAH = BuildConfig.BASE_URL_BURSA_SAJADAH + "Slider/List"

    val ENDPOINT_BURSA_SAJADAH_KATEGORI = BuildConfig.BASE_URL_BURSA_SAJADAH + "Kategori/List/type/{type}"

    val ENDPOINT_BURSA_SAJADAH_PRODUK = BuildConfig.BASE_URL_BURSA_SAJADAH + "Produk/ListProduk/detail?kategori={id}"

    val ENDPOINT_BURSA_SAJADAH_SEARCH_PRODUK = BuildConfig.BASE_URL_BURSA_SAJADAH + "Produk/ListProduk/detail?name={keyword}"

    val ENDPOINT_BURSA_SAJADAH_CART = BuildConfig.BASE_URL_BURSA_SAJADAH + "Keranjang/List/get_by?"

    val ENDPOINT_BURSA_SAJADAH_CART_UPDATE = BuildConfig.BASE_URL_BURSA_SAJADAH + "Keranjang/Add"

    val ENDPOINT_BURSA_SAJADAH_CART_DELETE = BuildConfig.BASE_URL_BURSA_SAJADAH + "Keranjang/Delete"

    val ENDPOINT_BURSA_SAJADAH_HISTORY_ORDER = BuildConfig.BASE_URL_BURSA_SAJADAH + "Order/ListOrder/detail"

    val ENDPOINT_CHECK_CODE_COUPON_BURSA_SAJADAH = BuildConfig.BASE_URL_BURSA_SAJADAH + "Voucher/List/{code_coupon}"
    val ENDPOINT_PROVINCE_LIST = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/ListProvince"
    val ENDPOINT_CITY_LIST = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/ListCity"
    val ENDPOINT_SUB_DISTRICT_LIST = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/ListSubdistrict"
    val ENDPOINT_COURIER_LIST = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/ListKurir"
    val ENDPOINT_CHECK_ONGKIR = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/CekOngkir"
    val EDNPOINT_ORDER = BuildConfig.BASE_URL_BURSA_SAJADAH + "Order"

    val EDNPOINT_BURSA_SAJADAH_DELETE_ALL_CART = BuildConfig.BASE_URL_BURSA_SAJADAH + "Keranjang/Delete"

    val ENDPOINT_BURSA_SAJADAH_TRACKING_ORDER = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/CekResi"

    val ENDPOINT_BURSA_SAJADAH_SET_ADDR = BuildConfig.BASE_URL_BURSA_SAJADAH + "Shipping/SetAddr"

    val ENDPOINT_BURSA_SAJADAH_KONFIRMASI_DITERIMA = BuildConfig.BASE_URL_BURSA_SAJADAH + "Order/Confirm"
}