package com.example.favoriteteamsearch.models

import com.example.favoriteteamsearch.utils.StringWrapper
import com.example.favoriteteamsearch.utils.asStringWrapper


data class ServiceException(val code: String?, val string: StringWrapper) {
    constructor(code: String?, string: String) : this(code, string.asStringWrapper())
}
