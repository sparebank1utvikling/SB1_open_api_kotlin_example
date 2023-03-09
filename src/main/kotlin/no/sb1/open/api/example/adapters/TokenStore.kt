package no.sb1.open.api.example.adapters
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class TokenStore() {
    var clientId: String
    var clientSecret: String
    var accessToken: String
    var refreshToken: String
    var expires: String

    companion object{
        val props = Properties()
    }
    init {
        props.load(FileInputStream("secrets.properties"))
        clientId = props.getProperty("client_id").also {
            if (it.isNullOrEmpty()) {
                throw Exception("Missing client_id")
            }
        }
        clientSecret = props.getProperty("client_secret").also {
            if (it.isNullOrEmpty()) {
                throw Exception("Missing client_secret")
            }
        }
        accessToken = props.getProperty("access_token").orEmpty()
        refreshToken = props.getProperty("refresh_token").orEmpty()
        expires = props.getProperty("expires").orEmpty()
    }

    fun saveTokens(accessToken: String, refreshToken: String, expires: Long) {
        val fileOutputStream = FileOutputStream("secrets.properties")
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.expires = expires.toString()
        props.setProperty("access_token",accessToken)
        props.setProperty("refresh_token",refreshToken)
        props.setProperty("expires", expires.toString())
        fileOutputStream.use {
            props.store(it, "Props")
        }
        println(props.toString())
    }

    fun loadTokens() = this
}
