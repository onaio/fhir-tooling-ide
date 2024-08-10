package org.smartregister.fct.sm.data.repository

import kotlinx.coroutines.flow.Flow
import org.smartregister.fct.sm.domain.datasource.SMDataSource
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.domain.repository.SMRepository

class SMSqlDelightRepository(private val smDataSource: SMDataSource) : SMRepository {

    override fun getAll(): Flow<List<SMDetail>> = smDataSource.getAll()
    override suspend fun insert(smDetail: SMDetail) = smDataSource.insert(smDetail)
    override suspend fun update(smDetail: SMDetail) = smDataSource.update(smDetail)
    override suspend fun delete(id: String) = smDataSource.delete(id)
}