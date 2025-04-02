package example.com.services

import example.com.plugins.Logger
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties

class EmailService(
    private val smtpHost: String,
    private val smtpPort: Int,
    private val username: String,
    private val password: String,
    private val fromEmail: String,
    private val isProduction: Boolean = false
) {
    private val properties = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", smtpHost)
        put("mail.smtp.port", smtpPort.toString())
    }
    
    private val session: Session by lazy {
        Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }
    
    fun sendPasswordResetEmail(toEmail: String, resetToken: String, baseUrl: String) {
        if (!isProduction) {
            Logger.d("Development mode: Would send password reset email to $toEmail")
            Logger.d("Reset link: $baseUrl/reset-password/$resetToken")
            return
        }
        
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(fromEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                subject = "Password Reset Request"
                
                val resetLink = "$baseUrl/reset-password/$resetToken"
                setText("""
                    Hello,
                    
                    You have requested to reset your password. Please click the link below to reset your password:
                    
                    $resetLink
                    
                    If you didn't request a password reset, please ignore this email.
                    
                    This link will expire in 24 hours.
                    
                    Regards,
                    Active Hive Team
                """.trimIndent())
            }
            
            Transport.send(message)
            Logger.d("Password reset email sent to $toEmail")
        } catch (e: Exception) {
            Logger.d("Failed to send password reset email: ${e.message}")
            throw e
        }
    }
    
    /**
     * A simple development-friendly implementation that just logs the email.
     * In production, this would be replaced with actual email sending.
     */
    fun sendEmail(toEmail: String, subject: String, body: String) {
        if (!isProduction) {
            Logger.d("Development mode: Would send email to $toEmail")
            Logger.d("Subject: $subject")
            Logger.d("Body: $body")
            return
        }
        
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(fromEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                setSubject(subject)
                setText(body)
            }
            
            Transport.send(message)
            Logger.d("Email sent to $toEmail")
        } catch (e: Exception) {
            Logger.d("Failed to send email: ${e.message}")
            throw e
        }
    }
} 