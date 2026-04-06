package repository

import db.database_request.*
import models.Operation
import org.junit.jupiter.api.*
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinanceRepositoryTest {

    private lateinit var repository: FinanceRepository
    private val testDbPath = "test_finlytics.db"

    @BeforeAll
    fun setup() {
        // Удаляем тестовую БД если существует
        File(testDbPath).delete()
        // Создаем репозиторий (он автоматически создаст таблицы)
        repository = FinanceRepository()
    }

    @AfterAll
    fun tearDown() {
        // Очищаем тестовую БД после всех тестов
        File(testDbPath).delete()
    }

    @BeforeEach
    fun cleanDatabase() {
        // Очищаем все таблицы перед каждым тестом
        cleanAllTables()
    }

    private fun cleanAllTables() {
        // Удаляем все транзакции доходов
        GetIncomeTransactions.getAll().forEach {
            DeleteIncomeTransaction.deleteIncomeTransaction(it.id)
        }
        // Удаляем все транзакции расходов
        GetExpensesTransactions.getAll().forEach {
            DeleteExpensesTransaction.deleteExpensesTransaction(it.id)
        }
        // Удаляем кастомные категории доходов (оставляем стандартные)
        GetIncomeCategories.getAll().forEach { cat ->
            if (cat.second !in listOf("Зарплата", "Стипендия", "Фриланс", "Инвестиции", "Подарок")) {
                DeleteCategory.deleteIncomeCategory(cat.first)
            }
        }
        // Удаляем кастомные категории расходов (оставляем стандартные)
        GetExpensesCategories.getAll().forEach { cat ->
            if (cat.second.trim() !in listOf("Еда", "Транспорт", "Жилье", "Развлечения", "Образование", "Здоровье", "Одежда", "Коммунальные услуги")) {
                DeleteCategory.deleteExpensesCategory(cat.first)
            }
        }
    }

    @Test
    fun `test 1 - add income operation with default category`() {
        val operation = Operation(
            id = 0, type = "Доход", amount = 50000.0,
            category = "Зарплата", date = LocalDate.of(2026, 4, 1), name = "ЗП за апрель"
        )
        val result = repository.addOperation(operation)
        assertEquals("Доход", result.type)
        assertEquals(50000.0, result.amount)
        assertTrue(result.id > 0, "ID операции должен быть больше 0 после сохранения")
    }

    @Test
    fun `test 2 - add another income operation`() {
        val operation = Operation(
            id = 0, type = "Доход", amount = 15000.0,
            category = "Стипендия", date = LocalDate.of(2026, 4, 5), name = "Стипендия"
        )
        val result = repository.addOperation(operation)
        assertEquals("Доход", result.type)
        assertEquals("Стипендия", result.category)
        assertTrue(result.id > 0)
    }

    @Test
    fun `test 3 - get all income operations`() {
        repository.addOperation(Operation(0, "Доход", 1000.0, "Зарплата", LocalDate.now(), "Тест1"))
        repository.addOperation(Operation(0, "Доход", 2000.0, "Фриланс", LocalDate.now(), "Тест2"))

        val operations = repository.getAllOperations()
        val incomeOps = operations.filter { it.type == "Доход" }

        assertTrue(incomeOps.size >= 2, "Должны быть как минимум 2 операции дохода")
        assertTrue(incomeOps.any { it.category == "Зарплата" })
        assertTrue(incomeOps.any { it.category == "Фриланс" })
    }

    @Test
    fun `test 4 - update income operation`() {
        val op = repository.addOperation(Operation(0, "Доход", 1000.0, "Зарплата", LocalDate.now(), "Премия"))
        val updatedOp = op.copy(amount = 2500.0, name = "Премия большая")
        repository.updateOperation(updatedOp)
        val allOps = repository.getAllOperations()
        val found = allOps.find { it.id == op.id }
        assertTrue(found != null)
        assertEquals(2500.0, found?.amount)
        assertEquals("Премия большая", found?.name)
    }

    @Test
    fun `test 5 - delete income operation`() {
        val op = repository.addOperation(Operation(0, "Доход", 999.0, "Стипендия", LocalDate.now(), "Удали меня"))
        val opId = op.id
        repository.deleteOperation(opId, "Доход")
        val allOps = repository.getAllOperations()
        val found = allOps.find { it.id == opId }
        assertTrue(found == null, "Операция должна быть удалена")
    }

    @Test
    fun `test 6 - add custom income category`() {
        val success = repository.addCategory("Бонус", true) // true = доход
        assertTrue(success, "Категория должна успешно добавиться")
        val categories = repository.getIncomeCategories()
        assertTrue(categories.contains("Бонус"), "Новая категория должна появиться в списке")
    }

    @Test
    fun `test 7 - can delete unused income category`() {
        repository.addCategory("Временная", true)
        repository.deleteCategory("Временная", true)
        val categories = repository.getIncomeCategories()
        assertFalse(categories.contains("Временная"), "Удаленная категория не должна быть в списке")
    }

    @Test
    fun `test 8 - get operations by period (income only)`() {
        val date1 = LocalDate.of(2026, 4, 1)
        val date2 = LocalDate.of(2026, 4, 15)
        val date3 = LocalDate.of(2026, 5, 1)

        repository.addOperation(Operation(0, "Доход", 1000.0, "Зарплата", date1, "Апрель"))
        repository.addOperation(Operation(0, "Доход", 2000.0, "Зарплата", date2, "Апрель середина"))
        repository.addOperation(Operation(0, "Доход", 3000.0, "Зарплата", date3, "Май"))

        val operations = repository.getOperations(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))

        assertEquals(2, operations.size, "Должно быть 2 операции за апрель")
        assertTrue(operations.all { it.date.month.value == 4 })
        assertTrue(operations.all { it.type == "Доход" })
    }

    @Test
    fun `test 9 - operations sorted by date descending (income)`() {
        val date1 = LocalDate.of(2026, 4, 1)
        val date2 = LocalDate.of(2026, 4, 10)
        val date3 = LocalDate.of(2026, 4, 20)

        repository.addOperation(Operation(0, "Доход", 100.0, "Зарплата", date1, "Первый"))
        repository.addOperation(Operation(0, "Доход", 200.0, "Зарплата", date2, "Второй"))
        repository.addOperation(Operation(0, "Доход", 300.0, "Зарплата", date3, "Третий"))

        val operations = repository.getAllOperations().filter { it.type == "Доход" }

        assertTrue(operations.size >= 3, "Должно быть как минимум 3 операции")
        assertTrue(operations[0].date >= operations[1].date, "Операции должны быть отсортированы по убыванию даты")
    }

    @Test
    fun `test 10 - default income categories exist`() {
        val incomeCats = repository.getIncomeCategories()

        assertTrue(incomeCats.contains("Зарплата"), "Должна быть категория 'Зарплата'")
        assertTrue(incomeCats.contains("Стипендия"), "Должна быть категория 'Стипендия'")
        assertTrue(incomeCats.contains("Фриланс"), "Должна быть категория 'Фриланс'")
        assertTrue(incomeCats.contains("Инвестиции"), "Должна быть категория 'Инвестиции'")
        assertTrue(incomeCats.contains("Подарок"), "Должна быть категория 'Подарок'")
    }

    @Test
    fun `test 11 - cannot add empty income category`() {
        val success = repository.addCategory("", true)
        assertFalse(success, "Пустая категория не должна добавиться")

        val success2 = repository.addCategory("   ", true)
        assertFalse(success2, "Категория с пробелами не должна добавиться")
    }

    @Test
    fun `test 12 - income categories are unique`() {
        repository.addCategory("Уникальная", true)
        val success = repository.addCategory("Уникальная", true)
        // Вторая попытка добавить ту же категорию должна вернуть false или не изменить список
        val categories = repository.getIncomeCategories()
        val count = categories.count { it == "Уникальная" }
        assertEquals(1, count, "Категория должна быть только одна")
    }
}