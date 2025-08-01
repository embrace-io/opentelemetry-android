
# Session Instrumentation

Out of the box, the `opentelemetry-android` agent will create and manage sessions. Sessions are associated
with a unique identifier and will expire after a duration. This instrumentation is responsible
for adding a `SessionObserver` instance that will generate OpenTelemetry events for
`session.start` and `session.end` when interesting things happen to the session.

See [session.md in the semantic conventions](https://github.com/open-telemetry/semantic-conventions/blob/main/docs/general/session.md)
for additional details about sessions.

See ["Session Events"](https://github.com/open-telemetry/semantic-conventions/blob/main/docs/general/session.md#session-events)
for more information about the specific events generated by this instrumentation.

## Installation

This instrumentation comes with the [android agent](../../android-agent) out of the box, so
if you depend on it, you don't need to do anything else to install this instrumentation.
However, if you don't use the agent but instead depend on [core](../../core) directly, you can
manually install this instrumentation by following the steps below.

### Adding dependencies

```kotlin
implementation("io.opentelemetry.android.instrumentation:sessions:0.13.0-alpha")
```

1. You can find the latest version [here](https://central.sonatype.com/artifact/io.opentelemetry.android.instrumentation/sessions).
