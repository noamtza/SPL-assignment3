package bgu.spl.net.api;

import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;

public interface Connections<T> {
    void addConnection(ConnectionHandler<T> connection, int id);
    public <T> boolean send( int connectionId,T msg);
    public  <T> void broadcast(T msg);
}
