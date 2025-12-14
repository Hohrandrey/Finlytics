package ui.theme.icons.finlyticsiconpack

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ui.theme.icons.FinlyticsIconPack

/**
 * Векторное изображение иконки "Кошелек".
 * Используется в пользовательском интерфейсе приложения Finlytics.
 * Размер по умолчанию: 22x22 dp.
 */
public val FinlyticsIconPack.Wallet: ImageVector
    get() {
        if (_wallet != null) {
            return _wallet!!
        }
        _wallet = Builder(name = "Wallet", defaultWidth = 22.0.dp, defaultHeight = 22.0.dp,
                viewportWidth = 22.0f, viewportHeight = 22.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFECB324)),
                    strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(6.0f, 4.17f)
                lineTo(16.0f, 4.17f)
                arcTo(5.0f, 5.0f, 0.0f, false, true, 21.0f, 9.17f)
                lineTo(21.0f, 15.17f)
                arcTo(5.0f, 5.0f, 0.0f, false, true, 16.0f, 20.17f)
                lineTo(6.0f, 20.17f)
                arcTo(5.0f, 5.0f, 0.0f, false, true, 1.0f, 15.17f)
                lineTo(1.0f, 9.17f)
                arcTo(5.0f, 5.0f, 0.0f, false, true, 6.0f, 4.17f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFECB324)),
                    strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.0f, 4.67f)
                curveTo(18.0f, 2.346f, 15.868f, 0.609f, 13.592f, 1.077f)
                lineTo(4.992f, 2.848f)
                curveTo(2.668f, 3.326f, 1.0f, 5.372f, 1.0f, 7.745f)
                lineTo(1.0f, 11.17f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFECB324)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(5.0f, 15.67f)
                horizontalLineTo(11.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFECB324)),
                    strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(14.0f, 12.17f)
                curveTo(14.0f, 10.789f, 15.119f, 9.67f, 16.5f, 9.67f)
                horizontalLineTo(21.0f)
                verticalLineTo(14.67f)
                horizontalLineTo(16.5f)
                curveTo(15.119f, 14.67f, 14.0f, 13.55f, 14.0f, 12.17f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFECB324)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(16.5f, 12.17f)
                horizontalLineTo(16.7f)
            }
        }
        .build()
        return _wallet!!
    }

private var _wallet: ImageVector? = null
