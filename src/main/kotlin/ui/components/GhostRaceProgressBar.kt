package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppColors

/**
 * Компонент прогресс-бара для функционала "Гонка с призраком".
 * Отображает сравнение расходов текущего выбранного периода с расходами за аналогичный предыдущий период.
 * Прогресс-бар заполняется в зависимости от отношения текущих расходов к расходам предыдущего периода.
 * Цветовая индикация:
 * - Зелёный: менее 80% от лимита
 * - Жёлтый: от 80% до 100%
 * - Красный: более 100% (превышение лимита)
 *
 * Визуально компонент содержит:
 * - Заголовок с текущим процентом
 * - Горизонтальный прогресс-бар с числовой меткой посередине
 * - Строку с суммами: потрачено и лимит прошлого периода
 *
 * @param currentExpenses Сумма расходов за текущий выбранный период
 * @param previousExpenses Сумма расходов за аналогичный предыдущий период (лимит)
 * @param percent Отношение текущих расходов к предыдущим (currentExpenses / previousExpenses)
 * @param modifier Модификатор для настройки внешнего вида и расположения компонента
 */
@Composable
fun GhostRaceProgressBar(
    currentExpenses: Double,
    previousExpenses: Double,
    percent: Double,
    modifier: Modifier = Modifier
) {
    // Цвет прогресса
    val progressColor = when {
        percent < 0.8 -> AppColors.GreenColor
        percent <= 1.0 -> AppColors.YellowColor
        else -> AppColors.RedColor
    }
    // Заполнение не может превышать 100%
    val fillFraction = if (percent > 1.0) 1.0f else percent.toFloat()

    Surface(
        color = AppColors.DarkGreyColor,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Заголовок и процент
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Гонка с призраком",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.LightColor
                )
                Text(
                    text = String.format("%.1f%%", percent * 100),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = progressColor
                )
            }

            // Прогресс-бар с меткой посередине
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppColors.LightGreyColor.copy(alpha = 0.3f))
            ) {
                // Заполненная часть
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = fillFraction)
                        .fillMaxHeight()
                        .background(progressColor)
                )
            }

            // Строка с суммами
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Потрачено: ${String.format("%.2f", currentExpenses)} ₽",
                    fontSize = 16.sp,
                    color = progressColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Лимит прошлого периода: ${String.format("%.2f", previousExpenses)} ₽",
                    fontSize = 16.sp,
                    color = AppColors.LightColor
                )
            }
        }
    }
}
