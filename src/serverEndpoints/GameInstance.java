package serverEndpoints;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import model.UserAccount;

public class GameInstance {
  private static final int NUMBER_OF_PLAYERS_FOR_A_GAME = 2;
  private Set<Session> players = new HashSet<Session>();
  private Set<UserAccount> userAccounts = new HashSet<UserAccount>();

  public boolean join(Session player, UserAccount ua) {
    boolean success = false;
    if (players.size() < NUMBER_OF_PLAYERS_FOR_A_GAME && !players.contains(player)) {
      players.add(player);
      userAccounts.add(ua);
      success = true;

      if (players.size() == NUMBER_OF_PLAYERS_FOR_A_GAME) {
        // now we can start the game
        JSONObject startGame = new JSONObject();
        startGame.put("action", "start");

        try {
          player.getBasicRemote().sendText(startGame.toString());
          getPeer(player).getBasicRemote().sendText(startGame.toString());

          // Increase their totalGame
          for (UserAccount account : userAccounts) {
            account.setNumberOfTotalGames(account.getNumberOfTotalGames() + 1);
          }
          persistUserAccounts(userAccounts);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return success;
  }

  public void win(Session s, UserAccount ua) {
    if (players.contains(s)) {
      ua.setNumberOfWins(ua.getNumberOfWins() + 1);
      persistUserAccount(ua);
    }
  }

  private void persistUserAccounts(Set<UserAccount> userAccounts) {
    EntityManager em = null;
    EntityManagerFactory emfactory = null;
    emfactory = Persistence.createEntityManagerFactory("BubbleBattle");
    em = emfactory.createEntityManager();
    em.getTransaction().begin();
    for (UserAccount ua : userAccounts) {
      // em.persist(ua);
      em.merge(ua);
    }
    em.getTransaction().commit();
    em.close();
    emfactory.close();
  }

  private void persistUserAccount(UserAccount ua) {
    EntityManager em = null;
    EntityManagerFactory emfactory = null;
    emfactory = Persistence.createEntityManagerFactory("BubbleBattle");
    em = emfactory.createEntityManager();
    em.getTransaction().begin();
    // em.persist(ua);
    em.merge(ua);
    em.getTransaction().commit();
    em.close();
    emfactory.close();
  }

  public boolean ongoing() {
    return players.size() == NUMBER_OF_PLAYERS_FOR_A_GAME;
  }

  public boolean contains(Session session) {
    return players.contains(session);
  }

  public Session getPeer(Session s) {
    Session peer = null;
    if (players.contains(s)) {
      for (Session session : players) {
        if (session != s) {
          peer = session;
          break;
        }
      }
    }

    return peer;
  }

  public void shoot(Session session, JSONObject jsonObj) {
    try { // make sure that it is a integer
      int x = Integer.parseInt(jsonObj.getString("x"));
      Session peer = getPeer(session);

      // send msg to the peer
      JSONObject enemyShoot = new JSONObject();
      enemyShoot.put("action", "shoot");
      enemyShoot.put("x", "" + x);
      peer.getBasicRemote().sendText(enemyShoot.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void hit(Session session, JSONObject jsonObj) {
    try { // make sure that it is a integer
      Session peer = getPeer(session);

      // send msg to the peer
      JSONObject enemyHit = new JSONObject();
      enemyHit.put("action", "hit");
      peer.getBasicRemote().sendText(enemyHit.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void gameover(Session session, JSONObject jsonObj) {
    try { // make sure that it is a integer
      Session peer = getPeer(session);
      // send msg to the peer
      JSONObject win = new JSONObject();
      win.put("action", "win");
      peer.getBasicRemote().sendText(win.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void handleProfile(Session session, int id) {
    if (!players.isEmpty()) {
      JSONObject response = new JSONObject();
      response.put("action", "profileResponse");
      JSONArray array = new JSONArray();
      int i = 0;
      for (UserAccount ua : userAccounts) {
        JSONObject profile = new JSONObject();
        profile.put("email", ua.getEmail());
        profile.put("totalGames", ua.getNumberOfTotalGames());
        profile.put("wins", ua.getNumberOfWins());
        array.put(i, profile);
        i++;
      }

      response.put("profiles", array);
      try {
        session.getBasicRemote().sendText(response.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
