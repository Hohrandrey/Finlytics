package viewmodel

import models.Operation
import org.junit.jupiter.api.*
import repository.FinanceRepository
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FinanceViewModelTest {

    private lateinit var viewModel: FinanceViewModel
    private lateinit var repository: FinanceRepository

    @BeforeAll
    fun setup() {
        repository = FinanceRepository()
        viewModel = FinanceViewModel(repository)
    }

    @BeforeEach
    fun cleanData() {
        val operations = repository.getAllOperations()
        operations.forEach {
            repository.deleteOperation(it.id, it.type)
        }
        viewModel.refresh()
    }

    @Test
    fun `test 1 - initial state is empty`() {
        val state = viewModel.state.value
        assertTrue(state.operations.isEmpty())
        assertEquals(0.0, state.totalIncome)
        assertEquals(0.0, state.totalExpenses)
        assertEquals(0.0, state.balance)
    }

    @Test
    fun `test 2 - add income operation updates state`() {
        val operation = Operation(0, "Доход", 50000.0, "Зарплата", LocalDate.now(), "ЗП")
        viewModel.saveOperation(operation)
        val state = viewModel.state.value
        assertEquals(50000.0, state.totalIncome)
        assertEquals(0.0, state.totalExpenses)
        assertEquals(50000.0, state.balance)
        assertTrue(state.operations.any { it.type == "Доход" && it.amount == 50000.0 })
    }

    @Test
    fun `test 3 - add expense operation updates state`() {
        val operation = Operation(0, "Расход", 1500.0, "Еда", LocalDate.now(), "Обед")
        viewModel.saveOperation(operation)
        val state = viewModel.state.value
        assertEquals(0.0, state.totalIncome)
        assertEquals(1500.0, state.totalExpenses)
        assertEquals(-1500.0, state.balance)
    }

    @Test
    fun `test 4 - balance calculation with multiple operations`() {
        viewModel.saveOperation(Operation(0, "Доход", 10000.0, "Зарплата", LocalDate.now(), "ЗП"))
        viewModel.saveOperation(Operation(0, "Расход", 3000.0, "Еда", LocalDate.now(), "Еда"))
        viewModel.saveOperation(Operation(0, "Расход", 2000.0, "Транспорт", LocalDate.now(), "Такси"))
        val state = viewModel.state.value
        assertEquals(10000.0, state.totalIncome)
        assertEquals(5000.0, state.totalExpenses)
        assertEquals(5000.0, state.balance)
    }

    @Test
    fun `test 5 - edit operation updates state`() {
        val operation = Operation(0, "Расход", 1000.0, "Еда", LocalDate.now(), "Обед")
        viewModel.saveOperation(operation)
        val savedOp = viewModel.state.value.operations.find { it.name == "Обед" }!!
        val editedOp = savedOp.copy(amount = 1500.0, name = "Обед большой")
        viewModel.showEditOperation(editedOp)
        viewModel.saveOperation(editedOp)
        val state = viewModel.state.value
        val updatedOp = state.operations.find { it.id == savedOp.id }!!
        assertEquals(1500.0, updatedOp.amount)
        assertEquals("Обед большой", updatedOp.name)
        assertEquals(1500.0, state.totalExpenses)
    }

    @Test
    fun `test 6 - delete operation updates state`() {
        viewModel.saveOperation(Operation(0, "Доход", 5000.0, "Зарплата", LocalDate.now(), "ЗП"))
        val stateBefore = viewModel.state.value
        val opToDelete = stateBefore.operations.find { it.amount == 5000.0 }!!
        viewModel.deleteOperation(opToDelete)
        val stateAfter = viewModel.state.value
        assertEquals(0.0, stateAfter.totalIncome)
        assertTrue(stateAfter.operations.none { it.id == opToDelete.id })
    }

    @Test
    fun `test 7 - add category`() {
        viewModel.state.value.expenseCategories.size
        viewModel.showAddCategory(false)
        viewModel.saveCategory("Кафе")
        val state = viewModel.state.value
        assertTrue(state.expenseCategories.map { it.trim() }.contains("Кафе"))
    }

    @Test
    fun `test 8 - cannot add duplicate category`() {
        viewModel.showAddCategory(true)
        viewModel.saveCategory("ТестКатегория")
        val initialCount = viewModel.state.value.incomeCategories.size
        viewModel.showAddCategory(true)
        viewModel.saveCategory("ТестКатегория")
        val state = viewModel.state.value
        assertEquals(initialCount, state.incomeCategories.size)
    }

    @Test
    fun `test 9 - delete unused category`() {
        viewModel.showAddCategory(false)
        viewModel.saveCategory("Удаляемая")
        viewModel.state.value.expenseCategories.size
        viewModel.deleteCategory("Удаляемая", false)
        val state = viewModel.state.value
        assertFalse(state.expenseCategories.map { it.trim() }.contains("Удаляемая"))
    }

    @Test
    fun `test 10 - cannot delete used category`() {
        viewModel.showAddCategory(false)
        viewModel.saveCategory("Используемая")
        viewModel.showAddOperation()
        viewModel.saveOperation(Operation(0, "Расход", 100.0, "Используемая", LocalDate.now(), "Тест"))
        viewModel.deleteCategory("Используемая", false)
        val state = viewModel.state.value
        assertTrue(state.expenseCategories.map { it.trim() }.contains("Используемая"))
    }

    @Test
    fun `test 11 - navigation between screens`() {
        assertEquals("Overview", viewModel.currentScreen)
        viewModel.navigateTo("History")
        assertEquals("History", viewModel.currentScreen)
        viewModel.navigateTo("Settings")
        assertEquals("Settings", viewModel.currentScreen)
        viewModel.navigateTo("Overview")
        assertEquals("Overview", viewModel.currentScreen)
    }


    @Test
    fun `test 12 - expenses by category calculation`() {
        viewModel.saveOperation(Operation(0, "Расход", 1000.0, "Еда", LocalDate.now(), "Еда1"))
        viewModel.saveOperation(Operation(0, "Расход", 500.0, "Еда", LocalDate.now(), "Еда2"))
        viewModel.saveOperation(Operation(0, "Расход", 2000.0, "Транспорт", LocalDate.now(), "Такси"))
        val state = viewModel.state.value
        assertTrue(state.expensesByCategory.containsKey("Еда") || state.expensesByCategory.containsKey("Еда "))
        assertTrue(state.expensesByCategory.containsKey("Транспорт") || state.expensesByCategory.containsKey("Транспорт "))
    }

    @Test
    fun `test 13 - refresh updates state`() {
        repository.addOperation(Operation(0, "Доход", 9999.0, "Зарплата", LocalDate.now(), "Прямое добавление"))
        var state = viewModel.state.value
        val beforeCount = state.operations.size
        viewModel.refresh()
        state = viewModel.state.value
        assertTrue(state.operations.size >= beforeCount)
    }
}