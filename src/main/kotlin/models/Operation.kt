package models

import java.time.LocalDate

data class Operation(
    val id: Int,
    val type: String,
    val amount: Double,
    val category: String,
    val date: LocalDate
)