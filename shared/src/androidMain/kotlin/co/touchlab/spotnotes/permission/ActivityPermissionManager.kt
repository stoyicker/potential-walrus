package co.touchlab.spotnotes.permission

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

internal class ActivityPermissionManager(activity: ComponentActivity) : PermissionManager {
    private var activity: ComponentActivity? = activity
    private val permissionRequestResultCallback = PermissionRequestResultCallback()
    private val requestLauncher = activity.activityResultRegistry.register(
        KEY_LAUNCHER,
        ActivityResultContracts.RequestPermission(),
        permissionRequestResultCallback
    )

    init {
        activity.lifecycle.addObserver(TeardownObserver())
    }

    override suspend fun request(permission: Permission): RequestResult {
        val activity = activity
        if (activity == null) {
            return RequestResult.UNKNOWN
        }
        val androidKey = permission.asAndroidKey
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, androidKey)) {
            return RequestResult.REQUEST_PREVENTED
        }
        if (ActivityCompat.checkSelfPermission(activity.applicationContext, androidKey)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return RequestResult.ALREADY_GRANTED
        }
        val sink = MutableSharedFlow<Boolean?>(extraBufferCapacity = 1)
        permissionRequestResultCallback.addDelegate(sink)
        requestLauncher.launch(androidKey)
        return when (sink.first()) {
            true -> RequestResult.NEWLY_GRANTED
            false -> RequestResult.NOT_GRANTED
            null -> RequestResult.UNKNOWN
        }
    }

    private class PermissionRequestResultCallback : ActivityResultCallback<Boolean> {
        private val delegates = ArrayDeque<MutableSharedFlow<Boolean?>>()

        override fun onActivityResult(it: Boolean) {
            delegates.removeFirst().tryEmit(it)
        }

        fun addDelegate(delegate: MutableSharedFlow<Boolean?>) {
            delegates.addLast(delegate)
        }

        fun releaseDelegates() {
            delegates.iterator()
                .let {
                    while (it.hasNext()) {
                        it.next().tryEmit(null)
                        it.remove()
                    }
                }
        }
    }

    private inner class TeardownObserver : LifecycleEventObserver {
        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event
        ) {
            val activity = activity ?: return
            if (source === activity && event == Lifecycle.Event.ON_DESTROY) {
                activity.lifecycle.removeObserver(this)
                this@ActivityPermissionManager.activity = null
                permissionRequestResultCallback.releaseDelegates()
                requestLauncher.unregister()
            }
        }
    }

    companion object {
        private const val KEY_LAUNCHER = "KEY_LAUNCHER"
    }
}
