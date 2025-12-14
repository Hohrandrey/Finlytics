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
 * Векторное изображение иконки "Расходы".
 * Используется в пользовательском интерфейсе приложения Finlytics.
 * Размер по умолчанию: 24x16 dp.
 */
public val FinlyticsIconPack.Expenses: ImageVector
    get() {
        if (_expenses != null) {
            return _expenses!!
        }
        _expenses = Builder(name = "Expenses", defaultWidth = 24.0.dp, defaultHeight = 16.0.dp,
                viewportWidth = 24.0f, viewportHeight = 16.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(23.0f, 15.0f)
                lineTo(14.962f, 6.688f)
                curveTo(14.817f, 6.538f, 14.744f, 6.463f, 14.68f, 6.405f)
                curveTo(13.636f, 5.453f, 12.057f, 5.453f, 11.013f, 6.404f)
                curveTo(10.948f, 6.463f, 10.875f, 6.538f, 10.73f, 6.688f)
                curveTo(10.586f, 6.837f, 10.514f, 6.912f, 10.449f, 6.971f)
                curveTo(9.405f, 7.922f, 7.825f, 7.922f, 6.781f, 6.971f)
                curveTo(6.717f, 6.912f, 6.645f, 6.837f, 6.501f, 6.689f)
                lineTo(1.0f, 1.0f)
                moveTo(23.0f, 15.0f)
                lineTo(22.999f, 6.6f)
                moveTo(23.0f, 15.0f)
                horizontalLineTo(14.75f)
            }
        }
        .build()
        return _expenses!!
    }

private var _expenses: ImageVector? = null
