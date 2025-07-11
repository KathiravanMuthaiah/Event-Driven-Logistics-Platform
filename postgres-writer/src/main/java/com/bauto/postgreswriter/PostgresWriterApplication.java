package com.bauto.postgreswriter;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Entry point for the Postgres Writer service.
 * Not mandatory but useful for explicit run/debug setups.
 */
@QuarkusMain
public class PostgresWriterApplication {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}

