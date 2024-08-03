package org.smartregister.fct.adb.data.commands

import org.smartregister.fct.adb.domain.model.PackageInfo
import org.smartregister.fct.adb.domain.program.ADBCommand
import org.smartregister.fct.adb.utils.resultAsCommandException
import org.smartregister.fct.adb.utils.takeIfNotError
import java.util.Queue

internal class GetAllPackagesCommand(private val packageFilterList: List<String>) :
    ADBCommand<List<PackageInfo>> {

    override fun process(
        result: String,
        dependentResult: Queue<Result<*>>
    ): Result<List<PackageInfo>> {
        return result
            .takeIfNotError()
            ?.split("\n")
            ?.map { PackageInfo(packageId = it.replace("package:", "")) }
            ?.let { Result.success(it) }
            ?: result.resultAsCommandException()

    }

    override fun build(): List<String> {

        return listOf(
            "pm",
            "list",
            "packages",
        ) + packageFilterList
    }

    companion object {
        const val DISABLED_PACKAGES = "-d"
        const val ENABLED_PACKAGES = "-e"
        const val SYSTEM_PACKAGES = "-s"
        const val THIRD_PARTY_PACKAGES = "-3"
    }
}