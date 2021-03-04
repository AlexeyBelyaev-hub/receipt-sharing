#!/usr/bin/env bash

mvn clean package

echo 'Copy files'

scp -i ~/.ssh/id_rsa \
  target/receipt-sharing-0.0.2-SNAPSHOT.jar \
  root@194.87.214.209:home/rcp/

echo 'Restart server...'


ssh -i ~/.ssh/id_rsa root@194.87.214.209 << EOF

pgrep java | xargs kill -9
nohup java -jar ./home/rcp/receipt-sharing-0.0.2-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'