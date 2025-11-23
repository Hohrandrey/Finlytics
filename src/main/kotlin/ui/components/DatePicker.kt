package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePicker(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    OutlinedTextField(
        value = date.format(formatter),
        onValueChange = {},
        label = { Text("Дата") },
        readOnly = true,
        modifier = modifier
            .clickable {
                // В реальном приложении — показать DatePickerDialog
                // Пока просто +1 день для теста
                onDateChange(date.plusDays(1))
            }
    )
}