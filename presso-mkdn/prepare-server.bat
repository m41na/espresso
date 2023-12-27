
scp build\libs\presso-mkdn-1.0-SNAPSHOT.jar zesty@192.168.1.2:/home/zesty/apps/presso-mkdn-app.jar

scp x:\Downloads\jinsi_ddns_net.pem zesty@192.168.1.2:/home/zesty/certs/cacert.pem

scp start-app.sh zesty@192.168.1.2:/home/zesty/apps/start-app.sh

keytool -import -trustcacerts -alias jetty-server-ca -file X:\Downloads\<pem file> -keystore cert\server-truststore.jks