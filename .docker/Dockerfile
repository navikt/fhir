# Used to build IGs locally without having to manually install all the required dependencies.
# References:
# - https://github.com/NIH-NCPI/hl7-fhir-ig-publisher
# - https://github.com/FHIR/auto-ig-builder

FROM openjdk:11-jre-slim
WORKDIR /app

RUN apt-get update && \
  apt-get -y install curl && \
  curl -sL https://deb.nodesource.com/setup_16.x | bash - && \
  apt-get -y install ruby-full build-essential zlib1g-dev nodejs libfontconfig1 libfreetype6 && \
  npm install -g fsh-sushi && \
  gem install jekyll bundler && \
  curl -L https://github.com/HL7/fhir-ig-publisher/releases/latest/download/publisher.jar -o publisher.jar && \
  curl -L https://github.com/hapifhir/org.hl7.fhir.core/releases/latest/download/validator_cli.jar -o validator_cli.jar && \
  curl -L https://github.com/navikt/fhir-validator-junit-engine/releases/latest/download/fhir-validator-junit-engine.jar -o engine.jar && \
  curl -L https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.8.1/junit-platform-console-standalone-1.8.1.jar -o junit_cli.jar

COPY ./docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]