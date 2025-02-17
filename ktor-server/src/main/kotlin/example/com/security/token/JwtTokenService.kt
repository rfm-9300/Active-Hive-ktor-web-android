package example.com.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService: TokenService {

    override fun generateAuthToken(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresInt))
        claims.forEach {
            token = token.withClaim(it.name, it.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override fun generateVerificationToken(config: TokenConfig, vararg claims: TokenClaim): String {
        val newConfig = config.copy(expiresInt = 1000L * 60L * 60L * 24L)
        var token = JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + newConfig.expiresInt))
        claims.forEach {
            token = token.withClaim(it.name, it.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}