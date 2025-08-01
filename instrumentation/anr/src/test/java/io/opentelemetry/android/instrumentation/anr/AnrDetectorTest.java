/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.android.instrumentation.anr;

import static io.opentelemetry.api.common.AttributeKey.stringKey;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Looper;
import io.opentelemetry.android.instrumentation.common.EventAttributesExtractor;
import io.opentelemetry.android.internal.services.applifecycle.AppLifecycle;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnrDetectorTest {

    @Mock Looper mainLooper;
    @Mock ScheduledExecutorService scheduler;
    @Mock AppLifecycle appLifecycle;

    @Test
    void shouldInstallInstrumentation() {
        when(mainLooper.getThread()).thenReturn(new Thread());
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().build();

        EventAttributesExtractor<StackTraceElement[]> extractor =
                (parentContext, o) -> Attributes.of(stringKey("test.key"), "abc");
        AnrDetector anrDetector =
                new AnrDetector(
                        Collections.singletonList(extractor),
                        mainLooper,
                        scheduler,
                        appLifecycle,
                        openTelemetry);
        anrDetector.start();

        // verify that the ANR scheduler was started
        verify(scheduler)
                .scheduleWithFixedDelay(
                        isA(AnrWatcher.class), eq(1L), eq(1L), eq(TimeUnit.SECONDS));
        // verify that an application listener was installed
        verify(appLifecycle).registerListener(isA(AnrDetectorToggler.class));
    }
}
