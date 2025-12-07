package viewmodel

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.Operation
import repository.FinanceRepository
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class FinanceViewModel(private val repo: FinanceRepository) {
    private val _state = MutableStateFlow(FinanceState())
    val state: StateFlow<FinanceState> = _state.asStateFlow()

    var currentScreen by mutableStateOf("Overview")
        private set

    var showOperationDialog by mutableStateOf(false)
        private set

    var showCategoryDialog by mutableStateOf(false)
        private set

    var editingOperation by mutableStateOf<Operation?>(null)
        private set

    var isIncomeCategory by mutableStateOf(true)
        private set

    init {
        refresh()
    }

    private fun calculateStatistics(operations: List<Operation>) {
        val income = operations.filter { it.type == "Доход" }.sumOf { it.amount }
        val expenses = operations.filter { it.type == "Расход" }.sumOf { it.amount }
        val expByCat = operations.filter { it.type == "Расход" }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { op -> op.amount } }

        _state.value = _state.value.copy(
            operations = operations,
            totalIncome = income,
            totalExpenses = expenses,
            balance = income - expenses,
            expensesByCategory = expByCat,
            incomeCategories = repo.getIncomeCategories(),
            expenseCategories = repo.getExpenseCategories()
        )
    }

    fun refresh() {
        println("Обновление данных...")
        val allOps = repo.getAllOperations()
        println("Операций загружено: ${allOps.size}")
        calculateStatistics(allOps)
    }

    fun applyFilter(period: String) {
        val today = LocalDate.now()
        val operations = when (period) {
            "День" -> repo.getOperations(today, today)
            "Неделя" -> repo.getOperations(today.minusDays(7), today)
            "Месяц" -> repo.getOperations(
                today.withDayOfMonth(1),
                today.with(TemporalAdjusters.lastDayOfMonth())
            )
            "Год" -> repo.getOperations(
                today.withDayOfYear(1),
                today.with(TemporalAdjusters.lastDayOfYear())
            )
            else -> repo.getAllOperations()
        }
        calculateStatistics(operations)
    }

    fun showAddOperation() {
        println("Показать диалог добавления операции")
        editingOperation = null
        showOperationDialog = true
    }

    fun showEditOperation(op: Operation) {
        println("Показать диалог редактирования операции: $op")
        editingOperation = op
        showOperationDialog = true
    }

    fun hideOperationDialog() {
        println("Скрыть диалог операции")
        showOperationDialog = false
        editingOperation = null
    }

    fun saveOperation(op: Operation) {
        println("Сохранение операции: $op")
        try {
            if (op.id == 0) {
                repo.addOperation(op)
            } else {
                repo.updateOperation(op)
            }
            refresh()
            hideOperationDialog()
        } catch (e: Exception) {
            println("Ошибка при сохранении операции: ${e.message}")
        }
    }

    fun deleteOperation(op: Operation) {
        println("Удаление операции: $op")
        try {
            repo.deleteOperation(op.id, op.type)
            refresh()
        } catch (e: Exception) {
            println("Ошибка при удалении операции: ${e.message}")
        }
    }

    fun showAddCategory(isIncome: Boolean) {
        println("Показать диалог добавления категории. Тип: ${if (isIncome) "доходы" else "расходы"}")
        isIncomeCategory = isIncome
        showCategoryDialog = true
    }

    fun hideCategoryDialog() {
        println("Скрыть диалог категории")
        showCategoryDialog = false
    }

    fun saveCategory(name: String) {
        println("Сохранение категории: '$name'. Тип: ${if (isIncomeCategory) "доходы" else "расходы"}")
        try {
            if (name.isBlank()) {
                println("Имя категории пустое")
                return
            }

            val trimmedName = name.trim()

            val existingCategories = if (isIncomeCategory)
                state.value.incomeCategories
            else
                state.value.expenseCategories

            if (existingCategories.any { it.equals(trimmedName, ignoreCase = true) }) {
                println("Категория '$trimmedName' уже существует")
                return
            }

            val success = repo.addCategory(trimmedName, isIncomeCategory)
            println("Категория добавлена. Успех: $success")

            if (success) {
                refresh()
                hideCategoryDialog()
            } else {
                println("Не удалось добавить категорию")
            }
        } catch (e: Exception) {
            println("Ошибка при добавлении категории: ${e.message}")
        }
    }

    fun navigateTo(screen: String) {
        currentScreen = screen
    }

    fun deleteCategory(name: String, isIncome: Boolean) {
        println("Удаление категории: '$name'. Тип: ${if (isIncome) "доходы" else "расходы"}")
        try {
            val hasOperations = if (isIncome) {
                state.value.operations.any { it.type == "Доход" && it.category == name }
            } else {
                state.value.operations.any { it.type == "Расход" && it.category == name }
            }

            if (hasOperations) {
                println("Нельзя удалить категорию '$name', так как есть операции с этой категорией")
                return
            }

            repo.deleteCategory(name, isIncome)
            refresh()
        } catch (e: Exception) {
            println("Ошибка при удалении категории: ${e.message}")
            e.printStackTrace()
        }
    }
}
