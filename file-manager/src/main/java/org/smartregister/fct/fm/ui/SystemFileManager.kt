package org.smartregister.fct.fm.ui

import androidx.compose.runtime.Composable
import ca.gosyer.appdirs.AppDirs
import okio.FileSystem
import okio.Path.Companion.toPath

@Composable
fun SystemFileManager() {
    val appDirs = AppDirs(appName = null)
    println("User data dir: " + appDirs.getUserDataDir())
    println("User data dir (roaming): " + appDirs.getUserDataDir(roaming = true))
    println("User config dir: " + appDirs.getUserConfigDir())
    println("User config dir (roaming): " + appDirs.getUserConfigDir(roaming = true))
    println("User cache dir: " + appDirs.getUserCacheDir())
    println("User log dir: " + appDirs.getUserLogDir())
    println("Site data dir: " + appDirs.getSiteDataDir())
    println("Site data dir (multi path): " + appDirs.getSiteDataDir(multiPath = true))
    println("Site config dir: " + appDirs.getSiteConfigDir())
    println("Site config dir (multi path): " + appDirs.getSiteConfigDir(multiPath = true))
    println("Shared dir: " + appDirs.getSharedDir())

    val fs = FileSystem.SYSTEM.canonicalize(".".toPath())
    println(fs)
}