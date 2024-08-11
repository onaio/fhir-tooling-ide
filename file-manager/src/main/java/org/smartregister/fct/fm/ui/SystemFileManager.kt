package org.smartregister.fct.fm.ui

import androidx.compose.runtime.Composable
import ca.gosyer.appdirs.AppDirs
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

@Composable
fun SystemFileManager() {

    val home = System.getProperty("user.home")
    val downloads = "$home/Downloads"
    val music = "$home/Music"
    val pictures = "$home/Pictures"
    val homeFs = FileSystem.SYSTEM.list(home.toPath().root!!)
    val downloadFs = FileSystem.SYSTEM.list(downloads.toPath())
    val musicFs = FileSystem.SYSTEM.list(music.toPath())
    val pictureFs = FileSystem.SYSTEM.list(pictures.toPath())

    println(homeFs)
    println(downloadFs)
    println(musicFs)
    println(pictureFs)

}