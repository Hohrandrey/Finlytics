package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var dateText by remember { mutableStateOf(selectedDate.format(formatter)) }
    var error by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = dateText,
            onValueChange = { newText ->
                dateText = newText
                error = false

                try {
                    val parsedDate = LocalDate.parse(
                        newText.replace(".", "-").replace("/", "-"),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
                    onDateSelected(parsedDate)
                } catch (e: DateTimeParseException) {
                    try {
                        val parsedDate = LocalDate.parse(newText)
                        onDateSelected(parsedDate)
                    } catch (e2: DateTimeParseException) {
                        error = true
                    }
                }
            },
            label = { Text("Дата") },
            placeholder = { Text("ДД.ММ.ГГГГ") },
            modifier = Modifier.fillMaxWidth(),
            isError = error,
            singleLine = true
        )

        if (error) {
            Text(
                text = "Формат даты: ДД.ММ.ГГГГ",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                "Сегодня" to LocalDate.now(),
                "Вчера" to LocalDate.now().minusDays(1),
                "Завтра" to LocalDate.now().plusDays(1)
            ).forEach { (label, date) ->
                Button(
                    onClick = {
                        onDateSelected(date)
                        dateText = date.format(formatter)
                        error = false
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedDate == date)
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.surface
                    )
                ) {
                    Text(
                        label,
                        color = if (selectedDate == date)
                            MaterialTheme.colors.onPrimary
                        else
                            MaterialTheme.colors.onSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val newDate = selectedDate.minusDays(1)
                    onDateSelected(newDate)
                    dateText = newDate.format(formatter)
                },
                modifier = Modifier.weight(1f).padding(end = 2.dp)
            ) {
                Text("-1 день")
            }

            Button(
                onClick = {
                    val newDate = selectedDate.plusDays(1)
                    onDateSelected(newDate)
                    dateText = newDate.format(formatter)
                },
                modifier = Modifier.weight(1f).padding(start = 2.dp)
            ) {
                Text("+1 день")
            }
        }
    }
}
