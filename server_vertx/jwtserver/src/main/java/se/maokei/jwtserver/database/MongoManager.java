package se.maokei.jwtserver.database;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;

import java.util.Optional;

/**
 *
 * Create database eventsdb
 * */
public class MongoManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
  private MongoClient  mongoClient = null;

  public MongoManager(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public void findAllUsers(Message<Object> message) {

  }

  public void createCollection() {
    this.mongoClient.createCollection("", res -> {
      if(res.succeeded()) {
        LOGGER.info("Created collection");
      }else{
        res.cause().printStackTrace();
      }
    });
  }

  public void findUserByEmail(Message<Object> message) {
    JsonObject data = new JsonObject(message.body().toString());
    JsonObject user = data.getJsonObject("user");
    JsonObject query = new JsonObject();
    query.put("email", user.getValue("email"));
    this.mongoClient.findOne("users", query, new JsonObject(), res -> {
      if(res.succeeded()) {
        Optional<JsonObject> opt = Optional.ofNullable(res.result());
        if(opt.isPresent()) {
          message.reply(opt.get());
        }else{
          message.reply(new JsonObject().put("error", "No user with email: " + user.getValue("email")));
        }
      }else{
        message.reply(new JsonObject().put("error", "Internal error"));
        LOGGER.error("Error findUserByEmail looking up: " + user.getValue("email"));
      }
    });
  }

  public void registerConsumer(Vertx vertx) {
    vertx.eventBus().consumer("se.maokei.mongoservice", message -> {
      JsonObject jo = new JsonObject(message.body().toString());
      switch(jo.getString("cmd")) {
        case "findAllUsers":
          findAllUsers(message);
          break;
        case "createCollection":
          createCollection();
          break;
        case "addUser":
          addUser(message);
          break;
        case "findUserByEmail":
          findUserByEmail(message);
          break;
        default:
          LOGGER.info("No such command in mongo manager: ");
      }
    });
  }

  private void addUser(Message<Object> message) {
    JsonObject mess = new JsonObject(message.body().toString());
    JsonObject user = mess.getJsonObject("user");

    //TODO user already exists check
    this.mongoClient.insert("users", user, res -> {
      if(res.succeeded()) {
        try {
          message.reply(new JsonObject().put("success", user));
        }catch(Exception e) {

        }
      }else{
        LOGGER.error("MongoManager could not add user");
        message.reply(new JsonObject().put("error", "Unable to add user"));
      }
    });
  }
}
