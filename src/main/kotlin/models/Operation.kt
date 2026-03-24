package models

import java.time.LocalDate

/**
 * Модель данных, представляющая финансовую операцию (доход или расход).
 *
 * @property id Уникальный идентификатор операции в базе данных
 * @property type Тип операции: "Доход" или "Расход"
 * @property amount Сумма операции (в рублях)
 * @property category Категория операции (например, "Еда", "Зарплата", "Транспорт")
 * @property date Дата совершения операции
 * @property name Описание операции (необязательное поле)
 *
 * @author Finlytics Team
 * @since 1.0.0
 */
data class Operation(
    val id: Int,
    val type: String,
    val amount: Double,
    val category: String,
    val date: LocalDate,
    val name: String?
)