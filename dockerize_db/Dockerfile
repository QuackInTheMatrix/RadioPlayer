FROM alpine:3.20.3
EXPOSE 8082 9092
COPY h2-2.1.214.jar radioplayer.mv.db /root/
COPY .h2.server.properties /root/
RUN apk add openjdk11-jre-headless
CMD ["java", "-jar", "/root/h2-2.1.214.jar", "-webAllowOthers", "-tcpAllowOthers"]

