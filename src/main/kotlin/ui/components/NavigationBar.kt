package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import viewmodel.FinanceViewModel
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*

@Composable
fun NavigationBar(viewModel: FinanceViewModel) {
    val currentScreen = viewModel.currentScreen

    Row(
        modifier = Modifier
            .wrapContentSize()
            .background(AppColors.DarkGreyColor, RoundedCornerShape(15.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationButton(
            text = "Добавить",
            isSelected = currentScreen == "AddNew",
            onClick = { viewModel.showAddOperation() },
            Icon = FinlyticsIconPack.Add
        )

        Spacer(Modifier.width(20.dp))

        NavigationButton(
            text = "Обзор",
            isSelected = currentScreen == "Overview",
            onClick = { viewModel.navigateTo("Overview") },
            Icon = FinlyticsIconPack.Statistic
        )

        Spacer(Modifier.width(20.dp))

        NavigationButton(
            text = "История",
            isSelected = currentScreen == "History",
            onClick = { viewModel.navigateTo("History") },
            Icon = FinlyticsIconPack.History
        )

        Spacer(Modifier.width(20.dp))

        NavigationButton(
            text = "Настройки",
            isSelected = currentScreen == "Settings",
            onClick = { viewModel.navigateTo("Settings") },
            Icon = FinlyticsIconPack.Settings
        )
    }
}

@Composable
fun NavigationButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    Icon: ImageVector
) {
    val backgroundColor = if (isSelected) AppColors.BlueColor else AppColors.LightGreyColor
    val textColor = AppColors.LightColor
    val iconColor = AppColors.LightColor
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(backgroundColor, RoundedCornerShape(15.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icon,
                contentDescription = text,
                modifier = Modifier.size(28.dp),
                tint = iconColor
            )

            if (isSelected) {
                Spacer(Modifier.width(5.dp))
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        letterSpacing = 0.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}