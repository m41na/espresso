#!/usr/bin/env bash

java -jar presso-mkdn-1.0.jar \
  -host 0.0.0.0 \
  -port 8080 \
  -redirectSecure true \
  -securePort 8443 \
  -keystorePath /home/zesty/certs/server-keystore.jks \
  -keystorePass changeit \
  -home /mnt/www \
  -pages public \
  -welcome index.html