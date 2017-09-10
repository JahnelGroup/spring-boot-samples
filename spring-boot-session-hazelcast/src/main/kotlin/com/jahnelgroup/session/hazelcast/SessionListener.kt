package com.jahnelgroup.session.hazelcast

import com.hazelcast.core.EntryEvent
import com.hazelcast.map.listener.EntryAddedListener
import com.hazelcast.map.listener.EntryRemovedListener
import io.prometheus.client.Gauge

class SessionListener : EntryAddedListener<String, String>, EntryRemovedListener<String, String> {

    var sessionGauge : Gauge = Gauge.build("http_sessions",
            "This gauge will inc when a session is created and dec when it is destroyed.").register()

    override fun entryAdded(event: EntryEvent<String, String>) {
        sessionGauge.inc()
    }

    override fun entryRemoved(event: EntryEvent<String, String>) {
        sessionGauge.dec()
    }

}