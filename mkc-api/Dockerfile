FROM harbor.zjbhsk.com:9443/library/openjdk:8-jre-base
MAINTAINER David Flemström <dflemstr@spotify.com>

#ENTRYPOINT ["/usr/local/openjdk-8/bin/java", "-jar", "/usr/share/myservice/myservice.jar"]

#RUN apt-get update && apt-get install -y vim \
#    && apt-get autoremove \
#    && rm -rf /var/lib/apt/lists/* \
#    && rm -f /etc/localtime \
#    && ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo 'alias ll="ls -l"' >> ~/.bashrc \
#    && echo "Asia/Shanghai" > /etc/timezone

# Add Maven dependencies (not shaded into the artifact; Docker-cached)
#ADD target/lib           /usr/share/myservice/lib
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/myservice/myservice.jar
