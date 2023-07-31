package com.isolaatti.posting.posts.data.remote

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FeedFilterDto(
    val includeAudio: String,
    val includeFromSquads: String,
    val dateRange: DataRange
) {

    class LocalDateTimeJsonAdapter : TypeAdapter<LocalDate>() {
        override fun write(out: JsonWriter?, value: LocalDate?) {
            if(value != null) {
                out?.jsonValue("\"${value.format(DateTimeFormatter.ISO_LOCAL_DATE)}\"")
            }
        }

        override fun read(`in`: JsonReader?): LocalDate {
            return LocalDate.parse(`in`.toString())
        }

    }
    data class DataRange(
        val enabled: Boolean,
        @JsonAdapter(LocalDateTimeJsonAdapter::class)
        val from: LocalDate,
        @JsonAdapter(LocalDateTimeJsonAdapter::class)
        val to: LocalDate
    )
}
