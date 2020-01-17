package com.example.android.sampleroomerror.utils

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException


object CustomConverters {

    private val listStringType =
        Types.newParameterizedType(List::class.java, String::class.java)
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val stringListadapter = moshi.adapter<List<String>>(listStringType)

    @TypeConverter
    @JvmStatic
    fun listToString(value: List<String>?): String {
        val result: List<String> = value ?: emptyList()
        return stringListadapter.toJson(result)
    }

    @TypeConverter
    @JvmStatic
    fun stringToList(value: String?): List<String> {
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                stringListadapter.fromJson(value) ?: emptyList()
            } catch (e: IOException) {
                e.printStackTrace()
                listOf("error_parsing_value")
            }
        }
    }
}
