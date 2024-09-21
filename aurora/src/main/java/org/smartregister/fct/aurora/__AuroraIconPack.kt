package org.smartregister.fct.aurora

import androidx.compose.ui.graphics.vector.ImageVector
import org.smartregister.fct.aurora.auroraiconpack.ConnectedTv
import org.smartregister.fct.aurora.auroraiconpack.Cyclone
import org.smartregister.fct.aurora.auroraiconpack.Database
import org.smartregister.fct.aurora.auroraiconpack.DesignServices
import org.smartregister.fct.aurora.auroraiconpack.EditNote
import org.smartregister.fct.aurora.auroraiconpack.Equal
import org.smartregister.fct.aurora.auroraiconpack.ExpandAll
import org.smartregister.fct.aurora.auroraiconpack.Folder
import org.smartregister.fct.aurora.auroraiconpack.JoinInner
import org.smartregister.fct.aurora.auroraiconpack.JoinLeft
import org.smartregister.fct.aurora.auroraiconpack.JoinRight
import org.smartregister.fct.aurora.auroraiconpack.KeyVertical
import org.smartregister.fct.aurora.auroraiconpack.KeyboardArrowDown
import org.smartregister.fct.aurora.auroraiconpack.KeyboardArrowUp
import org.smartregister.fct.aurora.auroraiconpack.ListAlt
import org.smartregister.fct.aurora.auroraiconpack.LocalFireDepartment
import org.smartregister.fct.aurora.auroraiconpack.MoveDown
import org.smartregister.fct.aurora.auroraiconpack.PhoneAndroid
import org.smartregister.fct.aurora.auroraiconpack.SearchInsights
import org.smartregister.fct.aurora.auroraiconpack.Settings
import org.smartregister.fct.aurora.auroraiconpack.Sync
import org.smartregister.fct.aurora.auroraiconpack.Table
import org.smartregister.fct.aurora.auroraiconpack.TableEye
import org.smartregister.fct.aurora.auroraiconpack.Token
import org.smartregister.fct.aurora.auroraiconpack.Widgets
import kotlin.collections.List as ____KtList

public object AuroraIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val AuroraIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons = listOf(
      ConnectedTv,
      Settings,
      Widgets,
      MoveDown,
      Token,
      Cyclone,
      Folder,
      SearchInsights,
      EditNote,
      PhoneAndroid,
      DesignServices,
      ListAlt,
      KeyVertical,
      Equal,
      JoinInner,
      JoinLeft,
      JoinRight,
      Table,
      TableEye,
      Sync,
      Database,
      KeyboardArrowDown,
      KeyboardArrowUp,
      ExpandAll,
      LocalFireDepartment
    )
    return __AllIcons!!
  }
