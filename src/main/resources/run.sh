#!/bin/sh

java -cp "./:lib/*" "-XX:TieredStopAtLevel=1" "com.cedarmeadowmeats.orderservice.OrderServiceApplication"
# Springboot jar configuration
#exec java -cp "./:./BOOT-INF/classes:./BOOT-INF/lib/*" "-XX:TieredStopAtLevel=1" "com.cedarmeadowmeats.orderservice.OrderServiceApplication"