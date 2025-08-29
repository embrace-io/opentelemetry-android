/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("DEPRECATION") // suppress deprecation for android.app.Fragment

package io.opentelemetry.android.instrumentation.common

import android.app.Activity
import android.app.Fragment
import io.opentelemetry.android.instrumentation.annotations.RumScreenName
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ScreenNameExtractorTest {
    @Test
    fun testActivity() {
        val activity = Activity()
        val name = ScreenNameExtractor.DEFAULT.extract(activity)
        Assertions.assertEquals("Activity", name)
    }

    @Test
    fun testFragment() {
        val fragment = Fragment()
        val name = ScreenNameExtractor.DEFAULT.extract(fragment)
        Assertions.assertEquals("Fragment", name)
    }

    @Test
    fun testObject() {
        val obj = Object()
        val name = ScreenNameExtractor.DEFAULT.extract(obj)
        Assertions.assertEquals("Object", name)
    }

    @Test
    fun testAnnotatedActivity() {
        val activity: Activity = AnnotatedActivity()
        val name = ScreenNameExtractor.DEFAULT.extract(activity)
        Assertions.assertEquals("squarely", name)
    }

    @Test
    fun testAnnotatedObject() {
        val obj = AnnotatedObject()
        val name = ScreenNameExtractor.DEFAULT.extract(obj)
        Assertions.assertEquals("woohoo", name)
    }

    @RumScreenName("bumpity")
    private class AnnotatedFragment : Fragment()

    @RumScreenName("squarely")
    private class AnnotatedActivity : Activity()

    @RumScreenName("woohoo")
    private class AnnotatedObject
}
