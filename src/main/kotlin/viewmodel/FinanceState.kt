package viewmodel

import models.Operation

/**
 * Класс состояния приложения, содержащий все данные, необходимые для отображения UI.
 * Состояние является иммутабельным (immutable) и обновляется через Flow в архитектуре MVVM.
 *
 * @property operations Список всех финансовых операций (доходы и расходы)
 * @property totalIncome Общая сумма всех доходов за выбранный период
 * @property totalExpenses Общая сумма всех расходов за выбранный период
 * @property balance Текущий баланс (доходы минус расходы)
 * @property expensesByCategory Распределение расходов по категориям для построения диаграмм
 * @property incomeCategories Список доступных категорий доходов
 * @property expenseCategories Список доступных категорий расходов
 * @property editingCategory Категория, находящаяся в процессе редактирования (в текущей реализации не используется)
 */
data class FinanceState(
    val operations: List<Operation> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val incomeCategories: List<String> = emptyList(),
    val expenseCategories: List<String> = emptyList(),
    val editingCategory: String? = null
)
