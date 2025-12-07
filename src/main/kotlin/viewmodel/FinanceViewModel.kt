package viewmodel

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.Operation
import repository.FinanceRepository
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/**
 * ViewModel для управления состоянием приложения и бизнес-логикой.
 * Следует архитектуре MVVM, обеспечивает реактивное обновление UI.
 */
class FinanceViewModel(private val repo: FinanceRepository) {
    // Хранит текущее состояние приложения
    private val _state = MutableStateFlow(FinanceState())
    val state: StateFlow<FinanceState> = _state.asStateFlow()

    // Текущий экран приложения
    var currentScreen by mutableStateOf("Overview")
        private set

    // Флаги для отображения диалогов
    var showOperationDialog by mutableStateOf(false)
        private set
    var showCategoryDialog by mutableStateOf(false)
        private set

    var editingOperation by mutableStateOf<Operation?>(null)
        private set

    var isIncomeCategory by mutableStateOf(true)
        private set

    // Инициализация данных при создании
    init {
        refresh()
    }

    /**
     * Пересчитывает статистику на основе списка операций.
     *
     * @param operations Список операций для анализа
     */
    private fun calculateStatistics(operations: List<Operation>) {
        // Суммируем доходы и расходы
        val income = operations.filter { it.type == "Доход" }.sumOf { it.amount }
        val expenses = operations.filter { it.type == "Расход" }.sumOf { it.amount }

        // Группируем расходы по категориям для диаграммы
        val expenseOperations = operations.filter { it.type == "Расход" }
        val expByCat = expenseOperations
            .groupBy { it.category }
            .mapValues { it.value.sumOf { op -> op.amount } }

        // Отладочная информация
        println("Статистика расчет:")
        println("  Всего операций: ${operations.size}")
        println("  Операций доходов: ${operations.count { it.type == "Доход" }}")
        println("  Операций расходов: ${expenseOperations.size}")
        println("  Категории расходов: ${expByCat.keys}")
        println("  Суммы по категориям: $expByCat")

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

    /**
     * Обновляет данные из репозитория и пересчитывает статистику.
     * Вызывается при изменении данных.
     */
    fun refresh() {
        println("Обновление данных...")
        val allOps = repo.getAllOperations()
        println("Операций загружено: ${allOps.size}")
        calculateStatistics(allOps)
    }

    /**
     * Применяет фильтр по временному периоду.
     *
     * @param period Название периода ("День", "Неделя", "Месяц", "Год", "Все время")
     */
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

    /**
     * Открывает диалог добавления новой операции.
     * Сбрасывает editingOperation в null для режима создания.
     */
    fun showAddOperation() {
        println("Показать диалог добавления операции")
        editingOperation = null
        showOperationDialog = true
    }

    /**
     * Открывает диалог редактирования существующей операции.
     *
     * @param op Операция для редактирования
     */
    fun showEditOperation(op: Operation) {
        println("Показать диалог редактирования операции: $op")
        editingOperation = op
        showOperationDialog = true
    }

    /**
     * Закрывает диалог операции и сбрасывает связанные состояния.
     */
    fun hideOperationDialog() {
        println("Скрыть диалог операции")
        showOperationDialog = false
        editingOperation = null
    }

    /**
     * Сохраняет операцию: добавляет новую или обновляет существующую.
     * Автоматически обновляет данные приложения после сохранения.
     *
     * @param op Операция для сохранения (при id=0 - добавление, иначе - обновление)
     */
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

    /**
     * Удаляет операцию из базы данных.
     *
     * @param op Операция для удаления
     */
    fun deleteOperation(op: Operation) {
        println("Удаление операции: $op")
        try {
            repo.deleteOperation(op.id, op.type)
            refresh()
        } catch (e: Exception) {
            println("Ошибка при удалении операции: ${e.message}")
        }
    }

    /**
     * Открывает диалог добавления новой категории.
     *
     * @param isIncome Тип добавляемой категории (true - доходы, false - расходы)
     */
    fun showAddCategory(isIncome: Boolean) {
        println("Показать диалог добавления категории. Тип: ${if (isIncome) "доходы" else "расходы"}")
        isIncomeCategory = isIncome
        showCategoryDialog = true
    }

    /**
     * Закрывает диалог добавления категории.
     */
    fun hideCategoryDialog() {
        println("Скрыть диалог категории")
        showCategoryDialog = false
    }

    /**
     * Сохраняет новую категорию в базу данных.
     * Проверяет уникальность имени категории перед добавлением.
     *
     * @param name Название новой категории
     */
    fun saveCategory(name: String) {
        println("Сохранение категории: '$name'. Тип: ${if (isIncomeCategory) "доходы" else "расходы"}")
        try {
            if (name.isBlank()) {
                println("Имя категории пустое")
                return
            }

            val trimmedName = name.trim()

            // Проверяем уникальность категории
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

    /**
     * Навигация между экранами приложения.
     *
     * @param screen Название целевого экрана ("Overview", "History", "Settings")
     */
    fun navigateTo(screen: String) {
        currentScreen = screen
    }

    /**
     * Удаляет категорию, если с ней не связано ни одной операции.
     *
     * @param name Название категории для удаления
     * @param isIncome Тип категории (true - доходы, false - расходы)
     */
    fun deleteCategory(name: String, isIncome: Boolean) {
        println("Удаление категории: '$name'. Тип: ${if (isIncome) "доходы" else "расходы"}")
        try {
            // Проверяем, есть ли операции с этой категорией
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
