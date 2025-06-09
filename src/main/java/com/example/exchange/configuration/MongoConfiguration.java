package com.example.exchange.configuration;

import de.flapdoodle.embed.mongo.config.ImmutableNet;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.transitions.Start;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {
    private RunningMongodProcess runningMongodProcess;


    @PostConstruct
    public void startEmbeddedMongo() {

        try {
            ImmutableNet immutableNet = Net.builder().bindIp("127.0.0.1").port(27017).isIpv6(false).build();
            final Mongod exe = Mongod.instance()
                    .withNet(Start.to(Net.class).initializedWith(immutableNet));
            runningMongodProcess = exe.start(Version.Main.V8_0).current();
        } catch (Exception e) {
            System.out.println("MongoDB Startup Exception: " + e.getMessage());
        }

    }

}
