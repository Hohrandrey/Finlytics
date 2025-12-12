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

public val FinlyticsIconPack.Statistic: ImageVector
    get() {
        if (_statistic != null) {
            return _statistic!!
        }
        _statistic = Builder(name = "Statistic", defaultWidth = 28.0.dp, defaultHeight = 28.0.dp,
                viewportWidth = 28.0f, viewportHeight = 28.0f).apply {
            path(fill = SolidColor(Color(0xFFF4F4F4)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(16.227f, 5.9f)
                curveTo(16.227f, 5.072f, 15.556f, 4.4f, 14.727f, 4.4f)
                curveTo(13.899f, 4.4f, 13.227f, 5.072f, 13.227f, 5.9f)
                horizontalLineTo(14.727f)
                horizontalLineTo(16.227f)
                close()
                moveTo(14.727f, 19.9f)
                horizontalLineTo(16.227f)
                verticalLineTo(5.9f)
                horizontalLineTo(14.727f)
                horizontalLineTo(13.227f)
                verticalLineTo(19.9f)
                horizontalLineTo(14.727f)
                close()
            }
            path(fill = SolidColor(Color(0xFFF4F4F4)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(22.242f, 10.45f)
                curveTo(22.242f, 9.622f, 21.57f, 8.95f, 20.742f, 8.95f)
                curveTo(19.913f, 8.95f, 19.242f, 9.622f, 19.242f, 10.45f)
                horizontalLineTo(20.742f)
                horizontalLineTo(22.242f)
                close()
                moveTo(20.742f, 19.9f)
                horizontalLineTo(22.242f)
                verticalLineTo(10.45f)
                horizontalLineTo(20.742f)
                horizontalLineTo(19.242f)
                verticalLineTo(19.9f)
                horizontalLineTo(20.742f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 3.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(3.5f, 4.5f)
                verticalLineTo(20.05f)
                curveTo(3.5f, 21.707f, 4.843f, 23.05f, 6.5f, 23.05f)
                horizontalLineTo(24.751f)
            }
            path(fill = SolidColor(Color(0xFFF4F4F4)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(10.614f, 15.35f)
                curveTo(10.614f, 14.521f, 9.942f, 13.85f, 9.114f, 13.85f)
                curveTo(8.285f, 13.85f, 7.614f, 14.521f, 7.614f, 15.35f)
                horizontalLineTo(9.114f)
                horizontalLineTo(10.614f)
                close()
                moveTo(9.114f, 19.9f)
                horizontalLineTo(10.614f)
                verticalLineTo(15.35f)
                horizontalLineTo(9.114f)
                horizontalLineTo(7.614f)
                verticalLineTo(19.9f)
                horizontalLineTo(9.114f)
                close()
            }
        }
        .build()
        return _statistic!!
    }

private var _statistic: ImageVector? = null
