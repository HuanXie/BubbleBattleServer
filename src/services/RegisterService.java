package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import model.UserAccount;

@Path("/register")
public class RegisterService {
  @Path("{email}/{password}")
  @GET
  @Produces("application/json")
  @Consumes("text/plain")
  public Response login(@PathParam("email") String email, @PathParam("password") String password) throws JSONException {
    try {
      File file = new File("/home/jun/tmp/log.txt");
      FileWriter writer = new FileWriter(file);
      writer.write("entering registering");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    }
    em.getTransaction().commit();
    em.close();
    emfactory.close();

    JSONObject jsonObject = new JSONObject();
    if (success) {
      jsonObject.put("Result", "success");
    } else {
      jsonObject.put("Result", "failure:the email has already been registered");
    }

    jsonObject.put("user name", email);
    jsonObject.put("password", password);

    String result = "registration result: " + jsonObject;
    return Response.status(200).entity(result).build();

  }
}
