package repository

import db.database_request.*
import models.Operation
import java.time.LocalDate

class FinanceRepository {
    init {
        DatabaseInitializer.createTablesIfNotExist()
    }

    fun getAllOperations(): List<Operation> {
        println("Получение всех операций...")
        val income = GetIncomeTransactions.getAll().map { row ->
            Operation(
                id = row.id,
                type = "Доход",
                amount = row.amount,
                category = row.category,
                date = LocalDate.parse(row.date)
            )
        }

        val expenses = GetExpensesTransactions.getAll().map { row ->
            Operation(
                id = row.id,
                type = "Расход",
                amount = row.amount,
                category = row.category,
                date = LocalDate.parse(row.date)
            )
        }

        val allOps = (income + expenses).sortedByDescending { it.date }
        println("Загружено операций: доходов=${income.size}, расходов=${expenses.size}, всего=${allOps.size}")
        return allOps
    }

    fun getOperations(from: LocalDate, to: LocalDate): List<Operation> {
        println("Получение операций с $from по $to")
        return getAllOperations().filter { it.date in from..to }
    }

    fun addOperation(op: Operation): Operation {
        println("Добавление операции: $op")
        return if (op.type == "Доход") {
            val catId = GetIncomeCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория доходов '${op.category}' не найдена")

            val success = AddIncomeTransaction.addIncomeTransaction(
                name = null,
                sum = op.amount,
                categoryId = catId,
                date = op.date.toString()
            )

            if (!success) {
                throw RuntimeException("Не удалось добавить операцию дохода")
            }

            op.copy(id = GetIncomeTransactions.getLastId())
        } else {
            val catId = GetExpensesCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория расходов '${op.category}' не найдена")

            val success = AddExpensesTransaction.addExpensesTransaction(
                name = null,
                sum = op.amount,
                categoryId = catId,
                date = op.date.toString()
            )

            if (!success) {
                throw RuntimeException("Не удалось добавить операцию расхода")
            }

            op.copy(id = GetExpensesTransactions.getLastId())
        }
    }

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

    fun getIncomeCategories(): List<String> {
        println("Получение категорий доходов...")
        val categories = GetIncomeCategories.getAllNames()
        println("Категории доходов: $categories")
        return categories
    }

    fun getExpenseCategories(): List<String> {
        println("Получение категорий расходов...")
        val categories = GetExpensesCategories.getAllNames()
        println("Категории расходов: $categories")
        return categories
    }

    fun addCategory(name: String, isIncome: Boolean): Boolean {
        println("Добавление категории: name='$name', isIncome=$isIncome")
        return if (isIncome) {
            AddCategory.addIncomeCategory(name)
        } else {
            AddCategory.addExpensesCategory(name)
        }
    }

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
