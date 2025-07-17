package com.bauto.demand;

import org.jboss.logging.Logger;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class DemandGeneratorApplication {

    private static final Logger LOG = Logger.getLogger(DemandGeneratorApplication.class);


    public static void main(String... args) {
        LOG.info("Staring the application DemandGeneratorApplication");
        Quarkus.run(args);
    }
}
