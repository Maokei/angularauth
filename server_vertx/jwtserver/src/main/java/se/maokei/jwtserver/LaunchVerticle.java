package se.maokei.jwtserver;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class LaunchVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(LaunchVerticle.class);

  public static void main(String[] args) {
    Vertx vertx  = Vertx.vertx();
    ConfigRetriever cr = ConfigRetriever.create(vertx);
    cr.getConfig(config -> {
      if(config.succeeded()) {
        JsonObject conf = config.result();
        DeploymentOptions options = new DeploymentOptions().setConfig(conf);
        vertx.deployVerticle(new LaunchVerticle(), options);
      }
    });
  }

  /**
  * @param future
  * */
  @Override
  public void start(Future<Void > future) {
    CompositeFuture.all(deployHelper(vertx, MainVerticle.class.getName()),
      deployHelper(vertx, MongoDBVerticle.class.getName()))
      .setHandler(result -> {
        if(result.succeeded()){
          future.complete();
        } else {
          future.fail(result.cause());
          LOGGER.error("Failed to deploy verticles!" + result.cause());
        }
      });
  }

  private Future<Void> deployHelper(Vertx vertx, String name) {
    final Future<Void> future = Future.future();
    ConfigRetriever cr = ConfigRetriever.create(vertx);
    cr.getConfig(config -> {
      if (config.succeeded()) {
        JsonObject conf = config.result();
        DeploymentOptions options = new DeploymentOptions().setConfig(conf);
        vertx.deployVerticle(name, options, result -> {
          if (result.failed()) {
            LOGGER.error("Failed to deploy verticle " + name);
            future.fail(result.cause());
          } else {
            LOGGER.info("Deployed verticle " + name);
            future.complete();
          }
        });
      }
    });
    return future;
  }

  /**
   * Deploys verticle with class name loads config.json and clusters with hazelcast.
   * @param name class name of verticle to be deployed.
   * @return Future
   * */
  private Future<Void> deploymentHelperSeprate(String name) {
    final Future<Void> future = Future.future();
    VertxOptions vertxOptions = new VertxOptions();
    vertxOptions.setClustered(true);
    Vertx.clusteredVertx(vertxOptions, res -> {
      if(res.succeeded()) {
        Vertx vertx  = res.result();
        ConfigRetriever cr = ConfigRetriever.create(vertx);
        cr.getConfig(config -> {
          if(config.succeeded()) {
            JsonObject conf = config.result();
            DeploymentOptions options = new DeploymentOptions().setConfig(conf);
            vertx.deployVerticle(name, options, result -> {
              if(result.succeeded()) {
                future.complete();
              }else{
                future.fail(result.cause());
              }
            });
          }
        });
      }
    });
    return future;
  }
}
