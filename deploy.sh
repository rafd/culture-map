#!/usr/bin/expect -f

set -euo pipefail

USER=$1
SERVER=$2
PASSWORD=$3

lein uberjar

spawn scp "target/culture_map-0.0.1-standalone.jar" "$USER@$SERVER:/var/www/html/culture_map/culture_map.jar"
expect "assword:"
send "$PASSWORD\r"

spawn ssh -t $USER@$SERVER "sudo supervisorctl restart culture_map"
expect "assword:"
send "$PASSWORD\r"
