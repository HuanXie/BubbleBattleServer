package serverEndpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.json.JSONObject;

import model.UserAccount;

public class SessionHandler {
  // All connected sessions
  // private Set<Session> sessions = new HashSet<>();
  private Map<Session, UserAccount> sessionToUserAccount = new HashMap<Session, UserAccount>();

  public void addSession(Session s, UserAccount ua) {
    sessionToUserAccount.put(s, ua);
  }

  public void removeSession(Session s) {
    sessionToUserAccount.remove(s);
  }

  public void sendToSession(Session s, JSONObject msg) {
    try {
      s.getBasicRemote().sendText(msg.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean hasSession(Session s) {
    return sessionToUserAccount.containsKey(s);
  }

  public void sendToAllSessions(JSONObject msg) {
    for (Session session : sessionToUserAccount.keySet()) {
      sendToSession(session, msg);
    }
  }

  public UserAccount getUserAccount(Session s) {
    return sessionToUserAccount.get(s);
  }
}
