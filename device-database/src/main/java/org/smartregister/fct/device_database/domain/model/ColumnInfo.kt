package org.smartregister.fct.device_database.domain.model

internal data class ColumnInfo(
    val name: String,
    val type: String,
    val hasPrimaryKey: Boolean,
    val isNullable: Boolean
)
