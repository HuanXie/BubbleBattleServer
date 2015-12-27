package serverEndpoints;

import javax.websocket.server.ServerEndpointConfig;

public class MyConfigurator extends ServerEndpointConfig.Configurator {

  private static final GameLobbyEndpoint ENDPOINT = new GameLobbyEndpoint();

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    if (GameLobbyEndpoint.class.equals(endpointClass)) {
      return (T) ENDPOINT;
    } else {
      throw new InstantiationException();
    }
  }
}
