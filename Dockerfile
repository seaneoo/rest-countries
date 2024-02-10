FROM gradle:jdk17-jammy AS builder

RUN useradd -rm -d /home/builder -s /bin/bash -g root -G sudo -u 1001 builder
USER builder
WORKDIR /home/builder/

COPY gradle gradle/
COPY src src/
COPY build.gradle.kts .
COPY gradle.properties .
COPY gradlew .
COPY settings.gradle.kts .

RUN ./gradlew installDist

FROM gradle:jdk17-jammy

RUN useradd -rm -d /home/runner -s /bin/bash -g root -G sudo -u 1001 runner
USER runner
WORKDIR /home/runner/

COPY --from=builder /home/builder/build build/
CMD "./build/install/rest-countries/bin/rest-countries"
