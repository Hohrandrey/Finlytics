package ui.theme.icons

import androidx.compose.ui.graphics.vector.ImageVector
import ui.theme.icons.finlyticsiconpack.Add
import ui.theme.icons.finlyticsiconpack.Close
import ui.theme.icons.finlyticsiconpack.Date
import ui.theme.icons.finlyticsiconpack.Delete
import ui.theme.icons.finlyticsiconpack.Edit
import ui.theme.icons.finlyticsiconpack.Expenses
import ui.theme.icons.finlyticsiconpack.History
import ui.theme.icons.finlyticsiconpack.Income
import ui.theme.icons.finlyticsiconpack.Left
import ui.theme.icons.finlyticsiconpack.Minus
import ui.theme.icons.finlyticsiconpack.Plus
import ui.theme.icons.finlyticsiconpack.Right
import ui.theme.icons.finlyticsiconpack.Settings
import ui.theme.icons.finlyticsiconpack.Statistic
import ui.theme.icons.finlyticsiconpack.Wallet
import kotlin.collections.List as ____KtList

public object FinlyticsIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

public val FinlyticsIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Add, Close, Date, Delete, Edit, Expenses, History, Income, Left, Minus, Plus,
        Right, Settings, Statistic, Wallet)
    return __AllIcons!!
  }
