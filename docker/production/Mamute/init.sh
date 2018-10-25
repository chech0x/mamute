#!/usr/bin/env bash


#USER=$(cat /etc/secrets/user)
#PASSWORD=$(cat /etc/secrets/password)
#DB=$(cat /etc/secrets/instance)
#HOSTNAME=$(hostname)

sed -i -e "s?>root<?>$DB_USER<?g" /mamute/WEB-INF/classes/production/hibernate.cfg.xml && \
 sed -i -e "s?><?>$DB_PASS<?g" WEB-INF/classes/production/hibernate.cfg.xml && \
 sed -i -e "s?localhost/mamute_production?$DB_HOST?g" WEB-INF/classes/production/hibernate.cfg.xml && \
 sed -i -e "s?true?false?g" WEB-INF/classes/production/hibernate.cfg.xml && \
 sed -i -e "s?MyISAM?InnoDB?g" WEB-INF/classes/db_structure.sql

#sed -i -e "s?//localhost?//$HOSTNAME?g" /mamute/WEB-INF/classes/production.properties
sleep 15 && VRAPTOR_ENV=production ./run.sh
