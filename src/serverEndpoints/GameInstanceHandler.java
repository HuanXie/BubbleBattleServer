package serverEndpoints;

import javax.websocket.Session;

import org.json.JSONObject;

import model.UserAccount;

public class GameInstanceHandler {
  private static final int max = 16;
  // max 16 allowed for now
  private GameInstance[] instances = new GameInstance[max];

  public GameInstanceHandler() {
    for (int i = 0; i < max; ++i) {
      instances[i] = new GameInstance();
    }
  }

  public Session getPeer(Session player, int id) {
    if (id < max && id >= 0) {
      return instances[id].getPeer(player);
    }
    return null;
  }

  public boolean joinGame(Session player, int id, UserAccount ua) {
    boolean success = false;
    if (id < max && id >= 0) {
      return instances[id].join(player, ua);
    }
    return success;
  }

  public boolean ongoing(int id) {
    if (id < max && id >= 0) {
      return instances[id].ongoing();
    }
    return false;
  }

  public JSONObject getStatus() {
    JSONObject status = new JSONObject();
    status.put("action", "allGameStatus");
    for (int i = 0; i < max; ++i) {
      if (ongoing(i)) {
        status.put("" + i, "ongoing");
      } else {
        status.put("" + i, "waiting");
      }
    }

    return status;
  }

  public void shoot(Session session, JSONObject jsonObj) {
    for (GameInstance gameInstance : instances) {
      if (gameInstance.contains(session)) {
        gameInstance.shoot(session, jsonObj);
        break;
      }
    }
  }

  public void hit(Session session, JSONObject jsonObj) {
    for (GameInstance gameInstance : instances) {
      if (gameInstance.contains(session)) {
        gameInstance.hit(session, jsonObj);
        break;
      }
    }
  }

  public void win(Session session, int id, UserAccount ua) {
    if (id < max && id >= 0) {
      instances[id].win(session, ua);
    }
  }

  public void gameover(Session session, JSONObject jsonObj) {
    for (GameInstance gameInstance : instances) {
      if (gameInstance.contains(session)) {
        gameInstance.gameover(session, jsonObj);
        break;
      }
    }
  }

  public void handleProfile(Session session, int id) {
    if (id < max && id >= 0) {
      instances[id].handleProfile(session, id);
    }
  }
}
