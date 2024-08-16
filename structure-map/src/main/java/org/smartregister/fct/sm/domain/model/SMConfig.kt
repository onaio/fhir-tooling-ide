package org.smartregister.fct.sm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SMConfig(val smDetail: SMDetail, val tabIndex: Int)