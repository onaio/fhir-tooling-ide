package org.smartregister.fct.sm.domain.datasource

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.sm.domain.model.SMDetail

interface SMDataSource {

    fun getAll(): Flow<List<SMDetail>>
    suspend fun insert(smDetail: SMDetail)
    suspend fun update(smDetail: SMDetail)
    suspend fun delete(id: String)
}