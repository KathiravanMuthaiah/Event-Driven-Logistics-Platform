package com.bauto.unifier;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class KafkaUnifierApplication {
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
