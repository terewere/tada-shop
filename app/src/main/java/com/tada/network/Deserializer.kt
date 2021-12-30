package com.tada.network

import com.google.gson.*
import java.lang.reflect.Type

class Deserializer<T> : JsonDeserializer<T> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): T = Gson().run {
        val jsonObject = json?.asJsonObject?.get("data")?.asJsonArray
        fromJson(jsonObject, typeOfT)
    }
}

