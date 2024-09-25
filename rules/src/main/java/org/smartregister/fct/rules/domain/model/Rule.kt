package org.smartregister.fct.rules.domain.model

import kotlinx.serialization.Serializable


@Serializable
internal data class Rule(
    val id: String,
    val name: String,
    val priority: Int,
    val condition: String,
    val description: String,
    val actions: List<String>,

    @kotlinx.serialization.Transient
    var result: String = ""
) : java.io.Serializable
