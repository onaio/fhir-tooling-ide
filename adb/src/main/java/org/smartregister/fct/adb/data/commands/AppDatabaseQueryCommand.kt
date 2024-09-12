package org.smartregister.fct.adb.data.commands

import org.json.JSONObject

class AppDatabaseQueryCommand(
    private val packageId: String,
    private val requestJson: String
) :
    ContentCommand() {

    override fun getPackageName(): String {
        return "${packageId}.fct"
    }

    override fun getMethodName(): String {
        return "db_operation"
    }

    override fun getArgument(): String {
        /*val jsonArg = JSONObject().apply {
            put("database", database)
            put("query", query)
        }*/

        return requestJson
    }
}