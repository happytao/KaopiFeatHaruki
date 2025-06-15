package com.haruki.kaopifeatharuki.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {
    private const val TAG = "GsonUtil"
    private val gson = Gson()

    /**
     * 将对象转换为 JSON 字符串
     * @param obj 要转换的对象
     * @return JSON 字符串
     */
    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * @param json JSON 字符串
     * @param clazz 目标对象的 Class 类型
     * @return 转换后的对象
     */
    fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            gson.fromJson(json, clazz)
        } catch (e: Exception) {
            Log.e(TAG,"fromJson: failed")
            Log.e(TAG,Log.getStackTraceString(e))
            null
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象（适用于泛型类型）
     * @param json JSON 字符串
     * @param type 目标对象的 Type 类型
     * @return 转换后的对象
     */
    fun <T> fromJson(json: String?, type: Type): T? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e(TAG,"fromJson: failed")
            Log.e(TAG,Log.getStackTraceString(e))
            null
        }
    }

    /**
     * 将 JSON 字符串转换为 List 集合
     * @param json JSON 字符串
     * @param clazz List 中元素的 Class 类型
     * @return 转换后的 List 集合
     */
    fun <T> fromJsonList(json: String?, clazz: Class<T>): List<T>? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            val type = TypeToken.getParameterized(List::class.java, clazz).type
            gson.fromJson<List<T>>(json, type)
        } catch (e: Exception) {
            Log.e(TAG,"fromJsonList: failed")
            Log.e(TAG,Log.getStackTraceString(e))
            null
        }
    }

    /**
     * 将 JSON 字符串转换为 Map 集合
     * @param json JSON 字符串
     * @param keyClazz Map 中 key 的 Class 类型
     * @param valueClazz Map 中 value 的 Class 类型
     * @return 转换后的 Map 集合
     */
    fun <K, V> fromJsonMap(
        json: String?,
        keyClazz: Class<K>,
        valueClazz: Class<V>
    ): Map<K, V>? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            val type = TypeToken.getParameterized(Map::class.java, keyClazz, valueClazz).type
            gson.fromJson<Map<K, V>>(json, type)
        } catch (e: Exception) {
            Log.e(TAG,"fromJsonMap: failed")
            Log.e(TAG,Log.getStackTraceString(e))
            null
        }
    }
}