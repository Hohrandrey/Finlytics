package ui.theme.icons.finlyticsiconpack

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Round as StrokeJoinRound
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ui.theme.icons.FinlyticsIconPack

/**
 * Иконка "Закрыть" - стилизованный знак крестика (X).
 * Используется для закрытия диалоговых окон и модальных окон.
 *
 * Размер: 19x19 dp
 * Цвет: светлый (#F4F4F4)
 * Стиль: контурная иконка с закругленными краями
 */
public val FinlyticsIconPack.Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close = Builder(name = "Close", defaultWidth = 19.0.dp, defaultHeight = 19.0.dp,
            viewportWidth = 19.0f, viewportHeight = 19.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                strokeLineWidth = 3.0f, strokeLineCap = Round, strokeLineJoin = StrokeJoinRound,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(1.5f, 1.5f)
                lineTo(17.333f, 17.333f)
                moveTo(1.5f, 17.333f)
                lineTo(17.333f, 1.5f)
            }
        }
            .build()
        return _close!!
    }

private var _close: ImageVector? = null