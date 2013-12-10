package tp_continua.client;

import tp_continua.common.*;

import java.io.IOException;

/**
 * Connects to a dungeon master to validate authentication
 */
public class AuthRequest implements Runnable {

    private String user;
    private String password;
    private Client client;
    private boolean authAnswer;

    public AuthRequest(Client client, String user, String password) {
        this.user = user;
        this.password = password;
        this.client = client;
    }

    /**
     * Sends a multicast message with an authentication format and waits for an dungeon master to reply authentication's validity
     */
    @Override
    public void run() {
        //Sends AUTH_REQUEST User:Password
        String message = String.format("%s %s:%s", Protocol.AUTH_REQUEST, user, password);
        ConnectionManager.sendMulticast(message);
        try {
            ConnectionManager.listenForUDP(new UDPResponseCallBack() {
                @Override
                public void messageReceived(String messageReceived, Peer peer) {
                    if (messageReceived.startsWith(Protocol.AUTH_RESPONSE)) {

                        String result = messageReceived.substring(Protocol.AUTH_RESPONSE.length()).trim();
                        authAnswer = true;
                        AuthResponse response;
                        if (result.equals("OK")) {
                            response = AuthResponse.OK;
                        } else {
                            response = AuthResponse.NOK;
                        }
                        client.authResponse(new AuthResponseEvent(peer, response));
                    }
                }
            });
        } catch (IOException ex) {

        } finally {
            if (!authAnswer) {
                client.authFailed(new AuthFailedEvent(client));
            }
        }
    }
}
