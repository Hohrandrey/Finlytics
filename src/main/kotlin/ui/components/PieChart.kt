package ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

/**
 * Компонент для отображения круговой диаграммы распределения расходов по категориям.
 * Обновленная версия, которая отображает настоящую круговую диаграмму (pie chart).
 *
 * @param data Карта данных, где ключ - название категории, значение - сумма расходов
 * @param modifier Модификатор для настройки внешнего вида компонента
 * @param colors Список цветов для сегментов диаграммы (по умолчанию использует палитру из дизайна)
 */
@Composable
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFF2176FF),     // blue
        Color(0xFF00D9FB),     // emerald
        Color(0xFF4ADE80),     // green
        Color(0xFFEAFF00),     // lime
        Color(0xFFECB324),     // yellow
        Color(0xFFEF4444),     // red
        Color(0xFFFF00A6),     // rose
        Color(0xFF7300FF)      // purple
    )
) {
    println("\n=== PIE CHART ===")
    println("Полученные данные: $data")
    println("Количество категорий: ${data.size}")
    println("Данные не пустые: ${data.isNotEmpty()}")

    // Фильтруем данные, оставляя только категории с положительными значениями
    val filteredData = data.filterValues { it > 0 }.toList()
    val total = filteredData.sumOf { it.second }

    println("Отфильтрованные данные: $filteredData")
    println("Общая сумма расходов: $total")

    if (filteredData.isNotEmpty()) {
        println("Детализация по категориям:")
        filteredData.forEach { (category, amount) ->
            val percentage = (amount / total * 100)
            println("  - $category: $amount руб. (${"%.1f".format(percentage)}%)")
        }
    }
    println("==================\n")

    // Создаем DecimalFormat с явным указанием локали для корректного форматирования
    val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    val percentFormat = DecimalFormat("0.0", DecimalFormatSymbols(Locale.US))

    // Если данных нет, показываем сообщение
    if (total == 0.0 || filteredData.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Иконка пустой диаграммы (круг)
                Canvas(
                    modifier = Modifier.size(100.dp)
                ) {
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        style = Stroke(width = 4f)
                    )
                }
                Text(
                    "Нет данных по расходам",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }
        return
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Основная круговая диаграмма
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            val canvasSize = min(size.width, size.height)
            val radius = canvasSize / 2 - 20
            val centerOffset = Offset(size.width / 2, size.height / 2)

            // Вычисляем углы для каждого сегмента
            var startAngle = -90f // Начинаем сверху (12 часов)

            filteredData.forEachIndexed { index, (_, amount) ->
                val sweepAngle = (amount / total * 360).toFloat()

                // Рисуем сегмент
                drawArc(
                    color = colors.getOrElse(index) { colors.last() },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(
                        centerOffset.x - radius,
                        centerOffset.y - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )

                startAngle += sweepAngle
            }

            // Добавляем тонкую белую обводку между сегментами
            startAngle = -90f
            filteredData.forEachIndexed { index, (_, amount) ->
                val sweepAngle = (amount / total * 360).toFloat()

                // Рисуем разделительную линию
                val lineLength = radius + 5
                val lineStartX = centerOffset.x + cos(Math.toRadians(startAngle.toDouble())) * radius
                val lineStartY = centerOffset.y + sin(Math.toRadians(startAngle.toDouble())) * radius
                val lineEndX = centerOffset.x + cos(Math.toRadians(startAngle.toDouble())) * lineLength
                val lineEndY = centerOffset.y + sin(Math.toRadians(startAngle.toDouble())) * lineLength

                drawLine(
                    color = Color.White,
                    start = Offset(lineStartX.toFloat(), lineStartY.toFloat()),
                    end = Offset(lineEndX.toFloat(), lineEndY.toFloat()),
                    strokeWidth = 2f
                )

                startAngle += sweepAngle
            }
        }

        // Легенда диаграммы (справа или снизу в зависимости от размера)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(250.dp)
                .padding(16.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Расходы по категориям:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            filteredData.forEachIndexed { index, (category, amount) ->
                val percentage = (amount / total * 100)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                colors.getOrElse(index) { colors.last() },
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                    Text(
                        text = category,
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${percentFormat.format(percentage)}%",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Отображение общей суммы в центре диаграммы (опционально)
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = CircleShape
                )
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Итого:",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    decimalFormat.format(total),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "₽",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Вспомогательная функция для вычисления координат на окружности
 */
private fun Offset(
    radius: Float,
    angleDeg: Float
): Offset {
    val angleRad = angleDeg * (PI / 180f).toFloat()
    return Offset(
        x = radius * cos(angleRad),
        y = radius * sin(angleRad)
    )
}