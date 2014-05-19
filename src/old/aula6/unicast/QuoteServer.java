package aula6.unicast;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 05-11-2013
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}
