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
 * Иконка "Вправо" - стрелка навигации вправо.
 * Используется для навигации по периодам (следующий день, неделя, месяц, год).
 *
 * Размер: 20x20 dp
 * Цвет: светлый (#F4F4F4)
 * Стиль: контурная иконка с закругленными краями
 */
public val FinlyticsIconPack.Right: ImageVector
    get() {
        if (_right != null) {
            return _right!!
        }
        _right = Builder(name = "Right", defaultWidth = 20.0.dp, defaultHeight = 20.0.dp,
            viewportWidth = 20.0f, viewportHeight = 20.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin = StrokeJoinRound,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(8.333f, 14.167f)
                lineTo(12.5f, 10.0f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin = StrokeJoinRound,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(12.5f, 10.0f)
                lineTo(8.333f, 5.833f)
            }
        }
            .build()
        return _right!!
    }

private var _right: ImageVector? = null