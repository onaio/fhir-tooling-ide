package org.smartregister.fct.common.data.datatable.feature

interface DTPagination {

    fun totalRecords(): Int

    fun getOffset(): Int
    fun getLimit(): Int
    fun updateLimit(limit: Int)

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

    fun goNext(offset: Int)
}