package serverEndpoints;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import model.UserAccount;

@ServerEndpoint(value = "/gamelobby", configurator = MyConfigurator.class)
public class GameLobbyEndpoint {
  private SessionHandler sessionHandler = new SessionHandler();
  private GameInstanceHandler gameInstanceHandler = new GameInstanceHandler();

  @OnMessage
  public void onMessage(Session session, String msg) {
    try {
      System.out.println(this + " received msg " + msg + " from " + session.getId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    JSONObject jsonObj = new JSONObject(msg);
    String action = jsonObj.getString("action");
    switch (action) {
    // admin actions
    case "register":
      handleRegister(session, jsonObj);
      break;
    case "login":
      handleLogin(session, jsonObj);
      break;
    case "logout":
      handleLogout(session, jsonObj);
      break;
    case "myprofile":
      handleMyProfile(session, jsonObj);
      break;
    // game related actions
    case "join":
      handleJoin(session, jsonObj);
      break;
    case "shoot":
      handleShoot(session, jsonObj);
      break;
    case "quit": // player quits the game
      handleQuit(session, jsonObj);
      break;
    case "win": // A player has won the game
      handleWin(session, jsonObj);
      break;
    case "playerprofile":
      handleProfile(session, jsonObj);
      break;
    case "hit":
      handleHit(session, jsonObj);
    case "gameover":
      handleGameover(session, jsonObj);
    default:
      break;
    }
  }

  private void handleMyProfile(Session session, JSONObject jsonObj) {
    UserAccount userAccount = sessionHandler.getUserAccount(session);
    JSONObject myProfile = new JSONObject();
    myProfile.put("email", userAccount.getEmail());
    myProfile.put("total", userAccount.getNumberOfTotalGames());
    myProfile.put("wins", userAccount.getNumberOfWins());
    try {
      session.getBasicRemote().sendText(myProfile.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleProfile(Session session, JSONObject jsonObj) {
    int id = jsonObj.getInt("id");
    gameInstanceHandler.handleProfile(session, id);
  }

  private void handleWin(Session session, JSONObject jsonObj) {
    UserAccount ua = sessionHandler.getUserAccount(session);
    if (ua != null) {
      // The player wins
      gameInstanceHandler.win(session, jsonObj.getInt("id"), ua);
      // Update all logged in users with new game instance status
      sessionHandler.sendToAllSessions(gameInstanceHandler.getStatus());
    } else {
      System.out.println("Error: unknown user declares a win");
    }
  }

  private void handleQuit(Session session, JSONObject jsonObj) {
    // The player who quit has lost, so the peer wins
    int id = jsonObj.getInt("id");
    Session peer = gameInstanceHandler.getPeer(session, id);
    gameInstanceHandler.win(peer, id, sessionHandler.getUserAccount(peer));
    // Update all logged in users with new game instance status
    sessionHandler.sendToAllSessions(gameInstanceHandler.getStatus());
  }

  private void handleShoot(Session session, JSONObject jsonObj) {
    gameInstanceHandler.shoot(session, jsonObj);
  }

  private void sendJoinResponse(Session s, boolean success) {
    JSONObject joinSuccess = new JSONObject();
    joinSuccess.put("action", "joinResponse");
    if (success) {
      joinSuccess.put("status", "success");
    } else {
      joinSuccess.put("status", "failure");
    }

    sessionHandler.sendToSession(s, joinSuccess);
  }

  private void handleJoin(Session session, JSONObject jsonObj) {
    int id = jsonObj.getInt("id");
    if (gameInstanceHandler.joinGame(session, id, sessionHandler.getUserAccount(session))) {
      sessionHandler.sendToAllSessions(gameInstanceHandler.getStatus());
      sendJoinResponse(session, true);
    } else {
      sendJoinResponse(session, false);
    }
  }

  private void handleLogout(Session session, JSONObject jsonObj) {
    if (sessionHandler.hasSession(session)) {
      sessionHandler.removeSession(session);
    }
  }

  private void handleLogin(Session session, JSONObject jsonObj) {
    String email = jsonObj.getString("email");
    String password = jsonObj.getString("password");

    boolean success = false;
    EntityManager em = null;
    EntityManagerFactory emfactory = null;
    emfactory = Persistence.createEntityManagerFactory("BubbleBattle");
    em = emfactory.createEntityManager();

    // retrieve all user accounts
    em.getTransaction().begin();
    List<UserAccount> allUserAccounts = em.createNamedQuery("UserAccount.findAll", UserAccount.class).getResultList();
    if (!allUserAccounts.isEmpty()) {
      for (UserAccount userAccount : allUserAccounts) {
        if (userAccount.getEmail().equals(email) && userAccount.getPassword().equals(password)) {
          success = true;
          sessionHandler.addSession(session, userAccount);
          break;
        }
      }
    }
    // else: empty

    em.getTransaction().commit();
    em.close();
    emfactory.close();

    JSONObject jsonObject = new JSONObject();
    if (success) {
      jsonObject.put("status", "success");
      jsonObject.put("message", "login is successful");
    } else {
      jsonObject.put("status", "failure");
      jsonObject.put("message", "the email/password is not correct");
    }

    System.out.println("server sending json = " + jsonObject.toString());
    try {
      session.getBasicRemote().sendText(jsonObject.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleHit(Session session, JSONObject jsonObj) {
    gameInstanceHandler.hit(session, jsonObj);
  }

  private void handleGameover(Session session, JSONObject jsonObj) {
    gameInstanceHandler.gameover(session, jsonObj);
  }

  @OnOpen
  public void onOpen(Session session) {
    // sessionHandler.addSession(session);
    System.out.println("New session opened: " + session.getId());
  }

  @OnError
  public void onError(Session session, Throwable t) {
    sessionHandler.removeSession(session);
    System.err.println("Error on session " + session.getId());
    if (t != null) {
      System.err.println(t.getMessage());
    }
  }

  @OnClose
  public void onClose(Session session) {
    sessionHandler.removeSession(session);
    System.out.println("session closed: " + session.getId());
  }

  private void handleRegister(Session s, JSONObject jsonObj) {
    String email = jsonObj.getString("email");
    String password = jsonObj.getString("password");

    boolean success = true;
    EntityManager em = null;
    EntityManagerFactory emfactory = null;
    emfactory = Persistence.createEntityManagerFactory("BubbleBattle");
    em = emfactory.createEntityManager();

    // retrieve all user accounts
    em.getTransaction().begin();
    List<UserAccount> allUserAccounts = em.createNamedQuery("UserAccount.findAll", UserAccount.class).getResultList();
    if (!allUserAccounts.isEmpty()) {
      for (UserAccount userAccount : allUserAccounts) {
        if (userAccount.getEmail().equals(email)) {
          success = false; // email already used, cannot register
          break;
        }
      }
    }
    // else: empty

    if (success) {
      UserAccount newUser = new UserAccount();
      newUser.setEmail(email);
      newUser.setPassword(password);
      em.persist(newUser);
      sessionHandler.addSession(s, newUser);
    }
    em.getTransaction().commit();
    em.close();
    emfactory.close();

    // JsonObjectBuilder builder = Json.createObjectBuilder();

    JSONObject jsonObject = new JSONObject();
    if (success) {
      jsonObject.put("status", "success");
      jsonObject.put("message", "registration is successful");
    } else {
      jsonObject.put("status", "failure");
      jsonObject.put("message", "failure:the email has already been registered");
    }

    System.out.println("server sending json = " + jsonObject.toString());
    try {
      s.getBasicRemote().sendText(jsonObject.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
