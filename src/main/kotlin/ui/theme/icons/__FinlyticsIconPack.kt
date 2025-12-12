package ui.theme.icons

import androidx.compose.ui.graphics.vector.ImageVector
import ui.theme.icons.finlyticsiconpack.Add
import ui.theme.icons.finlyticsiconpack.History
import ui.theme.icons.finlyticsiconpack.Settings
import ui.theme.icons.finlyticsiconpack.Statistic
import kotlin.collections.List as ____KtList

public object FinlyticsIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val FinlyticsIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Add, History, Settings, Statistic)
    return __AllIcons!!
  }
