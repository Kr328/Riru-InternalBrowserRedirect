package com.github.kr328.ibr.serializers

import android.net.Uri
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializer(Uri::class)
class UriSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor = StringDescriptor
    override fun deserialize(decoder: Decoder): Uri = Uri.parse(decoder.decodeString())
    override fun serialize(encoder: Encoder, obj: Uri) = encoder.encodeString(obj.toString())
}