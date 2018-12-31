package se.maokei.jwtserver.database;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

public class MongoManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
  private MongoClient  mongoClient = null;

  public MongoManager(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public void findAll() {

  }

  public void createCollection() {
    this.mongoClient.createCollection("eventsdb", res -> {
      if(res.succeeded()) {
        //created
      }else{
        res.cause().printStackTrace();
      }
    });
  }

  public void findUser(JsonObject user) {

  }

  public void registerConsumer(Vertx vertx) {
    vertx.eventBus().consumer("se.maokei.mongoservice", message -> {
      JsonObject jo = new JsonObject(message.body().toString());
      switch(jo.getString("cmd")) {
        case "findAll":
          findAll();
          break;
        case "createCollection":
          createCollection();
          break;
        case "findUser":
          findUser(jo);
          break;
        default:
          LOGGER.info("No such command in mongo manager: ");
      }
    });
  }
}
