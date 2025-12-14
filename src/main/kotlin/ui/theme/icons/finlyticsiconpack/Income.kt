package ui.theme.icons.finlyticsiconpack

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ui.theme.icons.FinlyticsIconPack

/**
 * Векторное изображение иконки "Доходы".
 * Используется в пользовательском интерфейсе приложения Finlytics.
 * Размер по умолчанию: 24x16 dp.
 */
public val FinlyticsIconPack.Income: ImageVector
    get() {
        if (_income != null) {
            return _income!!
        }
        _income = Builder(name = "Income", defaultWidth = 24.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 24.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(23.0f, 1.0f)
                lineTo(14.962f, 9.313f)
                curveTo(14.817f, 9.462f, 14.744f, 9.537f, 14.68f, 9.595f)
                curveTo(13.636f, 10.547f, 12.057f, 10.547f, 11.013f, 9.596f)
                curveTo(10.948f, 9.537f, 10.875f, 9.462f, 10.73f, 9.312f)
                curveTo(10.586f, 9.163f, 10.514f, 9.088f, 10.449f, 9.029f)
                curveTo(9.405f, 8.078f, 7.825f, 8.078f, 6.781f, 9.029f)
                curveTo(6.717f, 9.088f, 6.645f, 9.163f, 6.501f, 9.311f)
                lineTo(1.0f, 15.0f)
                moveTo(23.0f, 1.0f)
                lineTo(22.999f, 9.4f)
                moveTo(23.0f, 1.0f)
                horizontalLineTo(14.75f)
            }
        }
        .build()
        return _income!!
    }

private var _income: ImageVector? = null
