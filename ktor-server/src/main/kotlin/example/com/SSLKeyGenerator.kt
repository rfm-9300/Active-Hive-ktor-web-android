package example.com

import example.com.plugins.Logger
import io.ktor.network.tls.certificates.*
import java.io.File
import java.lang.RuntimeException

/**
 * Utility to generate a self-signed certificate for local development
 */
object SSLKeyGenerator {
    // Store this so we can reference it from the application.yaml
    val KEYSTORE_PATH: String = File(System.getProperty("user.dir"), "keystore.jks").absolutePath
    
    /**
     * Generate a self-signed certificate and save it to the specified file
     */
    fun generate() {
        // Create the keystore at the absolute path
        generateKeystoreAt(KEYSTORE_PATH)
        println("Keystore file created at: $KEYSTORE_PATH")
    }
    
    private fun generateKeystoreAt(path: String) {
        val keyStoreFile = File(path)
        try {
            // Make sure the parent directory exists
            keyStoreFile.parentFile?.mkdirs()
            
            val keyStore = buildKeyStore {
                certificate("sampleAlias") {
                    password = "password123"
                    domains = listOf("127.0.0.1", "0.0.0.0", "localhost")
                }
            }
            keyStore.saveToFile(keyStoreFile, "password123")
            Logger.d("Self-signed certificate generated at ${keyStoreFile.absolutePath}")
        } catch (e: Exception) {
            Logger.d("Failed to generate certificate at $path: ${e.message}")
            e.printStackTrace()
        }
    }
} 