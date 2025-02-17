package example.com.security.token

interface TokenService {
    fun generateAuthToken(config: TokenConfig, vararg claims: TokenClaim): String
    fun generateVerificationToken(config: TokenConfig, vararg claims: TokenClaim): String
}