FROM java:8-jdk
MAINTAINER Roy Meissner, https://rmeissn.github.io

# Mamute VERSION --> Tested for v1.3 and 1.4
# On change rember to edit the variables also in ../docker-compose.yml
ARG VERSION=1.5.0

RUN mkdir /mamute
WORKDIR /mamute

ADD mamute-${VERSION}.war /mamute/

#RUN wget -nv https://github.com/caelum/mamute/releases/download/v${VERSION}/mamute-${VERSION}.war && \
#    unzip mamute-${VERSION}.war && \
#    rm mamute-${VERSION}.war
RUN  unzip mamute-${VERSION}.war && \
 rm mamute-${VERSION}.war

ADD ./production.properties /mamute/WEB-INF/classes/production.properties
ADD ./init.sh /mamute/init.sh

RUN if echo "$VERSION" | grep -q -E '^1.[345].0$'; then mv /mamute/WEB-INF/classes/production/hibernate.cfg.xml.example /mamute/WEB-INF/classes/production/hibernate.cfg.xml; fi

RUN chmod +x run.sh init.sh

EXPOSE 8080

# Wait for intial Database init --> TODO: Find a better way to wait for the DB
CMD ./init.sh
