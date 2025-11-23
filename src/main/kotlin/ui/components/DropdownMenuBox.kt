package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun DropdownMenuBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onValueChange(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
        // Клик по полю открывает меню
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(8.dp)
        ) { /* кликабельная зона */ }
            .clickable { expanded = true }
    }
}