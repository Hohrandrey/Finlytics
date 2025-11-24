package repository

import db.database_request.*
import models.Operation
import java.time.LocalDate

class FinanceRepository {
    init {
        DatabaseInitializer.createTablesIfNotExist()
    }

    fun getAllOperations(): List<Operation> {
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

        return (income + expenses).sortedByDescending { it.date }
    }

    fun getOperations(from: LocalDate, to: LocalDate): List<Operation> =
        getAllOperations().filter { it.date in from..to }

    fun addOperation(op: Operation): Operation {
        return if (op.type == "Доход") {
            val catId = GetIncomeCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория доходов '${op.category}' не найдена")
            AddIncomeTransaction.addIncomeTransaction(null, op.amount, catId, op.date.toString())
            op.copy(id = GetIncomeTransactions.getLastId())
        } else {
            val catId = GetExpensesCategories.getIdByName(op.category)
                ?: throw IllegalArgumentException("Категория расходов '${op.category}' не найдена")
            AddExpensesTransaction.addExpensesTransaction(null, op.amount, catId, op.date.toString())
            op.copy(id = GetExpensesTransactions.getLastId())
        }
    }

    fun updateOperation(op: Operation) {
        if (op.type == "Доход") {
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
    }

    fun deleteOperation(id: Int, type: String) {
        if (type == "Доход") {
            DeleteIncomeTransaction.deleteIncomeTransaction(id)
        } else {
            DeleteExpensesTransaction.deleteExpensesTransaction(id)
        }
    }

    // Категории
    fun getIncomeCategories(): List<String> = GetIncomeCategories.getAllNames()
    fun getExpenseCategories(): List<String> = GetExpensesCategories.getAllNames()

    fun addCategory(name: String, isIncome: Boolean) {
        if (isIncome) AddCategory.addIncomeCategory(name)
        else AddCategory.addExpensesCategory(name)
    }

    fun deleteCategory(name: String, isIncome: Boolean) {
        val id = if (isIncome) GetIncomeCategories.getIdByName(name)
        else GetExpensesCategories.getIdByName(name)
        if (id != null) {
            if (isIncome) DeleteCategory.deleteIncomeCategory(id)
            else DeleteCategory.deleteExpensesCategory(id)
        }
    }
}