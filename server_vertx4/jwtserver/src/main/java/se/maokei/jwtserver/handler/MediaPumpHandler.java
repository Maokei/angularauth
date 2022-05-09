package se.maokei.jwtserver.handler;

import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

public class MediaPumpHandler {
  private Vertx vertx;

  public MediaPumpHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * @param rc
   * */
  public void getAudio(RoutingContext rc) {
    String filename = "song.mp3";
    File song = new File(getClass().getClassLoader().getResource(filename).getFile());
    System.out.println("song: " + song.getPath());
    AsyncFile asyncFile = vertx.fileSystem().openBlocking(song.getPath(), new OpenOptions());
    String range = rc.request().getHeader("Range");
    HttpServerResponse response = rc.response().setChunked(true);
    if (range.isEmpty()) {
      System.out.println("no range");
      response.putHeader("Content-Length", song.length() + "");
      response.putHeader("Content-Type", "audio/mp3");
      response.setStatusCode(200);
      Pump.pump(asyncFile, response).start();
    }
    response.putHeader("Content-Type", "audio/mp3");
    response.putHeader("Content-Disposition", "inline;filename=\"" + "song.mp3" + "\"");
    response.putHeader("Accept-Ranges", "bytes");
    response.putHeader("ETag", "song.mp3");
    Pump.pump(asyncFile, response).start();
    System.out.println("getAudio");
  }

  /**
   * @param rc
   * */
  public void getVideo(RoutingContext rc) {

  }
}
