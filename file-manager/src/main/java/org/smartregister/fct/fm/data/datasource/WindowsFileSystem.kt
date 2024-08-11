package org.smartregister.fct.fm.data.datasource

import okio.Path.Companion.toPath
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.model.Directory
import java.io.File

internal class WindowsFileSystem : FileSystem {

    override fun rootDirs(): List<Directory> = File.listRoots().map {
        Directory(
            name = it.name,
            path = it.absolutePath.toPath()
        )
    }

}