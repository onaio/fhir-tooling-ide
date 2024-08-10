package org.smartregister.fct.json.util

import java.util.UUID

val randomUUID: String
    get() = UUID.randomUUID().toString()