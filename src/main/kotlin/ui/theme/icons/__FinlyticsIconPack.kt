package ui.theme.icons

import androidx.compose.ui.graphics.vector.ImageVector
import ui.theme.icons.finlyticsiconpack.Add
import ui.theme.icons.finlyticsiconpack.Check
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

/**
 * Объект-контейнер для всех векторных иконок приложения "Finlytics".
 * Предоставляет доступ к коллекции всех иконок через свойство AllIcons.
 *
 * Доступные иконки:
 * - Add - иконка добавления (плюс)
 * - Close - иконка закрытия (крестик)
 * - Date - иконка календаря
 * - Delete - иконка удаления (корзина)
 * - Edit - иконка редактирования (карандаш)
 * - Expenses - иконка расходов (стрелка вниз)
 * - History - иконка истории (документ)
 * - Income - иконка доходов (стрелка вверх)
 * - Left - иконка навигации влево (стрелка)
 * - Minus - иконка минуса
 * - Plus - иконка плюса
 * - Right - иконка навигации вправо (стрелка)
 * - Settings - иконка настроек (шестеренка)
 * - Statistic - иконка статистики (график)
 * - Wallet - иконка кошелька
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
public object FinlyticsIconPack

private var __AllIcons: ____KtList<ImageVector>? = null

/**
 * Список всех доступных иконок в приложении.
 * Используется для превью и отладки.
 */
public val FinlyticsIconPack.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons= listOf(Add, Check, Close, Date, Delete, Edit, Expenses, History, Income, Left, Minus, Plus,
      Right, Settings, Statistic, Wallet)
    return __AllIcons!!
  }