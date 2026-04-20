# IoTControlProtocolV2

This is a WIP version of a simple Babel protocol that exposes a Babel-compliant event-based interface to control and interact with a set of IoT devices in the context of Raspberry Pis.

The protocol defines configuration parameters to assist in the configuration of these devices, it furthers maintains information and state to facilitate the interaction with some of the devices.

## Authors

- João Brilha (j.brilha@campus.fct.unl.pt)
- João Leitão (jc.leitao@fct.unl.pt)

## Parameters

## Interface

## Importing to a Project

### Repository Setup

If you haven't already done so, you will need to add the following to your ```pom.xml``` file.

```xml
<repositories>
    <repository>
        <id>novasys-mvn</id>
        <url>https://novasys.di.fct.unl.pt/packages/mvn</url>
    </repository>
</repositories>
```

### Maven Dependency

Include the following dependency on your pom.xml

```xml
<dependency>
    <groupId>pt.unl.fct.di.novasys.babel</groupId>
    <artifactId>iot-control-protocol-v2</artifactId>
    <version>[0.0.1,)</version>
</dependency>
```
