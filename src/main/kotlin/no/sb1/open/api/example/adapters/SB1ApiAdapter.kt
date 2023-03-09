package no.sb1.open.api.example.adapters

import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URLEncoder
import java.time.Instant
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestTemplate

@Component
class SB1ApiAdapter(private val restTemplate: RestTemplate) {
    private val tokenStore = TokenStore()
    private val logger = LoggerFactory.getLogger(SB1ApiAdapter::class.java)
    private val apiAuthBaseURI = "https://api-auth.sparebank1.no"
    private val apiBaseURI = "https://api.sparebank1.no"

    private fun apiRequest(path: String): RequestEntity<Void> {
        val properties = tokenStore.loadTokens()
        return RequestEntity.get(URI.create("$apiBaseURI$path"))
            .headers(HttpHeaders().apply {
                this.contentType = MediaType.valueOf("application/vnd.sparebank1.v1+json; charset=utf-8")
                this.setBearerAuth(properties.accessToken)
            }).build()
    }

    private fun loadOauthToken(
        headers: HttpHeaders,
        requestBody: MultiValueMap<String, String>
    ) {
        val request = RequestEntity
            .post(URI.create("$apiAuthBaseURI/oauth/token"))
            .headers(headers)
            .body(requestBody)

        val response = restTemplate.exchange(request, AccessTokenDTO::class.java).body!!
        tokenStore.saveTokens(
            accessToken = response.access_token!!,
            refreshToken = response.refresh_token!!,
            expires = Instant.now().plusSeconds(response.expires_in!!).epochSecond
        )
    }

    private fun refreshOauthToken() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestBody.add("client_id", tokenStore.clientId)
        requestBody.add("client_secret", tokenStore.clientSecret)
        requestBody.add("grant_type", "refresh_token")
        requestBody.add("refresh_token", tokenStore.refreshToken)
        requestBody.add("redirect_uri", URLEncoder.encode("http://localhost:8080", charset("UTF-8")))

        loadOauthToken(headers, requestBody)
    }

    fun requestOauthToken(clientId: String, clientSecret: String, authenticationCode: String, state: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestBody.add("client_id", clientId)
        requestBody.add("client_secret", clientSecret)
        requestBody.add("code", authenticationCode)
        requestBody.add("grant_type", "authorization_code")
        requestBody.add("state", state)
        requestBody.add("redirect_uri", URLEncoder.encode("http://localhost:8080", charset("UTF-8")))

        loadOauthToken(headers, requestBody)
    }

    fun hello(): String {
        val path = "/common/helloworld"
        val requestEntity = apiRequest(path)
        return restTemplate.exchange(requestEntity, String::class.java).body!!
    }

    fun accounts(): String {
        val path = "/personal/banking/accounts"
        return try {
            restTemplate.exchange(apiRequest(path), String::class.java).body!!
        } catch(ux: Unauthorized) {
            refreshOauthToken()
            restTemplate.exchange(apiRequest(path), String::class.java).body!!
        }
    }

    fun transactions(accountKey: String): String {
        val path = "/personal/banking/transactions?accountKey=$accountKey"
        return try {
            restTemplate.exchange(apiRequest(path), String::class.java).body!!
        } catch (ue: Exception) {
            refreshOauthToken()
            restTemplate.exchange(apiRequest(path), String::class.java).body!!
        }
    }
}

data class AccessTokenDTO(
    val access_token: String? = null,
    val refresh_token: String? = null,
    val expires_in: Long? = null
)