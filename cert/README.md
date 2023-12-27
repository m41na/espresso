## How to

### Adding command line arguments

```-securePort 8443 -keystorePath <projects root>\\espresso\\cert\\server-truststore.jks -keystorePass <your password>```

### self-signed

- Navigate to cert directory

```cd cert```

- generate self-signed certificate and place it in a keystore

```keytool -genkey -keyalg RSA -alias jetty-server -keystore server-keystore.jks -validity 365 -keysize 2048```

> Generating 2,048 bit RSA key pair and self-signed certificate (SHA256withRSA) with a validity of 365 days
> for: CN=<domain>, OU=<org unit>, O=<org name>, L=<city>, ST=<state code>, C=<country code>

- view generated key in the keystore

```keytool -list -v -keystore server-keystore.jks```

```
    Keystore type: PKCS12
    Keystore provider: SUN
    Your keystore contains 1 entry
    Alias name: jetty-server
```

- export the certificate to a certificate file

```keytool -export -alias jetty-server -keystore server-keystore.jks -rfc -file jetty-server.cert```

- import the generated certificate into the truststore of your client

```keytool -importcert -alias jetty-server -file jetty-server.cert -keystore server-truststore.jks```

### CA-signed

- generate CA certificate request

```keytool -certreq -alias jetty-server -keystore server-keystore.jks -file jetty-server.csr```

- import the CA certificate sent back into the truststore of your client

```keytool -import -trustcacerts -alias jetty-server-ca -file <certificate_file> -keystore server-truststore.jks```

for example, download the CA issued .pem file, and then import
```keytool -import -trustcacerts -alias jetty-server-ca -file X:\Downloads\<pem file> -keystore cert\server-truststore.jks```

delete an existing key
```keytool -delete -noprompt -alias jetty-server-ca -keystore server-truststore.jks -storepass changeit```