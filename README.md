# Babel IoT Control Protocols

Concrete Babel protocols that drive IoT devices attached to a Raspberry
Pi gateway. Each protocol owns the Pi4J `Context` for its bus
(digital-input, digital-output, I²C-input, I²C-output) and translates
incoming Babel requests into reads or writes on the underlying
hardware, emitting replies and notifications back through the runtime.

The abstract event surface (requests, replies, notifications, device
handles, threshold predicates) is defined by the companion artifact
[`babel-iot-control-api`](../babel-iot-control-api).

**Group ID:** `pt.paradigmshift.iot`
**Artifact ID:** `babel-iot-control-protocols`
**Current version:** `1.0.0`
**Tested on:** Raspberry Pi 4 and 5 with Grove sensors / actuators
attached either directly or through the GrovePi base hat.

> **Status:** work in progress. Protocol coverage and request handlers
> are still being filled in — see the per-protocol notes below.

---

## Origin

This library is a fork of the IoT control protocols originally
developed at
[NOVA School of Science and Technology (NOVA FCT)](https://www.fct.unl.pt)
as part of the [TaRDIS](https://tardis-project.eu) European research
project on swarm systems (work package 6):

> **Original repository:**
> https://codelab.fct.unl.pt/di/research/tardis/wp6/iot/protocols/iot-control-protocols
>
> **Original authors:** João Brilha, João Leitão

The fork was created to serve as the IoT control layer used by the
StoneFlux edge gateway and is maintained by
[ParadigmShift](https://www.paradigmshift.pt). All original authorship
is acknowledged and preserved. Additions and modifications made after
the fork are copyright ParadigmShift.

---

## Protocol catalogue

| Protocol | ID | Bus | Drives |
|---|---:|---|---|
| `DigitalInputControlProtocol` | 4002 | GPIO digital | Ultrasonic ranger, encoder |
| `DigitalOutputControlProtocol` | — | GPIO digital | LED bar, chainable RGB, buzzer, 4-digit display |
| `I2CInputControlProtocol` | — | I²C | 3-axis accelerometer, gesture detector, barometer |
| `I2COutputControlProtocol` | — | I²C | LCD, RGB LCD, LED matrix |

Each protocol exposes the same control surface — `RegisterIoTDeviceRequest`
returns a `DeviceHandle`; subsequent input or output requests carry the
handle. Reactive input requests use `Threshold<T>` to gate notifications
to interesting samples. The `IoTMonitoringService` (singleton) owns
the background threads that poll long-running listeners (encoder,
gesture detector).

Higher-level helpers live alongside the protocols:

- **Listeners** (`controlprotocols/listeners/`): `EncoderListener`,
  `GestureListener`, `IoTListener`, `IoTMonitoringService`
- **Notifications** (`controlprotocols/notifications/`):
  `EncoderNotification`, `GestureNotification`
- **Replies** (`controlprotocols/replies/`): typed wrappers around
  `IoTInputReply` for accelerometer, barometer, encoder, gesture and
  ultrasonic-ranger reads
- **Output requests** (`controlprotocols/requests/output/`): show /
  clear / colour-set / animation / emoji / text payloads for displays
  and chainable RGB LEDs
- **Input requests** (`controlprotocols/requests/input/`): one-shot
  and reactive variants for each supported sensor
- **Utilities**: `I2CScanner` for discovering devices on the I²C bus

---

## Local API duplication

For historical reasons the project bundles its own local copy of the
`pt.unl.fct.di.tardis.babel.iot.api.*` classes (under `src/main/java/...`)
in addition to the published [`babel-iot-control-api`](../babel-iot-control-api).

The local copy is a slightly older snapshot — most files match, but
the request hierarchy uses the older `IoTEventRequest` /
`IoTPeriodicEventRequest` / `IoTReactiveEventRequest` naming, while
the published API has been renamed to `IoTInputRequest` /
`IoTPeriodicInputRequest` / `IoTReactiveInputRequest` and split into
`IoTInputRequest` / `IoTOutputRequest`.

Until the protocols are migrated to the published API, the local
package is authoritative for this module's compilation. The two are
not interchangeable.

---

## Usage

Add to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>paradigmshift-repository</id>
        <name>ParadigmShift Repository</name>
        <url>https://maven.paradigmshift.pt/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>pt.paradigmshift.iot</groupId>
        <artifactId>babel-iot-control-protocols</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

This artifact transitively brings:

- `pt.paradigmshift.babel:babel-core` — Babel runtime (`GenericProtocol`, `ProtoRequest`, `ProtoReply`, `ProtoNotification`)
- `pt.paradigmshift.iot:pi4j-iot-device-library` — Grove device wrappers
- `pt.paradigmshift.iot:pi4j-components` — Pi4J component catalogue and runtime plugins

You only need to declare this single dependency to add the protocols
to a Babel application.

### Wiring a protocol into a Babel app

```java
Properties props = ...;
Babel babel = Babel.getInstance();

DigitalInputControlProtocol digitalIn = new DigitalInputControlProtocol();
digitalIn.init(props);
babel.registerProtocol(digitalIn);

// Now any other protocol can sendRequest to DigitalInputControlProtocol.PROTOCOL_ID
sendRequest(
    new RegisterIoTDeviceRequest(DeviceType.GROVE_ULTRASONIC_RANGER, "front-bumper", 7),
    DigitalInputControlProtocol.PROTOCOL_ID);
```

> **Hardware note:** the protocols must run on a Raspberry Pi with the
> standard Pi4J runtime providers (`raspberrypi`, `linuxfs`, `gpiod`,
> `pigpio`, `mock`). They will compile on any platform but will fail
> at runtime elsewhere because of the underlying native libraries.

---

## Building

Requires Java 17 and Maven 3.6+.

```bash
mvn verify    # compile + (no tests yet)
mvn package   # produces JAR, sources JAR, and Javadoc JAR
mvn deploy    # publish to maven.paradigmshift.pt (requires REPOSILITE_TOKEN)
```

The protocols depend on:

- `pt.paradigmshift.babel:babel-core:1.0.0`
- `pt.paradigmshift.iot:pi4j-iot-device-library:1.0.0`
- `pt.paradigmshift.iot:pi4j-components:0.0.7`

These must be available either in `~/.m2/` (after a local `mvn install`
of each) or on `maven.paradigmshift.pt`.

## Releasing

Push a version tag — the GitHub Actions CI workflow builds and deploys
automatically:

```bash
git tag v1.0.0
git push origin v1.0.0
```

---

## Related artifacts

| Artifact | Purpose |
|---|---|
| `pt.paradigmshift.iot:babel-iot-control-api` | Canonical Babel events and supporting types |
| `pt.paradigmshift.iot:babel-iot-control-protocols` | This artifact — concrete protocol implementations |
| `pt.paradigmshift.iot:pi4j-iot-device-library` | Java device wrappers driven by the protocols |

---

## License

Copyright (c) 2026 ParadigmShift, Lda. See [LICENSE](LICENSE) for full terms.

Commercial use outside of ParadigmShift requires a written licence.
Contact: [info@paradigmshift.pt](mailto:info@paradigmshift.pt)
