package viewmodel

import androidx.compose.runtime.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.math.pow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import models.Operation
import repository.FinanceRepository

/**
 * ViewModel для управления состоянием приложения и бизнес-логикой.
 * Следует архитектуре MVVM, обеспечивает реактивное обновление UI.
 */
class FinanceViewModel(private val repo: FinanceRepository) {
    // Создаем DecimalFormat с явным указанием локали для корректного парсинга
    private val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))

    // Хранит текущее состояние приложения
    private val _state = MutableStateFlow(FinanceState())
    val state: StateFlow<FinanceState> = _state.asStateFlow()

    // Текущий экран приложения
    var currentScreen by mutableStateOf("Overview")
        private set

    // Флаги для отображения диалогов
    var showOperationDialog by mutableStateOf(false)
        private set
    var showAddCategoryDialog by mutableStateOf(false)
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
            .mapValues { entry ->
                val sum = entry.value.sumOf { op -> op.amount }
                sum
            }

        // Подробная отладочная информация
        println("\n=== CALCULATE STATISTICS ===")
        println("Всего операций: ${operations.size}")
        println("Операций доходов: ${operations.count { it.type == "Доход" }}")
        println("Операций расходов: ${expenseOperations.size}")
        println("Общая сумма доходов: $income руб.")
        println("Общая сумма расходов: $expenses руб.")
        println("Баланс: ${income - expenses} руб.")

        if (expByCat.isNotEmpty()) {
            println("Детализация расходов по категориям:")
            expByCat.forEach { (category, amount) ->
                println("  - $category: $amount руб.")
            }
        } else {
            println("Расходы по категориям: нет данных")
        }
        println("============================\n")

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
        showAddCategoryDialog = true
    }

    /**
     * Закрывает диалог добавления категории.
     */
    fun hideCategoryDialog() {
        println("Скрыть диалог категории")
        showAddCategoryDialog = false
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

    private fun Double.roundTo(decimals: Int): Double {
        val multiplier = 10.0.pow(decimals)
        return (this * multiplier).roundToInt() / multiplier
    }
}

// Добавляем функцию roundToDouble как extension
fun Double.roundToDouble(): Double = kotlin.math.round(this)
