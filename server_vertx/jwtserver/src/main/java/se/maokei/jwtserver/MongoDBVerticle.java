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
import se.maokei.jwtserver.database.MongoManager;

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
    JsonObject dbConfig = new JsonObject();
    dbConfig.put("connection_string", "mongodb://" + config().getString("mongodb.host") + ":" + config().getInteger("mongodb.port") + "/" + config().getString("mongodb.dbname"));
    //No need
    //dbConfig.put("username", config().getString("mongodb_username"));
    //dbConfig.put("password", config().getString("mongodb_password"));
    //dbConfig.put("authSource", config().getString("mongodb_authSource"));
    dbConfig.put("useObjectId", true); //object is will be mapped to string
    mongoClient = MongoClient.createShared(vertx, dbConfig);
    MongoManager mongoManager = new MongoManager(mongoClient);
    mongoManager.registerConsumer(vertx);
  }

  @Override
  public void stop() {
    LOGGER.info("MongoDB verticle stopping");
  }
}
