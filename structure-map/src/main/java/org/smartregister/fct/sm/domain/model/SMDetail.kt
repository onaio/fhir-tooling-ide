package org.smartregister.fct.sm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SMDetail(
    val id: String,
    val title: String,
    val body: String,
    var source: String? = null,
)