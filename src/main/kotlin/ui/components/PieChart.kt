/*package ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(data: Map<String, Double>, modifier: Modifier = Modifier) {
    val total = data.values.sum()
    if (total == 0.0) {
        Canvas(modifier) {
            drawCircle(Color(0xFF333333), radius = size.minDimension / 3)
        }
        return
    }

    val colors = listOf(
        Color(0xFFEF5350), Color(0xFFEC407A), Color(0xFFAB47BC),
        Color(0xFF7E57C2), Color(0xFF5C6BC0), Color(0xFF42A5F5),
        Color(0xFF29B6F6), Color(0xFF26C6DA), Color(0xFF26A69A)
    )

    var startAngle = 0f

    Canvas(modifier) {
        val radius = size.minDimension / 2
        val center = Offset(radius, radius)

        data.forEachIndexed { index, (category, value) ->
            val sweep = (value / total * 360).toFloat()
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                topLeft = Offset.Zero,
                size = Size(size.width, size.height)
            )
            startAngle += sweep
        }
    }
}*/