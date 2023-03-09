package no.sb1.open.api.example.rest

import no.sb1.open.api.example.adapters.SB1ApiAdapter
import no.sb1.open.api.example.adapters.TokenStore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val sB1ApiAdapter: SB1ApiAdapter
) {
    @GetMapping("/")
    fun status(
        @RequestParam(value = "status", defaultValue = "false") status: String,
        @RequestParam(value = "code") authentication_code: String
    ): String {
        val tokenStore = TokenStore()
        val clientId = tokenStore.clientId
        val clientSecret = tokenStore.clientSecret

        sB1ApiAdapter.requestOauthToken(clientId, clientSecret, authentication_code, status)

        return """koden er: $authentication_code <br> Og status: $status $clientId """
    }

    @GetMapping("/hei")
    fun Hello() = "hei" + sB1ApiAdapter.hello()

    @GetMapping("/accounts")
    fun Accounts() = sB1ApiAdapter.accounts()

    @GetMapping("/transactions")
    fun Transactions(
        @RequestParam(value = "key") key: String,
    ) = sB1ApiAdapter.transactions(key)
}