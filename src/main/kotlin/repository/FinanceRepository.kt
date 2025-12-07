package repository

import db.database_request.*
import models.Operation
import java.time.LocalDate

/**
 * Репозиторий для работы с финансовыми данными.
 * Служит прослойкой между ViewModel и базой данных.
 */
class FinanceRepository {
    /**
     * Инициализирует базу данных при создании репозитория.
     */
    init {
        DatabaseInitializer.createTablesIfNotExist()
    }

    /**
     * Получает все операции (доходы и расходы) из базы данных.
     *
     * @return Отсортированный по дате список всех операций
     */
    fun getAllOperations(): List<Operation> {
        println("\n=== REPOSITORY: ПОЛУЧЕНИЕ ВСЕХ ОПЕРАЦИЙ ===")

        // Получаем операции доходов и преобразуем в модель Operation
        val income = GetIncomeTransactions.getAll().map { row ->
            Operation(
                id = row.id,
                type = "Доход",
                amount = row.amount,
                category = row.category,
                date = LocalDate.parse(row.date)
            )
        }

        // Получаем операции расходов и преобразуем в модель Operation
        val expenses = GetExpensesTransactions.getAll().map { row ->
            Operation(
                id = row.id,
                type = "Расход",
                amount = row.amount,
                category = row.category,
                date = LocalDate.parse(row.date)
            )
        }

        // Подробная отладка
        println("Доходы получены: ${income.size} операций")
        println("Расходы получены: ${expenses.size} операций")

        if (income.isNotEmpty()) {
            println("Последняя операция дохода: ${income.first()}")
        }
        if (expenses.isNotEmpty()) {
            println("Последняя операция расхода: ${expenses.first()}")
        }

        // Соединяем и сортируем по дате (сначала новые)
        val allOps = (income + expenses).sortedByDescending { it.date }
        println("Загружено операций: доходов=${income.size}, расходов=${expenses.size}, всего=${allOps.size}")
        println("===============================================\n")
        return allOps
    }

    /**
     * Получает операции за указанный временной период.
     *
     * @param from Начальная дата периода (включительно)
     * @param to Конечная дата периода (включительно)
     * @return Отфильтрованный список операций в указанном диапазоне дат
     */
    fun getOperations(from: LocalDate, to: LocalDate): List<Operation> {
        println("Получение операций с $from по $to")
        return getAllOperations().filter { it.date in from..to }
    }

    /**
     * Добавляет новую операцию в базу данных.
     *
     * @param op Операция для добавления
     * @return Добавленная операция с присвоенным ID
     * @throws IllegalArgumentException Если категория не найдена
     * @throws RuntimeException Если не удалось добавить операцию
     */
    fun addOperation(op: Operation): Operation {
        println("\n=== ДОБАВЛЕНИЕ ОПЕРАЦИИ ===")
        println("Операция: $op")
        println("Тип операции: ${op.type}")
        println("Категория: ${op.category}")
        println("Сумма: ${op.amount}")
        println("Дата: ${op.date}")
        println("================================\n")

        return if (op.type == "Доход") {
            // Получаем ID категории доходов
            val catId = GetIncomeCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория доходов '${op.category}' не найдена")

            // Добавляем транзакцию в базу данных
            val success = AddIncomeTransaction.addIncomeTransaction(
                name = null,
                sum = op.amount,
                categoryId = catId,
                date = op.date.toString()
            )

            if (!success) {
                throw RuntimeException("Не удалось добавить операцию дохода")
            }

            // Возвращаем операцию с ID, полученным из базы
            op.copy(id = GetIncomeTransactions.getLastId())
        } else {
            // Получаем ID категории расходов
            val catId = GetExpensesCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория расходов '${op.category}' не найдена")

            // Добавляем транзакцию в базу данных
            val success = AddExpensesTransaction.addExpensesTransaction(
                name = null,
                sum = op.amount,
                categoryId = catId,
                date = op.date.toString()
            )

            if (!success) {
                throw RuntimeException("Не удалось добавить операцию расхода")
            }

            // Возвращаем операцию с ID, полученным из базы
            op.copy(id = GetExpensesTransactions.getLastId())
        }
    }

    /**
     * Обновляет существующую операцию в базе данных.
     *
     * @param op Операция с обновленными данными (должен быть задан корректный id)
     * @throws RuntimeException Если операция не может быть обновлена
     */
    fun updateOperation(op: Operation) {
        println("Обновление операции: $op")
        val success = if (op.type == "Доход") {
            UpdateIncomeTransaction.update(
                transactionId = op.id,
                sum = op.amount,
                categoryName = op.category,
                date = op.date.toString()
            )
        } else {
            UpdateExpensesTransaction.update(
                transactionId = op.id,
                sum = op.amount,
                categoryName = op.category,
                date = op.date.toString()
            )
        }

        if (!success) {
            throw RuntimeException("Не удалось обновить операцию")
        }
    }

    /**
     * Удаляет операцию из базы данных по идентификатору и типу.
     *
     * @param id Уникальный идентификатор операции
     * @param type Тип операции ("Доход" или "Расход")
     * @throws RuntimeException Если операция не может быть удалена
     */
    fun deleteOperation(id: Int, type: String) {
        println("Удаление операции: id=$id, type=$type")
        val success = if (type == "Доход") {
            DeleteIncomeTransaction.deleteIncomeTransaction(id)
        } else {
            DeleteExpensesTransaction.deleteExpensesTransaction(id)
        }

        if (!success) {
            throw RuntimeException("Не удалось удалить операцию")
        }
    }

    /**
     * Получает список всех категорий доходов.
     *
     * @return Список названий категорий доходов
     */
    fun getIncomeCategories(): List<String> {
        println("Получение категорий доходов...")
        val categories = GetIncomeCategories.getAllNames()
        println("Категории доходов: $categories")
        return categories
    }

    /**
     * Получает список всех категорий расходов.
     *
     * @return Список названий категорий расходов
     */
    fun getExpenseCategories(): List<String> {
        println("Получение категорий расходов...")
        val categories = GetExpensesCategories.getAllNames()
        println("Категории расходов: $categories")
        return categories
    }

    /**
     * Добавляет новую категорию в базу данных.
     *
     * @param name Название новой категории
     * @param isIncome Тип категории (true - доходы, false - расходы)
     * @return true если категория успешно добавлена, false в случае ошибки
     */
    fun addCategory(name: String, isIncome: Boolean): Boolean {
        println("Добавление категории: name='$name', isIncome=$isIncome")
        return if (isIncome) {
            AddCategory.addIncomeCategory(name)
        } else {
            AddCategory.addExpensesCategory(name)
        }
    }

    /**
     * Удаляет категорию из базы данных.
     *
     * @param name Название категории для удаления
     * @param isIncome Тип категории (true - доходы, false - расходы)
     * @throws IllegalArgumentException Если категория не найдена
     * @throws RuntimeException Если категория не может быть удалена
     */
    fun deleteCategory(name: String, isIncome: Boolean) {
        println("Удаление категории: name='$name', isIncome=$isIncome")
        val id = if (isIncome) GetIncomeCategories.getIdByName(name)
        else GetExpensesCategories.getIdByName(name)

        if (id == null) {
            throw IllegalArgumentException("Категория '$name' не найдена")
        }

        val success = if (isIncome) {
            DeleteCategory.deleteIncomeCategory(id)
        } else {
            DeleteCategory.deleteExpensesCategory(id)
        }

        if (!success) {
            throw RuntimeException("Не удалось удалить категорию '$name'")
        }
    }
}
