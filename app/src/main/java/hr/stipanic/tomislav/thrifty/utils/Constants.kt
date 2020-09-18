package hr.stipanic.tomislav.thrifty.utils

object Constants {

    //firestore collections
    const val USERS_COLLECTION = "users"
    const val ARTICLES_COLLECTION = "articles"

    //shared prefs
    const val LOGIN = "LOGIN"
    const val IS_LOGGED_IN = "IS_LOGGED_IN"

    // keys for key-value pairs
    const val ARTICLE_ID_EXTRA = "ARTICLE_ID_EXTRA"
    const val USER_ID_EXTRA = "USER_ID_EXTRA"
    const val TITLE_EXTRA = "TITLE_ID_EXTRA"
    const val CATEGORY_EXTRA = "CATEGORY_EXTRA"
    const val DESC_EXTRA = "DESC_EXTRA"
    const val PRICE_EXTRA = "PRICE_EXTRA"
    const val COUNTRY_EXTRA = "COUNTRY_EXTRA"
    const val CITY_EXTRA = "CITY_EXTRA"
    const val LAT_EXTRA = "LAT_EXTRA"
    const val LNG_EXTRA = "LNG_EXTRA"

    // query categories
    const val CATEGORY_CARS = "Cars"
    const val CATEGORY_CLOTHES = "Clothes"
    const val CATEGORY_ELECTRONICS = "Electronics"
    const val CATEGORY_OTHER = "Other"

    // article fields
    const val CATEGORY = "category"
    const val DATE = "date"
    const val USER_ID ="userId"

    const val PICK_IMAGE_REQUEST = 71
    const val LOCATION_PERMISSION_REQUEST_CODE = 1

    // 2 different chat message layouts
    const val TYPE_OUT_MESSAGE = 1
    const val TYPE_INC_MESSAGE = 2

    // time measures for date
    const val MINUTE = 60
    const val TWO_MINUTES = 120
    const val HOUR = 3_600
    const val TWO_HOURS = 7_200
    const val DAY = 86_400
    const val TWO_DAYS = 172_800
    const val FORTY_DAYS = 3_456_000
}