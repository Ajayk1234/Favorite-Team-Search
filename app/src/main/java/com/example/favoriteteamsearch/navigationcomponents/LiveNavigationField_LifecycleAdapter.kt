package com.example.favoriteteamsearch.navigationcomponents

import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MethodCallsLogger

class LiveNavigationField_LifecycleAdapter internal constructor(internal val mReceiver: LiveNavigationField<Any>) :
    GeneratedAdapter {

    override fun callMethods(
        owner: LifecycleOwner, event: Lifecycle.Event, onAny: Boolean,
        logger: MethodCallsLogger?
    ) {
        val hasLogger = logger != null
        if (onAny) {
            return
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            if (!hasLogger || logger!!.approveCall("onDestroy", 1)) {
                mReceiver.onDestroy()
            }
            return
        }
    }
}