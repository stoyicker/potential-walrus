package co.touchlab.spotnotes.permission

import co.touchlab.spotnotes.permission.delegates.LocationPermissionManagerDelegate

internal class IOsPermissionManager : PermissionManager {
    override suspend fun request(permission: Permission): RequestResult = when (permission) {
        Permission.LOCATION -> LocationPermissionManagerDelegate()
    }()
}
