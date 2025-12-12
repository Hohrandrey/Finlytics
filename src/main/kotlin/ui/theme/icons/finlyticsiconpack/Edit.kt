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

public val FinlyticsIconPack.Edit: ImageVector
    get() {
        if (_edit != null) {
            return _edit!!
        }
        _edit = Builder(name = "Edit", defaultWidth = 28.0.dp, defaultHeight = 28.0.dp,
                viewportWidth = 28.0f, viewportHeight = 28.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(21.784f, 14.7f)
                verticalLineTo(17.512f)
                curveTo(21.784f, 20.274f, 19.545f, 22.512f, 16.784f, 22.512f)
                horizontalLineTo(11.16f)
                curveTo(8.399f, 22.512f, 6.16f, 20.274f, 6.16f, 17.512f)
                verticalLineTo(11.888f)
                curveTo(6.16f, 9.127f, 8.399f, 6.888f, 11.16f, 6.888f)
                horizontalLineTo(13.972f)
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(18.275f, 6.455f)
                curveTo(19.055f, 5.674f, 20.321f, 5.673f, 21.102f, 6.454f)
                lineTo(22.024f, 7.376f)
                curveTo(22.798f, 8.15f, 22.806f, 9.404f, 22.042f, 10.188f)
                lineTo(16.212f, 16.169f)
                curveTo(15.648f, 16.748f, 14.874f, 17.075f, 14.065f, 17.075f)
                lineTo(12.965f, 17.075f)
                curveTo(12.113f, 17.075f, 11.432f, 16.364f, 11.467f, 15.511f)
                lineTo(11.517f, 14.343f)
                curveTo(11.548f, 13.591f, 11.861f, 12.879f, 12.392f, 12.348f)
                lineTo(18.275f, 6.455f)
                close()
            }
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(17.209f, 7.642f)
                lineTo(20.694f, 11.126f)
            }
        }
        .build()
        return _edit!!
    }

private var _edit: ImageVector? = null
