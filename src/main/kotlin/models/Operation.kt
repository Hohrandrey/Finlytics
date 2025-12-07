package models

import java.time.LocalDate

/**
 * Модель данных, представляющая финансовую операцию (доход или расход).
 *
 * @property id Уникальный идентификатор операции в базе данных
 * @property type Тип операции: "Доход" или "Расход"
 * @property amount Сумма операции (в рублях)
 * @property category Категория операции
 * @property date Дата совершения операции
 */
data class Operation(
    val id: Int,
    val type: String,
    val amount: Double,
    val category: String,
    val date: LocalDate
)
