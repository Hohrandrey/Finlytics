package db.database_request

import org.junit.jupiter.api.*
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseCRUDTest {

    private val testDbPath = "test_crud.db"

    @BeforeAll
    fun setup() {
        File(testDbPath).delete()
        DatabaseInitializer.createTablesIfNotExist()
    }

    @AfterAll
    fun tearDown() {
        File(testDbPath).delete()
    }

    @BeforeEach
    fun cleanTables() {
        // Очищаем все таблицы
        cleanIncomeTransactions()
        cleanExpenseTransactions()
        cleanCategories()

        // Добавляем стандартные категории
        AddCategory.addIncomeCategory("Зарплата")
        AddCategory.addIncomeCategory("Стипендия")
        AddCategory.addExpensesCategory("Еда")
        AddCategory.addExpensesCategory("Транспорт")
    }

    private fun cleanIncomeTransactions() {
        GetIncomeTransactions.getAll().forEach {
            DeleteIncomeTransaction.deleteIncomeTransaction(it.id)
        }
    }

    private fun cleanExpenseTransactions() {
        GetExpensesTransactions.getAll().forEach {
            DeleteExpensesTransaction.deleteExpensesTransaction(it.id)
        }
    }

    private fun cleanCategories() {
        GetIncomeCategories.getAll().forEach { cat ->
            if (cat.second !in listOf("Зарплата", "Стипендия")) {
                DeleteCategory.deleteIncomeCategory(cat.first)
            }
        }
        GetExpensesCategories.getAll().forEach { cat ->
            if (cat.second !in listOf("Еда", "Транспорт")) {
                DeleteCategory.deleteExpensesCategory(cat.first)
            }
        }
    }

    // ============ Тесты для категорий доходов ============

    @Test
    fun `test 1 - add income category`() {
        val result = AddCategory.addIncomeCategory("Фриланс")
        assertTrue(result, "Категория должна добавиться")

        val categories = GetIncomeCategories.getAllNames()
        assertTrue(categories.contains("Фриланс"))
    }

    @Test
    fun `test 2 - cannot add empty income category`() {
        val result = AddCategory.addIncomeCategory("")
        assertFalse(result, "Пустая категория не должна добавиться")

        val result2 = AddCategory.addIncomeCategory("   ")
        assertFalse(result2, "Категория с пробелами не должна добавиться")
    }

    @Test
    fun `test 3 - get income categories`() {
        AddCategory.addIncomeCategory("Инвестиции")
        AddCategory.addIncomeCategory("Подарок")

        val categories = GetIncomeCategories.getAll()

        assertTrue(categories.size >= 2)
        assertTrue(categories.any { it.second == "Инвестиции" })
        assertTrue(categories.any { it.second == "Подарок" })
    }

    @Test
    fun `test 4 - get income category by name`() {
        val id = GetIncomeCategories.getIdByName("Зарплата")

        assertTrue(id != null && id > 0, "ID категории 'Зарплата' должен быть найден")
    }

    @Test
    fun `test 5 - get non-existent category returns null`() {
        val id = GetIncomeCategories.getIdByName("Несуществующая")
        assertNull(id, "ID несуществующей категории должен быть null")
    }

    @Test
    fun `test 6 - delete income category`() {
        AddCategory.addIncomeCategory("Удаляемая")
        val category = GetIncomeCategories.getAll().find { it.second == "Удаляемая" }!!

        val result = DeleteCategory.deleteIncomeCategory(category.first)

        assertTrue(result, "Категория должна удалиться")
        assertNull(GetIncomeCategories.getIdByName("Удаляемая"))
    }

    // ============ Тесты для категорий расходов ============

    @Test
    fun `test 7 - add expense category`() {
        val result = AddCategory.addExpensesCategory("Развлечения")
        assertTrue(result)

        val categories = GetExpensesCategories.getAllNames()
        assertTrue(categories.contains("Развлечения"))
    }

    @Test
    fun `test 8 - cannot add empty expense category`() {
        assertFalse(AddCategory.addExpensesCategory(""))
        assertFalse(AddCategory.addExpensesCategory("   "))
    }

    @Test
    fun `test 9 - get expense categories`() {
        AddCategory.addExpensesCategory("Жилье")
        AddCategory.addExpensesCategory("Здоровье")

        val categories = GetExpensesCategories.getAll()

        assertTrue(categories.any { it.second == "Жилье" })
        assertTrue(categories.any { it.second == "Здоровье" })
    }

    @Test
    fun `test 10 - delete expense category`() {
        AddCategory.addExpensesCategory("УдаляемаяРасход")
        val category = GetExpensesCategories.getAll().find { it.second == "УдаляемаяРасход" }!!

        val result = DeleteCategory.deleteExpensesCategory(category.first)

        assertTrue(result)
        assertNull(GetExpensesCategories.getIdByName("УдаляемаяРасход"))
    }

    // ============ Тесты для транзакций доходов ============

    @Test
    fun `test 11 - add income transaction`() {
        val categoryId = GetIncomeCategories.getIdByName("Зарплата")!!
        val date = LocalDate.of(2026, 4, 1).toString()

        val result = AddIncomeTransaction.addIncomeTransaction(
            name = "ЗП за апрель",
            sum = 50000.0,
            categoryId = categoryId,
            date = date
        )

        assertTrue(result, "Транзакция должна добавиться")

        val transactions = GetIncomeTransactions.getAll()
        assertTrue(transactions.any { it.name == "ЗП за апрель" && it.amount == 50000.0 })
    }

    @Test
    fun `test 12 - cannot add income transaction with invalid sum`() {
        val categoryId = GetIncomeCategories.getIdByName("Зарплата")!!

        assertFalse(AddIncomeTransaction.addIncomeTransaction("Тест", -100.0, categoryId, "2026-04-01"))
        assertFalse(AddIncomeTransaction.addIncomeTransaction("Тест", 0.0, categoryId, "2026-04-01"))
    }

    @Test
    fun `test 13 - cannot add income transaction with invalid category`() {
        assertFalse(AddIncomeTransaction.addIncomeTransaction("Тест", 1000.0, -1, "2026-04-01"))
        assertFalse(AddIncomeTransaction.addIncomeTransaction("Тест", 1000.0, 0, "2026-04-01"))
    }

    @Test
    fun `test 14 - update income transaction`() {
        val categoryId = GetIncomeCategories.getIdByName("Зарплата")!!
        val date = LocalDate.of(2026, 4, 1).toString()

        // Добавляем
        AddIncomeTransaction.addIncomeTransaction("Старая", 1000.0, categoryId, date)
        val transaction = GetIncomeTransactions.getAll().find { it.name == "Старая" }!!

        // Обновляем
        val result = UpdateIncomeTransaction.update(
            transactionId = transaction.id,
            name = "Новая",
            sum = 2000.0,
            categoryName = "Зарплата",
            date = date
        )

        assertTrue(result)

        val updated = GetIncomeTransactions.getAll().find { it.id == transaction.id }!!
        assertEquals("Новая", updated.name)
        assertEquals(2000.0, updated.amount)
    }

    @Test
    fun `test 15 - delete income transaction`() {
        val categoryId = GetIncomeCategories.getIdByName("Стипендия")!!

        AddIncomeTransaction.addIncomeTransaction("Удаляемая", 5000.0, categoryId, "2026-04-01")
        val transaction = GetIncomeTransactions.getAll().find { it.name == "Удаляемая" }!!

        val result = DeleteIncomeTransaction.deleteIncomeTransaction(transaction.id)

        assertTrue(result)
        assertTrue(GetIncomeTransactions.getAll().none { it.id == transaction.id })
    }

    @Test
    fun `test 16 - get last income transaction id`() {
        val categoryId = GetIncomeCategories.getIdByName("Зарплата")!!

        AddIncomeTransaction.addIncomeTransaction("Первая", 1000.0, categoryId, "2026-04-01")
        AddIncomeTransaction.addIncomeTransaction("Вторая", 2000.0, categoryId, "2026-04-02")

        val lastId = GetIncomeTransactions.getLastId()
        val allTransactions = GetIncomeTransactions.getAll()
        val maxId = allTransactions.maxOf { it.id }

        assertEquals(maxId, lastId)
    }

    // ============ Тесты для транзакций расходов ============

    @Test
    fun `test 17 - add expense transaction`() {
        val categoryId = GetExpensesCategories.getIdByName("Еда")!!

        val result = AddExpensesTransaction.addExpensesTransaction(
            name = "Обед",
            sum = 500.0,
            categoryId = categoryId,
            date = "2026-04-01"
        )

        assertTrue(result)

        val transactions = GetExpensesTransactions.getAll()
        assertTrue(transactions.any { it.name == "Обед" && it.amount == 500.0 })
    }

    @Test
    fun `test 18 - cannot add expense transaction with invalid data`() {
        val categoryId = GetExpensesCategories.getIdByName("Еда")!!

        assertFalse(AddExpensesTransaction.addExpensesTransaction("Тест", -100.0, categoryId, "2026-04-01"))
        assertFalse(AddExpensesTransaction.addExpensesTransaction("Тест", 0.0, categoryId, "2026-04-01"))
        assertFalse(AddExpensesTransaction.addExpensesTransaction("Тест", 100.0, -1, "2026-04-01"))
    }

    @Test
    fun `test 19 - update expense transaction`() {
        val categoryId = GetExpensesCategories.getIdByName("Транспорт")!!

        AddExpensesTransaction.addExpensesTransaction("Такси", 200.0, categoryId, "2026-04-01")
        val transaction = GetExpensesTransactions.getAll().find { it.name == "Такси" }!!

        val result = UpdateExpensesTransaction.update(
            transactionId = transaction.id,
            name = "Такси дорого",
            sum = 500.0,
            categoryName = "Транспорт",
            date = "2026-04-01"
        )

        assertTrue(result)

        val updated = GetExpensesTransactions.getAll().find { it.id == transaction.id }!!
        assertEquals("Такси дорого", updated.name)
        assertEquals(500.0, updated.amount)
    }

    @Test
    fun `test 20 - delete expense transaction`() {
        val categoryId = GetExpensesCategories.getIdByName("Еда")!!

        AddExpensesTransaction.addExpensesTransaction("Ужин", 800.0, categoryId, "2026-04-01")
        val transaction = GetExpensesTransactions.getAll().find { it.name == "Ужин" }!!

        val result = DeleteExpensesTransaction.deleteExpensesTransaction(transaction.id)

        assertTrue(result)
        assertTrue(GetExpensesTransactions.getAll().none { it.id == transaction.id })
    }

    @Test
    fun `test 21 - transactions sorted by date descending`() {
        val categoryId = GetExpensesCategories.getIdByName("Еда")!!

        AddExpensesTransaction.addExpensesTransaction("Первый", 100.0, categoryId, "2026-04-01")
        AddExpensesTransaction.addExpensesTransaction("Второй", 200.0, categoryId, "2026-04-10")
        AddExpensesTransaction.addExpensesTransaction("Третий", 300.0, categoryId, "2026-04-20")

        val transactions = GetExpensesTransactions.getAll()

        assertTrue(transactions[0].date >= transactions[1].date)
        assertTrue(transactions[1].date >= transactions[2].date)
    }

    @Test
    fun `test 22 - transaction with null name`() {
        val categoryId = GetIncomeCategories.getIdByName("Зарплата")!!

        val result = AddIncomeTransaction.addIncomeTransaction(
            name = null,
            sum = 1000.0,
            categoryId = categoryId,
            date = "2026-04-01"
        )

        assertTrue(result)

        val transaction = GetIncomeTransactions.getAll().find { it.amount == 1000.0 }!!
        assertNull(transaction.name)
    }

    @Test
    fun `test 23 - update with null name`() {
        val categoryId = GetExpensesCategories.getIdByName("Еда")!!

        AddExpensesTransaction.addExpensesTransaction("Старое имя", 100.0, categoryId, "2026-04-01")
        val transaction = GetExpensesTransactions.getAll().find { it.name == "Старое имя" }!!

        val result = UpdateExpensesTransaction.update(
            transactionId = transaction.id,
            name = null,
            sum = 200.0,
            categoryName = "Еда",
            date = "2026-04-01"
        )

        assertTrue(result)

        val updated = GetExpensesTransactions.getAll().find { it.id == transaction.id }!!
        assertNull(updated.name)
        assertEquals(200.0, updated.amount)
    }
}