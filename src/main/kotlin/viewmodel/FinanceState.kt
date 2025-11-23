package viewmodel

import models.Operation

data class FinanceState(
    val operations: List<Operation> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val incomeCategories: List<String> = emptyList(),
    val expenseCategories: List<String> = emptyList(),
    val showOperationDialog: Boolean = false,
    val showCategoryDialog: Boolean = false,
    val editingOperation: Operation? = null,
    val editingCategory: String? = null,
    val isIncomeCategory: Boolean = true
)