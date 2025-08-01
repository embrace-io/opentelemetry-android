/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.android.instrumentation.crash;

import static io.opentelemetry.android.common.RumConstants.BATTERY_PERCENT_KEY;
import static io.opentelemetry.android.common.RumConstants.HEAP_FREE_KEY;
import static io.opentelemetry.android.common.RumConstants.STORAGE_SPACE_FREE_KEY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import androidx.annotation.Nullable;
import io.opentelemetry.android.instrumentation.common.EventAttributesExtractor;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import java.io.File;

/** Represents details about the runtime environment at a time */
public final class RuntimeDetailsExtractor<RQ, RS> extends BroadcastReceiver
        implements EventAttributesExtractor<RQ> {

    private @Nullable volatile Double batteryPercent = null;
    private final File filesDir;

    public static <RQ, RS> RuntimeDetailsExtractor<RQ, RS> create(Context context) {
        IntentFilter batteryChangedFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        File filesDir = context.getFilesDir();
        RuntimeDetailsExtractor<RQ, RS> runtimeDetails = new RuntimeDetailsExtractor<>(filesDir);
        context.registerReceiver(runtimeDetails, batteryChangedFilter);
        return runtimeDetails;
    }

    private RuntimeDetailsExtractor(File filesDir) {
        this.filesDir = filesDir;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batteryPercent = level * 100.0d / (float) scale;
    }

    @Override
    public Attributes extract(io.opentelemetry.context.Context parentContext, RQ request) {
        AttributesBuilder attributes = Attributes.builder();
        attributes.put(STORAGE_SPACE_FREE_KEY, getCurrentStorageFreeSpaceInBytes());
        attributes.put(HEAP_FREE_KEY, getCurrentFreeHeapInBytes());

        Double currentBatteryPercent = getCurrentBatteryPercent();
        if (currentBatteryPercent != null) {
            attributes.put(BATTERY_PERCENT_KEY, currentBatteryPercent);
        }
        return attributes.build();
    }

    private long getCurrentStorageFreeSpaceInBytes() {
        return filesDir.getFreeSpace();
    }

    private long getCurrentFreeHeapInBytes() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.freeMemory();
    }

    @Nullable
    private Double getCurrentBatteryPercent() {
        return batteryPercent;
    }
}
