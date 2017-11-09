package com.alessandrogaboardi.instatest.ws

/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
object ApiConstants {
    val BASE_URL = "https://api.instagram.com/"
    val BASE_API_URL = BASE_URL + "v1/"

    val GET_USER_INFO = BASE_API_URL + "users/self/?access_token="
    val GET_SELF_MEDIA = BASE_API_URL + "users/self/media/recent/?access_token="
    val LIKE_PICTURE = BASE_API_URL + "media/%s/likes?access_token="
    val DISLIKE_PICTURE = BASE_API_URL + "media/%s/likes?access_token="
    val GET_MEDIA_COMMENTS = BASE_API_URL + "media/%s/comments?access_token="
    val SEND_MEDIA_COMMENT = BASE_API_URL + "media/%s/comment?access_token="
}