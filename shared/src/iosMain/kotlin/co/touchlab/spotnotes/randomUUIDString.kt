package co.touchlab.spotnotes

import platform.Foundation.NSUUID

internal actual fun randomUUIDString() = NSUUID().UUIDString()
