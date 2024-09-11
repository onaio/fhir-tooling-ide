package org.smartregister.fct.aurora

import androidx.compose.ui.graphics.vector.ImageVector
import org.smartregister.fct.aurora.auroraiconpack.Database
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.auroraiconpack.Table
import org.smartregister.fct.aurora.auroraiconpack.TableEye
import kotlin.collections.List as ____KtList

object AuroraIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

val AuroraIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Table, TableEye, Sync, Database)
    return __AllIcons!!
  }
