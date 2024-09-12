package org.smartregister.fct.device_database.domain.model

import org.json.JSONObject

data class QueryResponse(
    val count: Int = 0,
    val columns: List<JSONObject> = listOf(),
    val data: List<JSONObject> = listOf(),
    val error: String? = ""
) {

    companion object {
        fun build(result: Result<JSONObject>): QueryResponse {

            return if (result.isSuccess) {
                val jsonObject = result.getOrThrow()
                val count = jsonObject.getInt("count")
                val columns = jsonObject.getJSONArray("columnNames").map {
                    it as JSONObject
                }
                val data = jsonObject.getJSONArray("data").map {
                    it as JSONObject
                }

                QueryResponse(
                    count = count,
                    columns = columns,
                    data = data,
                    error = null
                )
            } else {
                QueryResponse(
                    error = result.exceptionOrNull()?.message ?: "Query Error"
                )
            }
        }
    }
}