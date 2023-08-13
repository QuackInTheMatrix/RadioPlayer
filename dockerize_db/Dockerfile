FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY h2-2.1.214.jar /opt/app
COPY .h2.server.properties radioplayer.mv.db /root/
EXPOSE 8082 9092
CMD ["java", "-jar", "/opt/app/h2-2.1.214.jar", "-webAllowOthers", "-tcpAllowOthers"]
