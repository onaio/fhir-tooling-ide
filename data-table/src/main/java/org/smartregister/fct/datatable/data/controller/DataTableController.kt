package org.smartregister.fct.datatable.data.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

abstract class DataTableController(
    initialQuery: String,
    val columns: List<JSONObject>,
    data: List<JSONObject>,
    count: Int,
    initialOffset: Int = 0,
    initialLimit: Int
) {

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _query = MutableStateFlow(initialQuery)
    val query: StateFlow<String> = _query

    private var _data = MutableStateFlow(data)
    val records: StateFlow<List<JSONObject>> = _data

    private var offset: Int = initialOffset
    private var limit: Int = initialLimit
    private var totalRecords: Int = count

    fun refreshData() {}

    fun totalRecords(): Int = totalRecords

    fun getOffset(): Int = offset

    fun getLimit(): Int = limit

    fun updateLimit(limit: Int) {
        this.limit = limit
    }

    fun canGoFirstPage() : Boolean {
        return getOffset() > 0
    }

    fun canGoPreviousPage() : Boolean {
        return getOffset() > 0
    }

    fun canGoNextPage(): Boolean {
        return (getOffset() + getLimit()) < totalRecords()
    }

    fun canGoLastPage(): Boolean {
        return (getOffset() + getLimit()) < totalRecords()
    }

    fun goNext() {
        runQuery(
            offset = getOffset() + getLimit(),
            limit = getLimit()
        )
    }

    suspend fun update(newOffset: Int, data: List<JSONObject>) {
        offset = newOffset
        _data.emit(data)
    }

    suspend fun showError(message: String) {
        _error.emit(message)
        delay(200)
        _error.emit(null)
    }

    abstract fun runQuery(offset: Int, limit: Int)
}