package ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Объект, содержащий цветовую палитру приложения Finlytics.
 * Определяет цвета для темной темы приложения.
 */
object AppColors {
    // Основной темный цвет фона приложения
    val DarkColor = Color(0xFF1E1E1E)

    // Темно-серый цвет для вторичных элементов
    val DarkGreyColor = Color(0xFF2C2C2C)

    // Светло-серый цвет для неактивных элементов
    val LightGreyColor = Color(0xFF444444)

    // Основной светлый цвет текста
    val LightColor = Color(0xFFF4F4F4)

    // Основной синий цвет для акцентных элементов и кнопок
    val BlueColor = Color(0xFF2176FF)

    // Красный цвет для ошибок и негативных действий
    val RedColor = Color(0xFFEF4444)

    // Зеленый цвет для доходов и позитивных действий
    val GreenColor = Color(0xFF4ADE80)

    // Желтый цвет для предупреждений и баланса
    val YellowColor = Color(0xFFECB324)

    /**
     * Палитра цветов для категорий доходов в диаграммах.
     * 8 оттенков сине-зеленой гаммы.
     */
    val IncomeColor1 = Color(0xFF1565C0)
    val IncomeColor2 = Color(0xFF42A5F5)
    val IncomeColor3 = Color(0xFF29B6F6)
    val IncomeColor4 = Color(0xFF26A69A)
    val IncomeColor5 = Color(0xFF66BB6A)
    val IncomeColor6 = Color(0xFF9CCC65)
    val IncomeColor7 = Color(0xFFD4E157)
    val IncomeColor8 = Color(0xFFFFCA28)

    /**
     * Палитра цветов для категорий расходов в диаграммах.
     * 8 оттенков красно-фиолетовой гаммы.
     */
    val ExpensesColor1 = Color(0xFFD32F2F)
    val ExpensesColor2 = Color(0xFFFF5722)
    val ExpensesColor3 = Color(0xFFFF9800)
    val ExpensesColor4 = Color(0xFFFFB74D)
    val ExpensesColor5 = Color(0xFFF06292)
    val ExpensesColor6 = Color(0xFFBA68C8)
    val ExpensesColor7 = Color(0xFF7E57C2)
    val ExpensesColor8 = Color(0xFF5C6BC0)
}
