package com.rrain.util.base.string

import java.util.*


infix fun String.dot(other: String) = "$this.$other"


fun String.toBase64() = this.toByteArray().let { Base64.getEncoder().encodeToString(it) }



