package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import model.UserAccount;

@Path("/login")
public class LoginService {
  @Path("{email}/{password}")
  @GET
  @Produces("application/json")
  public Response login(@PathParam("email") String email, @PathParam("password") String password) throws JSONException {
    boolean success = false;
    EntityManager em = null;
    EntityManagerFactory emfactory = null;
    emfactory = Persistence.createEntityManagerFactory("BubbleBattle");
    em = emfactory.createEntityManager();

    // retrieve all user accounts
    for (UserAccount userAccount : em.createNamedQuery("UserAccount.findAll", UserAccount.class).getResultList()) {
      if (userAccount.getEmail().equals(email) && userAccount.getPassword().equals(password)) {
        success = true;
        break;
      }
    }
    em.close();
    emfactory.close();

    JSONObject jsonObject = new JSONObject();
    if (success) {
      jsonObject.put("Result", "success");
    } else {
      jsonObject.put("Result", "failure:email and/or password is incorrect");
    }

    jsonObject.put("user name", email);
    jsonObject.put("password", password);

    String result = "login result: " + jsonObject;

    try {
      File file = new File("/home/jun/tmp/log.txt");
      FileWriter writer = new FileWriter(file);
      writer.write(result);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Response.status(200).entity(result).build();
  }
}
