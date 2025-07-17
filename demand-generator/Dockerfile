FROM quay.io/quarkus/quarkus-micro-image:2.0
COPY target/quarkus-app/lib/ /deployments/lib/
COPY target/quarkus-app/*.jar /deployments/
COPY target/quarkus-app/app/ /deployments/app/
COPY target/quarkus-app/quarkus/ /deployments/quarkus/
CMD ["java", "-jar", "/deployments/quarkus-run.jar"]
