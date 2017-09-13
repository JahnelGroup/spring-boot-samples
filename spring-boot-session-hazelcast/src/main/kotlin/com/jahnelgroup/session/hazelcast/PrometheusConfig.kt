package com.jahnelgroup.session.hazelcast

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector
import io.prometheus.client.spring.web.EnablePrometheusTiming
import org.springframework.context.annotation.Configuration

@Configuration
@EnablePrometheusTiming
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
class PrometheusConfig