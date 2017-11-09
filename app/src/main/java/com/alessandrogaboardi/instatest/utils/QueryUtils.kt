package com.alessandrogaboardi.instatest.utils

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder


/**
 * Created by alessandrogaboardi on 02/11/2017.
 */
object QueryUtils{
    fun constructURL(base: String, params: Map<String, String>?): String {
        val url = StringBuilder(base)
        if (params != null && params.isNotEmpty()) {
            url.append("?")
            for ((key, value) in params) {
                url.append(key)
                url.append("=")
                try {
                    url.append(URLEncoder.encode(value, "UTF-8"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    //TODO: implement some proper exception handling
                }

                url.append("&")   //not for the last one (but should be OK)
            }
        }

        return url.toString()
    }

    @Throws(UnsupportedEncodingException::class)
    fun splitQuery(query: String): Map<String, String> {
        val query_pairs = LinkedHashMap<String, String>()
        val pairs = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"))
        }
        return query_pairs
    }
}