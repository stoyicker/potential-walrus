package co.touchlab.spotnotes.permission

interface PermissionManager {
    suspend fun request(permission: Permission): RequestResult
}
