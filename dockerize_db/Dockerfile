FROM alpine:3.20.3
EXPOSE 8082 9092
HEALTHCHECK --interval=1m --timeout=5s CMD wget --spider 127.0.0.1:8082 || exit 1
RUN apk add --no-cache openjdk11-jre-headless
RUN addgroup -S h2db && adduser -S h2db -G h2db && chown h2db:h2db /home/h2db
USER h2db
WORKDIR /home/h2db
COPY --chmod=0750 --chown=h2db:h2db h2-2.1.214.jar radioplayer.mv.db .h2.server.properties ./
CMD ["java", "-jar", "h2-2.1.214.jar", "-webAllowOthers", "-tcpAllowOthers"]
