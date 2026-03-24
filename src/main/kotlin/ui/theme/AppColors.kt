package ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Объект, содержащий цветовую палитру приложения "Finlytics".
 * Определяет основные и вспомогательные цвета для темной темы интерфейса.
 *
 * Цветовая схема:
 * - Основные цвета: темный фон, серые оттенки для элементов
 * - Акцентные цвета: синий для выделения, красный для расходов, зеленый для доходов, желтый для баланса
 * - Цвета для диаграмм: 8 цветов для доходов и 8 цветов для расходов
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
object AppColors {
    // Основные цвета интерфейса
    val DarkColor = Color(0xFF1E1E1E)      // Основной фон приложения
    val DarkGreyColor = Color(0xFF2C2C2C)   // Фон панелей и карточек
    val LightGreyColor = Color(0xFF444444)  // Фон кнопок и элементов ввода
    val LightColor = Color(0xFFF4F4F4)      // Основной цвет текста и иконок

    // Акцентные цвета
    val BlueColor = Color(0xFF2176FF)       // Цвет выделения, активных элементов
    val RedColor = Color(0xFFEF4444)        // Цвет для расходов, кнопка удаления
    val GreenColor = Color(0xFF4ADE80)      // Цвет для доходов
    val YellowColor = Color(0xFFECB324)     // Цвет для баланса, предупреждений

    // Цветовая палитра для круговой диаграммы доходов (от синего к желтому)
    val IncomeColor1 = Color(0xFF1565C0)     // Темно-синий
    val IncomeColor2 = Color(0xFF42A5F5)     // Синий
    val IncomeColor3 = Color(0xFF29B6F6)     // Голубой
    val IncomeColor4 = Color(0xFF26A69A)     // Бирюзовый
    val IncomeColor5 = Color(0xFF66BB6A)     // Зеленый
    val IncomeColor6 = Color(0xFF9CCC65)     // Светло-зеленый
    val IncomeColor7 = Color(0xFFD4E157)     // Лаймовый
    val IncomeColor8 = Color(0xFFFFCA28)     // Желтый

    // Цветовая палитра для круговой диаграммы расходов (от красного к фиолетовому)
    val ExpensesColor1 = Color(0xFFD32F2F)    // Темно-красный
    val ExpensesColor2 = Color(0xFFFF5722)    // Оранжевый
    val ExpensesColor3 = Color(0xFFFF9800)    // Оранжево-желтый
    val ExpensesColor4 = Color(0xFFFFB74D)    // Светло-оранжевый
    val ExpensesColor5 = Color(0xFFF06292)    // Розовый
    val ExpensesColor6 = Color(0xFFBA68C8)    // Фиолетовый
    val ExpensesColor7 = Color(0xFF7E57C2)    // Темно-фиолетовый
    val ExpensesColor8 = Color(0xFF5C6BC0)    // Индиго
}