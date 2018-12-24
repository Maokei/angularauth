package se.maokei.jwtserver;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

public class MongoDBVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBVerticle.class);
  private static MongoClient mongoClient = null;



  public static void main(String[] args) {
    VertxOptions vertxOptions = new VertxOptions();
    vertxOptions.setClustered(true);

    Vertx.clusteredVertx(vertxOptions, res -> {
      if(res.succeeded()) {
        Vertx vertx  = res.result();
        ConfigRetriever cr = ConfigRetriever.create(vertx);
        cr.getConfig(config -> {
          if(config.succeeded()) {
            JsonObject json = config.result();
            DeploymentOptions options = new DeploymentOptions().setConfig(json);
            vertx.deployVerticle(new MongoDBVerticle(), options);
          }else{
            LOGGER.error("Could not get config MongoDB verticle");
          }
        });
      }else{
        LOGGER.error("Could not cluster MongoDB verticle");
      }
    });
  }

  @Override
  public void start() {
    LOGGER.info("Starting MongoDB verticle");
    JsonObject dbconfig = new JsonObject();
    System.out.println("");


  }

  @Override
  public void stop() {
    LOGGER.info("MongoDB verticle stopping");
  }
}
