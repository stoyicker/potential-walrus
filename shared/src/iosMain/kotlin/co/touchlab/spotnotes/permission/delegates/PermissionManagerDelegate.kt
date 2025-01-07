package co.touchlab.spotnotes.permission.delegates

import co.touchlab.spotnotes.permission.RequestResult

internal interface PermissionManagerDelegate {
    suspend operator fun invoke(): RequestResult
}
