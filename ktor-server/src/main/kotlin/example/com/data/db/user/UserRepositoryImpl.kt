package example.com.data.db.user

import example.com.data.db.event.EventAttendeeTable
import example.com.data.db.event.EventTable
import example.com.data.db.event.toEvent
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

class UserRepositoryImpl: UserRepository {
    override suspend fun getUser(email: String): User? = suspendTransaction {
        UserTable
            .select { UserTable.email eq email }
            .singleOrNull()?.let {
                User(
                    id = it[UserTable.id].value,
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt],
                    verified = it[UserTable.verified],
                    googleId = it[UserTable.googleId],
                    authProvider = AuthProvider.valueOf(it[UserTable.authProvider])
                )
            }
    }

    override suspend fun getUserById(userId: Int): User? = suspendTransaction {
        UserTable
            .select { UserTable.id eq userId }
            .singleOrNull()?.let {
                User(
                    id = it[UserTable.id].value,
                    email = it[UserTable.email],
                    password = it[UserTable.password],
                    salt = it[UserTable.salt],
                    verified = it[UserTable.verified],
                    verificationToken = it[UserTable.verificationToken],
                    googleId = it[UserTable.googleId],
                    authProvider = AuthProvider.valueOf(it[UserTable.authProvider])
                )
            }
    }

    override suspend fun getUserByGoogleId(googleId: String): User? = suspendTransaction {
        UserTable
            .select { UserTable.googleId eq googleId }
            .singleOrNull()?.let {
                User(
                    id = it[UserTable.id].value,
                    email = it[UserTable.email],
                    password = "",
                    salt = "",
                    verified = true,
                    googleId = it[UserTable.googleId],
                    authProvider = AuthProvider.valueOf(it[UserTable.authProvider])
                )
            }
    }

    override suspend fun createOrUpdateGoogleUser(
        email: String, 
        googleId: String, 
        firstName: String, 
        lastName: String, 
        profileImageUrl: String
    ): User? = suspendTransaction {
        try {
            // Check if user with this Google ID already exists
            val existingUser = UserTable
                .select { UserTable.googleId eq googleId }
                .singleOrNull()
                
            if (existingUser != null) {
                // Update existing user
                UserTable.update({ UserTable.id eq existingUser[UserTable.id] }) {
                    it[UserTable.email] = email
                    it[UserTable.verified] = true
                }
                
                // Update profile if it exists
                val userProfileId = existingUser[UserTable.id]
                val userProfile = UserProfilesTable
                    .select { UserProfilesTable.userId eq userProfileId }
                    .singleOrNull()
                    
                if (userProfile != null) {
                    UserProfilesTable.update({ UserProfilesTable.userId eq userProfileId }) {
                        it[UserProfilesTable.firstName] = firstName
                        it[UserProfilesTable.lastName] = lastName
                        it[UserProfilesTable.email] = email
                        // Only update image if provided
                        if (profileImageUrl.isNotEmpty()) {
                            it[UserProfilesTable.imagePath] = profileImageUrl
                        }
                    }
                }
                
                return@suspendTransaction User(
                    id = existingUser[UserTable.id].value,
                    email = email,
                    verified = true,
                    googleId = googleId,
                    authProvider = AuthProvider.GOOGLE
                )
            } else {
                // Check if user with same email exists
                val existingEmailUser = UserTable
                    .select { UserTable.email eq email }
                    .singleOrNull()
                    
                if (existingEmailUser != null) {
                    // Update the existing user to link with Google
                    UserTable.update({ UserTable.id eq existingEmailUser[UserTable.id] }) {
                        it[UserTable.googleId] = googleId
                        it[UserTable.authProvider] = AuthProvider.GOOGLE.name
                        it[UserTable.verified] = true
                    }
                    
                    return@suspendTransaction User(
                        id = existingEmailUser[UserTable.id].value,
                        email = email,
                        verified = true,
                        googleId = googleId,
                        authProvider = AuthProvider.GOOGLE
                    )
                } else {
                    // Create new user
                    val userId = UserTable.insert {
                        it[UserTable.email] = email
                        it[UserTable.password] = ""
                        it[UserTable.salt] = ""
                        it[UserTable.verified] = true
                        it[UserTable.googleId] = googleId
                        it[UserTable.authProvider] = AuthProvider.GOOGLE.name
                        it[UserTable.createdAt] = LocalDateTime.now()
                    } get UserTable.id
                    
                    // Create user profile
                    UserProfilesTable.insert {
                        it[UserProfilesTable.userId] = userId
                        it[UserProfilesTable.firstName] = firstName
                        it[UserProfilesTable.lastName] = lastName
                        it[UserProfilesTable.email] = email
                        it[UserProfilesTable.phone] = ""
                        it[UserProfilesTable.joinedAt] = LocalDateTime.now()
                        it[UserProfilesTable.imagePath] = profileImageUrl.ifEmpty { "profile" }
                    }
                    
                    return@suspendTransaction User(
                        id = userId.value,
                        email = email,
                        verified = true,
                        googleId = googleId,
                        authProvider = AuthProvider.GOOGLE
                    )
                }
            }
        } catch (e: Exception) {
            println("Error creating/updating Google user: ${e.message}")
            null
        }
    }

    override suspend fun addUser(user: User): Boolean = suspendTransaction {
        try {
            UserTable.insert {
                it[email] = user.email
                it[password] = user.password
                it[salt] = user.salt
                it[verified] = user.verified
                it[verificationToken] = user.verificationToken
                it[createdAt] = user.createdAt
            }
            UserProfilesTable.insert {
                it[userId] = UserTable.select { UserTable.email eq user.email }.single()[UserTable.id]
                it[firstName] = user.profile?.firstName ?: ""
                it[lastName] = user.profile?.lastName ?: ""
                it[email] = user.profile?.email ?: ""
                it[phone] = user.profile?.phone ?: ""
                it[joinedAt] = user.profile?.joinedAt ?: LocalDateTime.now()
                it[imagePath] = user.profile?.profileImagePath ?: "profile"
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserProfile(userId: Int): UserProfile? = suspendTransaction {
        try {
            val userProfile = UserProfilesTable.select { UserProfilesTable.userId eq userId }.single().toUserProfile()
            val now = LocalDateTime.now()
            
            // Get all events where the user is the organizer
            val hostedEvents = EventTable
                .select { EventTable.organizerId eq userId }
                .map { it.toEvent() }

            // Get all events the user is registered for
            val userEvents = EventAttendeeTable
                .join(EventTable, JoinType.INNER) { EventTable.id eq EventAttendeeTable.eventId }
                .select { EventAttendeeTable.userId eq userId }
                .map { it.toEvent() }
            
            // Split events into past (attended) and future (attending)
            val attendedEvents = userEvents.filter { it.date.isBefore(now) }
            val attendingEvents = userEvents.filter { it.date.isAfter(now) }
            
            // For waiting list events, we need logic from EventWaitingListTable
            // This would be implemented if we had that table
            val waitingListEvents = emptyList<example.com.data.db.event.Event>()

            userProfile.copy(
                hostedEvents = hostedEvents,
                attendedEvents = attendedEvents,
                attendingEvents = attendingEvents,
                waitingListEvents = waitingListEvents
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateUserProfile(userProfile: UserProfile): Boolean = suspendTransaction {
        try {
            UserProfilesTable.update({ UserProfilesTable.userId eq userProfile.userId }) {
                it[firstName] = userProfile.firstName
                it[lastName] = userProfile.lastName
                it[email] = userProfile.email
                it[phone] = userProfile.phone
                it[imagePath] = userProfile.profileImagePath
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}