package com.example.itemsearch.models

import com.example.itemsearch.utils.StringWrapper
import com.example.itemsearch.utils.asStringWrapper


data class ServiceException(val code: String?, val string: StringWrapper) {
    constructor(code: String?, string: String) : this(code, string.asStringWrapper())
}
