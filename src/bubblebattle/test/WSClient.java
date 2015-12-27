package bubblebattle.test;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONObject;

@ClientEndpoint()
public class WSClient {

  @OnMessage
  public void onMessage(String message) {
    System.out.println("client received msg: " + message);
    JSONObject jsonObject = new JSONObject(message);
    String status = jsonObject.getString("status");
    String verboseMessage = jsonObject.getString("message");

    // String status = message.getString("status");
    // String verboseMessage = message.getString("message");
    System.out.println("Status: " + status);
    System.out.println("Message: " + verboseMessage);
  }

  public static void main(String[] args) {
    WebSocketContainer container = null;//
    Session session = null;
    try {
      // Tyrus is plugged via ServiceLoader API. See notes above
      container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WSClient.class, URI.create("ws://localhost:8080/BubbleBattle/gamelobby"));
      // JsonObject msgToServer = Json.createObjectBuilder().add("action",
      // "register").add("email", "jx1@as.com")
      // .add("password", "1234").build();
      JSONObject msgToServer = new JSONObject();
      msgToServer.put("action", "register");
      msgToServer.put("email", "jx@as.com");
      msgToServer.put("password", "1234");
      System.out.println("sending json = " + msgToServer.toString());
      session.getBasicRemote().sendText(msgToServer.toString());
      while (true) {
        // wait
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    System.err.println("Error on session " + session.getId());
    if (t != null) {
      System.out.println(t.getMessage());
    }
  }
}