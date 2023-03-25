import com.sirdratuti.personalaccount.controller.UserAccountController
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.properties.Delegates

@Testcontainers
@SpringBootTest
@Transactional
class UserAccountControllerTest {
    @Autowired
    private lateinit var userAccountController: UserAccountController
    private var userId by Delegates.notNull<Long>()
    private val template = TestRestTemplate()

    @Container
    private var marketContainer = FixedHostPortGenericContainer(
        "marketContainer:1.0-SNAPSHOT"
    )
        .withFixedExposedPort(8080, 8080)
        .withExposedPorts(8080)

    @BeforeEach
    fun setup() {
        marketContainer.start()
        userId = userAccountController.addAccount(DEFAULT_BALANCE).id
        template.postForObject(
            "$HOST/add-company?id={id}&stocks={stocks}&price={price}",
            null, String::class.java, TEST_COMPANY_ID, DEFAULT_STOCK_AMOUNT, DEFAULT_STOCK_PRICE,
        )
    }

    @AfterEach
    fun clear() = marketContainer.stop()

    @Test
    fun `should correctly register user`() {
        val res = userAccountController.getAccount(userId)
        Assertions.assertEquals(DEFAULT_BALANCE, res.balance)
        Assertions.assertTrue(res.stocksMap.isEmpty())
    }

    @Test
    fun `should correctly add amount to user balance`() {
        val amountToAdd = 1000.0
        val res = userAccountController.addMoney(userId, amountToAdd)
        Assertions.assertEquals(DEFAULT_BALANCE + amountToAdd, res.balance)
        Assertions.assertTrue(res.stocksMap.isEmpty())
    }

    @Test
    fun `should correctly add stocks to account`() {
        val amountOfStocks = 10
        val res = userAccountController.buyStocks(userId, TEST_COMPANY_ID, amountOfStocks)
        Assertions.assertEquals(DEFAULT_BALANCE - DEFAULT_STOCK_PRICE * amountOfStocks, res.balance)
        Assertions.assertTrue(
            res.stocksMap.entries.count { (id, amount) ->
                id == TEST_COMPANY_ID && amount == amountOfStocks
            } == 1,
        )
        Assertions.assertEquals(DEFAULT_STOCK_PRICE * amountOfStocks, userAccountController.inStocks(userId))
    }

    @Test
    fun `should correctly spend stocks with different prices`() {
        val amountOfStocks = 10
        val newStockPrice = 50
        userAccountController.buyStocks(userId, TEST_COMPANY_ID, amountOfStocks)
        template.postForObject(
            "$HOST/update-price/{id}?price={price}",
            null, String::class.java, TEST_COMPANY_ID, newStockPrice,
        )
        val account = userAccountController.buyStocks(userId, TEST_COMPANY_ID, amountOfStocks)
        Assertions.assertEquals(
            DEFAULT_BALANCE - amountOfStocks * DEFAULT_STOCK_PRICE - amountOfStocks * newStockPrice,
            account.balance,
        )
        Assertions.assertEquals(
            amountOfStocks * 2 * newStockPrice,
            userAccountController.inStocks(userId)
        )
        Assertions.assertTrue(
            account.stocksMap.entries.count { (id, amount) ->
                id == TEST_COMPANY_ID && amount == amountOfStocks * 2
            } == 1,
        )
    }

    @Test
    fun `should correctly buy and sell stocks`() {
        val amountOfStocks = 10
        val newStockPrice = 50
        userAccountController.buyStocks(userId, TEST_COMPANY_ID, amountOfStocks)
        template.postForObject(
            "$HOST/update-price/{id}?price={price}",
            null, String::class.java, TEST_COMPANY_ID, newStockPrice,
        )
        val account = userAccountController.sellStocks(userId, TEST_COMPANY_ID, amountOfStocks)
        Assertions.assertEquals(
            DEFAULT_BALANCE - amountOfStocks * DEFAULT_STOCK_PRICE + amountOfStocks * newStockPrice,
            account.balance,
        )
        Assertions.assertEquals(
            0.0,
            userAccountController.inStocks(userId),
        )
        Assertions.assertTrue(
            account.stocksMap.entries.count { (id, amount) ->
                id == TEST_COMPANY_ID && amount == 0
            } == 1,
        )
    }

    private companion object {
        private const val DEFAULT_BALANCE = 10000.0
        private const val DEFAULT_STOCK_PRICE = 500.0
        private const val DEFAULT_STOCK_AMOUNT = 100

        private const val TEST_COMPANY_ID = 124L
        private const val HOST = "http://localhost:8080/stock-market"
    }
}