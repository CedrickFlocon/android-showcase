package com.cedrickflocon.android.showcase.user.data

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import com.apollographql.apollo3.api.json.writeAny
import java.net.URI

class UriAdapter : Adapter<URI> {
    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): URI {
        return URI(reader.nextString())
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: URI) {
        writer.writeAny(value.toString())
    }
}
