package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the UserAccount database table.
 * 
 */
@Entity
@Table(name = "UserAccount")
@NamedQueries({ @NamedQuery(name = "UserAccount.findAll", query = "SELECT u FROM UserAccount u"),
    @NamedQuery(name = "UserAccount.findUser", query = "SELECT u from UserAccount u WHERE u.email = :email"), })
public class UserAccount implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int id;

  private String email;

  private String password;

  private int numberOfWins;

  private int numberOfTotalGames;

  public UserAccount() {
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getNumberOfWins() {
    return numberOfWins;
  }

  public void setNumberOfWins(int numberOfWins) {
    this.numberOfWins = numberOfWins;
  }

  public int getNumberOfTotalGames() {
    return numberOfTotalGames;
  }

  public void setNumberOfTotalGames(int numberOfTotalGames) {
    this.numberOfTotalGames = numberOfTotalGames;
  }
}