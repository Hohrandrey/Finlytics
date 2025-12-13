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

public val FinlyticsIconPack.Date: ImageVector
    get() {
        if (_date != null) {
            return _date!!
        }
        _date = Builder(name = "Date", defaultWidth = 18.0.dp, defaultHeight = 20.0.dp,
                viewportWidth = 18.0f, viewportHeight = 20.0f).apply {
            path(fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFFF4F4F4)),
                    strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                    StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(1.0f, 7.0f)
                horizontalLineTo(17.0f)
                moveTo(1.0f, 7.0f)
                verticalLineTo(15.8f)
                curveTo(1.0f, 16.92f, 1.0f, 17.48f, 1.218f, 17.908f)
                curveTo(1.41f, 18.284f, 1.715f, 18.59f, 2.092f, 18.782f)
                curveTo(2.519f, 19.0f, 3.079f, 19.0f, 4.197f, 19.0f)
                horizontalLineTo(13.803f)
                curveTo(14.921f, 19.0f, 15.48f, 19.0f, 15.907f, 18.782f)
                curveTo(16.284f, 18.59f, 16.59f, 18.284f, 16.782f, 17.908f)
                curveTo(17.0f, 17.48f, 17.0f, 16.921f, 17.0f, 15.804f)
                verticalLineTo(7.0f)
                moveTo(1.0f, 7.0f)
                verticalLineTo(6.2f)
                curveTo(1.0f, 5.08f, 1.0f, 4.52f, 1.218f, 4.092f)
                curveTo(1.41f, 3.715f, 1.715f, 3.41f, 2.092f, 3.218f)
                curveTo(2.52f, 3.0f, 3.08f, 3.0f, 4.2f, 3.0f)
                horizontalLineTo(5.0f)
                moveTo(17.0f, 7.0f)
                verticalLineTo(6.197f)
                curveTo(17.0f, 5.079f, 17.0f, 4.519f, 16.782f, 4.092f)
                curveTo(16.59f, 3.715f, 16.284f, 3.41f, 15.907f, 3.218f)
                curveTo(15.48f, 3.0f, 14.92f, 3.0f, 13.8f, 3.0f)
                horizontalLineTo(13.0f)
                moveTo(13.0f, 1.0f)
                verticalLineTo(3.0f)
                moveTo(13.0f, 3.0f)
                horizontalLineTo(5.0f)
                moveTo(5.0f, 1.0f)
                verticalLineTo(3.0f)
            }
        }
        .build()
        return _date!!
    }

private var _date: ImageVector? = null
