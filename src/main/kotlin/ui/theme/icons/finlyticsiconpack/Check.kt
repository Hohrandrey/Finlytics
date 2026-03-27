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
 * Иконка "Галочка" - символ подтверждения выбора.
 * Используется для обозначения выбранной категории в диалоге операции.
 *
 * Размер: 18x18 dp
 * Цвет: динамический (зависит от типа операции)
 * Стиль: контурная иконка с закругленными краями
 */
public val FinlyticsIconPack.Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check = Builder(name = "Check", defaultWidth = 18.0.dp, defaultHeight = 18.0.dp,
            viewportWidth = 18.0f, viewportHeight = 18.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                strokeLineWidth = 2.5f, strokeLineCap = Round, strokeLineJoin = StrokeJoinRound,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(2.0f, 9.0f)
                lineTo(7.0f, 14.0f)
                lineTo(16.0f, 4.0f)
            }
        }
            .build()
        return _check!!
    }

private var _check: ImageVector? = null