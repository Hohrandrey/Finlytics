package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Button(
                onClick = { expanded = !expanded }
            ) {
                Text(value.ifEmpty { "Выберите категорию" })
            }
        }

        if (expanded) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column {
                    if (items.isEmpty()) {
                        Text(
                            "Нет категорий",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        items.forEach { item ->
                            Button(
                                onClick = {
                                    onValueChange(item)
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }
        }
    }
}
