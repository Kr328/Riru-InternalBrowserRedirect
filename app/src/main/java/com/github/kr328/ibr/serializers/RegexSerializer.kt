package com.github.kr328.ibr.serializers

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

@Serializer(Regex::class)
class RegexSerializer : KSerializer<Regex> {
    override val descriptor: SerialDescriptor = StringDescriptor
    override fun deserialize(decoder: Decoder): Regex = Regex(decoder.decodeString())
    override fun serialize(encoder: Encoder, obj: Regex) = encoder.encodeString(obj.pattern)
}