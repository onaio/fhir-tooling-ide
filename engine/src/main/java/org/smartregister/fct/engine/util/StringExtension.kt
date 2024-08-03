package org.smartregister.fct.engine.util

import java.util.UUID

fun uuid(): String {
    return UUID.randomUUID().toString();
}