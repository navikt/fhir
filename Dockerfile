# References:
# https://github.com/NIH-NCPI/hl7-fhir-ig-publisher
# https://github.com/FHIR/auto-ig-builder

FROM openjdk:11
WORKDIR /app

RUN curl -sL https://deb.nodesource.com/setup_16.x | bash - && \
  apt-get update && \
  apt-get -y install ruby-full build-essential zlib1g-dev nodejs && \
  npm install -g fsh-sushi && \
  gem install jekyll bundler && \
  mkdir -p /root/.fhir/packages && \
  curl -L https://github.com/HL7/fhir-ig-publisher/releases/latest/download/publisher.jar -o publisher.jar

ENTRYPOINT ["java", "-Xmx2048m", "-jar", "/app/publisher.jar"]
CMD ["/bin/bash", "-c", "echo Welcome to the FHIR IG Publisher"]