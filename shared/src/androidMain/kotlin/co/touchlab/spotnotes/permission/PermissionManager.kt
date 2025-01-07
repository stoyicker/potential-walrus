package co.touchlab.spotnotes.permission

import androidx.activity.ComponentActivity

fun buildPermissionManager(activity: ComponentActivity): PermissionManager =
    ActivityPermissionManager(activity)
