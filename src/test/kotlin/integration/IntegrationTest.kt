package integration

import models.Operation
import org.junit.jupiter.api.*
import repository.FinanceRepository
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTest {

    private lateinit var repository: FinanceRepository

    @BeforeAll
    fun setup() {
        repository = FinanceRepository()
    }

    @BeforeEach
    fun cleanData() {
        val operations = repository.getAllOperations()
        operations.forEach {
            try { repository.deleteOperation(it.id, it.type) } catch (_: Exception) {}
        }
    }

    @Test
    fun `test 1 - full operation lifecycle`() {
        val operation = Operation(0, "Расход", 1000.0, "Еда", LocalDate.of(2026, 4, 1), "Обед")
        val created = repository.addOperation(operation)
        assertTrue(created.id > 0)

        val operations = repository.getAllOperations()
        val found = operations.find { it.id == created.id }
        assertTrue(found != null)

        val updated = found.copy(amount = 1500.0, name = "Обед большой")
        repository.updateOperation(updated)

        val updatedOps = repository.getAllOperations()
        val foundUpdated = updatedOps.find { it.id == updated.id }
        assertEquals(1500.0, foundUpdated?.amount)

        repository.deleteOperation(updated.id, updated.type)
        val finalOps = repository.getAllOperations()
        assertTrue(finalOps.none { it.id == updated.id })
    }


    @Test
    fun `test 2 - statistics calculation`() {
        repository.addOperation(Operation(0, "Доход", 50000.0, "Зарплата", LocalDate.of(2026, 4, 1), "ЗП"))
        repository.addOperation(Operation(0, "Доход", 10000.0, "Фриланс", LocalDate.of(2026, 4, 5), "Проект"))
        repository.addOperation(Operation(0, "Расход", 15000.0, "Еда", LocalDate.of(2026, 4, 10), "Продукты"))
        repository.addOperation(Operation(0, "Расход", 5000.0, "Транспорт", LocalDate.of(2026, 4, 15), "Такси"))

        val operations = repository.getAllOperations()
        val totalIncome = operations.filter { it.type == "Доход" }.sumOf { it.amount }
        val totalExpenses = operations.filter { it.type == "Расход" }.sumOf { it.amount }
        val balance = totalIncome - totalExpenses

        assertEquals(60000.0, totalIncome)
        assertEquals(20000.0, totalExpenses)
        assertEquals(40000.0, balance)
    }

    @Test
    fun `test 3 - period filtering`() {
        val date1 = LocalDate.of(2026, 3, 15)
        val date2 = LocalDate.of(2026, 4, 1)
        val date3 = LocalDate.of(2026, 4, 15)
        val date4 = LocalDate.of(2026, 5, 1)

        repository.addOperation(Operation(0, "Расход", 100.0, "Еда", date1, "Март"))
        repository.addOperation(Operation(0, "Расход", 200.0, "Еда", date2, "Апрель начало"))
        repository.addOperation(Operation(0, "Расход", 300.0, "Еда", date3, "Апрель середина"))
        repository.addOperation(Operation(0, "Расход", 400.0, "Еда", date4, "Май"))

        val aprilOps = repository.getOperations(LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30))
        assertEquals(2, aprilOps.size)
        assertTrue(aprilOps.all { it.date.month.value == 4 })
        assertEquals(500.0, aprilOps.sumOf { it.amount })
    }

    @Test
    fun `test 4 - default categories initialization`() {
        val incomeCats = repository.getIncomeCategories().map { it.trim() }
        val expenseCats = repository.getExpenseCategories().map { it.trim() }

        assertTrue(incomeCats.contains("Зарплата"))
        assertTrue(incomeCats.contains("Стипендия"))
        assertTrue(expenseCats.contains("Еда"))
        assertTrue(expenseCats.contains("Транспорт"))
    }

    @Test
    fun `test 5 - data integrity after multiple operations`() {
        val expenseCategory = repository.getExpenseCategories().find { it.trim() == "Еда" } ?: "Еда"

        repeat(10) { i ->
            repository.addOperation(Operation(
                id = 0,
                type = if (i % 2 == 0) "Доход" else "Расход",
                amount = (i + 1) * 100.0,
                category = if (i % 2 == 0) "Зарплата" else expenseCategory,
                date = LocalDate.of(2026, 4, i + 1),
                name = "Операция $i"
            ))
        }

        val operations = repository.getAllOperations()
        assertEquals(10, operations.size)

        val totalIncome = operations.filter { it.type == "Доход" }.sumOf { it.amount }
        val totalExpenses = operations.filter { it.type == "Расход" }.sumOf { it.amount }

        assertEquals(2500.0, totalIncome)
        assertEquals(3000.0, totalExpenses)
    }
}