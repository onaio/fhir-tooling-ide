package org.smartregister.fct.adb.data.commands

import org.json.JSONObject
import org.smartregister.fct.logger.FCTLogger

class ExecuteRulesCommand(packageId: String, arg: String) : ContentCommand(packageId, arg) {

    override fun getMethodName(): String {
        return "execute_rules"
    }

    override fun process(jsonObject: JSONObject): Result<JSONObject> {
        return Result.success(jsonObject)
    }
}