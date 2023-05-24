#!/usr/bin/env bash
rm -rf build
./gradlew nativeCompile
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost/bp
export SPRING_DATASOURCE_PASSWORD=bp
export SPRING_DATASOURCE_USERNAME=bp
./build/native/nativeCompile/vaadin
