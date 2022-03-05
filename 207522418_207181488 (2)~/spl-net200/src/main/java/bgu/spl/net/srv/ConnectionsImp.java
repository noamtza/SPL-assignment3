package bgu.spl.net.srv;

import bgu.spl.net.api.Connections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImp<T> implements Connections<T> {
    public static ConnectionsImp instance = new ConnectionsImp();
    private ConcurrentHashMap<Integer,ConnectionHandler<T>> idClient;//maybe add just when registering
        //AtomicInteger idCounter;


        public ConnectionsImp() {
            idClient = new ConcurrentHashMap<>();
        //    idCounter = new AtomicInteger(0);
        }

    public static <T> ConnectionsImp<T> getInstance() {
            return instance;
    }

    public void addConnection(ConnectionHandler<T> connection,int id) {//check if blocking or non blocking

            idClient.put(id, connection);
        }

    public <T> boolean send(int conId,T msg) {
            if (idClient.containsKey(conId)) {
                ConnectionHandler conn=idClient.get(conId);
                conn.send(msg);
                return true;
            }
            else {
                return false;
        }
        }

        public <T> void broadcast(T msg) {
            // no need for sync because handler.send is thread safe
            for(ConnectionHandler conn: idClient.values()){
                conn.send(msg);
            }
        }
        public void diconnect(int conId){
            idClient.remove(conId);
        }
    }
//        Iterator it=idClient.entrySet().iterator();
//        while(it.hasNext()){
//           Map.Entry mapElement=(Map.Entry)it.next();
//            BlockingConnectionHandler<T> c=(BlockingConnectionHandler<T>)mapElement.getValue();
//            c.send(msg);
//        }



