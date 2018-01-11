FROM ubuntu:14.04

MAINTAINER Ranganath Gurram <rgurram@tibco.com>

ENV TOMCAT_VERSION 8.0.46

# Set locales
RUN locale-gen en_GB.UTF-8
ENV LANG en_GB.UTF-8
ENV LC_CTYPE en_GB.UTF-8

# Fix sh
RUN rm /bin/sh && ln -s /bin/bash /bin/sh

# Install dependencies
RUN apt-get update && \
apt-get install -y git build-essential curl wget software-properties-common

# Install JDK 8
RUN \
echo oracle-java9-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
add-apt-repository -y ppa:webupd8team/java && \
apt-get update && \
apt-get install -y oracle-java9-installer wget unzip tar && \
rm -rf /var/lib/apt/lists/* && \
rm -rf /var/cache/oracle-jdk8-installer

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-9-oracle

#copy  /usr/lib/jvm/java-8-oracle/lib/tools.jar /usr/lib/jvm/java-8-oracle/jre/lib

# Get Tomcat
RUN wget --quiet --no-cookies http://www-eu.apache.org/dist/tomcat/tomcat-9/v9.0.2/bin/apache-tomcat-9.0.2.tar.gz -O /tmp/tomcat.tgz \
& tar xzvf /tmp/tomcat.tgz -C /opt  \
& mv /opt/apache-tomcat-9.0.2 /opt/tomcat \
& rm /tmp/tomcat.tgz  
# rm -rf /opt/tomcat/webapps/examples && \
# rm -rf /opt/tomcat/webapps/docs && \
#rm -rf /opt/tomcat/webapps/ROOT

# Add admin/admin user
COPY tomcat-users.xml /opt/tomcat/conf/tomcat-users.xml
COPY server.xml /opt/tomcat/conf/server.xml

#ADD https://github.com/rangag/sample_app_test/blob/master/webhdfs.war /opt/tomcat/webapps

RUN git clone https://github.com/rangag/sample_app_test
RUN mv sample_app_test/webhdfs /opt/tomcat/webapps



ENV CATALINA_HOME /opt/tomcat
ENV PATH $PATH:$CATALINA_HOME/bin

EXPOSE 8080
EXPOSE 8009
#VOLUME "/opt/tomcat/webapps"
WORKDIR /opt/tomcat

# Launch Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]
#CMD ["bash"]
