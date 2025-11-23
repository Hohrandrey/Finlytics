package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.Operation
import repository.FinanceRepository
import java.time.LocalDate

class FinanceViewModel(private val repo: FinanceRepository) {
    private val _state = MutableStateFlow(FinanceState())
    val state: StateFlow<FinanceState> = _state.asStateFlow()
    var currentScreen by mutableStateOf("Overview")

    init { refresh() }

    fun refresh() {
        val ops = repo.getAllOperations()
        val income = ops.filter { it.type == "Доход" }.sumOf { it.amount }
        val expenses = ops.filter { it.type == "Расход" }.sumOf { it.amount }
        val expByCat = ops.filter { it.type == "Расход" }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { op -> op.amount } }

        _state.value = _state.value.copy(
            operations = ops,
            totalIncome = income,
            totalExpenses = expenses,
            balance = income - expenses,
            expensesByCategory = expByCat,
            incomeCategories = repo.getIncomeCategories(),
            expenseCategories = repo.getExpenseCategories()
        )
    }

    fun applyFilter(period: String) {
        val (from, to) = when (period) {
            "День" -> LocalDate.now() to LocalDate.now()
            "Неделя" -> LocalDate.now().minusDays(6) to LocalDate.now()
            "Месяц" -> LocalDate.now().withDayOfMonth(1) to LocalDate.now()
            "Год" -> LocalDate.now().withDayOfYear(1) to LocalDate.now()
            else -> LocalDate.MIN to LocalDate.MAX
        }
        val filtered = repo.getOperations(from, to)
        // Пересчёт аналогично refresh()
        refresh() // пока просто обновляем всё
    }

    fun showAddOperation() = _state.value.copy(showOperationDialog = true)
    fun showEditOperation(op: Operation) = _state.value.copy(showOperationDialog = true, editingOperation = op)
    fun hideOperationDialog() = _state.value.copy(showOperationDialog = false, editingOperation = null)

    fun saveOperation(op: Operation) {
        if (op.id == 0) repo.addOperation(op) else repo.updateOperation(op)
        refresh()
        hideOperationDialog()
    }

    fun deleteOperation(op: Operation) {
        repo.deleteOperation(op.id, op.type)
        refresh()
    }

    fun showAddCategory(isIncome: Boolean) = _state.value.copy(showCategoryDialog = true, isIncomeCategory = isIncome)
    fun hideCategoryDialog() = _state.value.copy(showCategoryDialog = false)

    fun saveCategory(name: String) {
        repo.addCategory(name, _state.value.isIncomeCategory)
        refresh()
        hideCategoryDialog()
    }

    fun deleteCategory(name: String, isIncome: Boolean) {
        repo.deleteCategory(name, isIncome)
        refresh()
    }
}