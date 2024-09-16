package org.smartregister.fct.aurora

import androidx.compose.ui.graphics.vector.ImageVector
import org.smartregister.fct.aurora.auroraiconpack.Database
import org.smartregister.fct.aurora.auroraiconpack.Equal
import org.smartregister.fct.aurora.auroraiconpack.ExpandAll
import org.smartregister.fct.aurora.auroraiconpack.JoinInner
import org.smartregister.fct.aurora.auroraiconpack.JoinLeft
import org.smartregister.fct.aurora.auroraiconpack.JoinRight
import org.smartregister.fct.aurora.auroraiconpack.KeyVertical
import org.smartregister.fct.aurora.auroraiconpack.KeyboardArrowDown
import org.smartregister.fct.aurora.auroraiconpack.KeyboardArrowUp
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.auroraiconpack.Table
import org.smartregister.fct.aurora.auroraiconpack.TableEye
import kotlin.collections.List as ____KtList

public object AuroraIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val AuroraIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(KeyVertical, Equal, JoinInner, JoinLeft, JoinRight, Table, TableEye, Sync, Database, KeyboardArrowDown, KeyboardArrowUp, ExpandAll)
    return __AllIcons!!
  }
