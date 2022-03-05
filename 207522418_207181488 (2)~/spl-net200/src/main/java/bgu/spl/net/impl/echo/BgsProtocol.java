package bgu.spl.net.impl.echo;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Connections;
import bgu.spl.net.impl.rci.User;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.MessageImpl;

import java.util.LinkedList;
import java.util.List;

public class BgsProtocol<T> implements BidiMessagingProtocol<T> {
   private User user;
   private int connectionId;
   private Connections<T> connections;
   private MessageImpl messageImp;

    public void start(int connectionId, Connections<T> connections){
        this.connectionId=connectionId;
        this.connections=connections;
    }

 //   @Override
//    public void process(T message) {
//
//    }

    public BgsProtocol(){
       user=new User();
        messageImp=MessageImpl.getInstance();
        connections= ConnectionsImp.getInstance();
        connectionId=-1;
   }
    @Override
    public void process(String msg) {
        int start;
        String messageOp="";
        for(int i=0;i<msg.length()&&msg.charAt(i)!=' ';i++){
            messageOp=messageOp+msg.charAt(i);
        }
        if(messageOp.equals("1")){
            start=2;
            String userName="";
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                userName=userName+msg.charAt(i);
            }
            start=start+ userName.length()+1;
            String password="";
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                password=password+msg.charAt(i);
            }
            start=start+  password.length()+1;
            String birthday="";
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                birthday=birthday+msg.charAt(i);
            }
            start=start+ birthday.length()+1;

            String response=messageImp.register(userName,password,birthday,user,connectionId);
            boolean b;
            System.out.println(response);
            b=connections.send(connectionId,response);
        }
        if(messageOp.equals("2")){
            String userName="";
            start=2;
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                userName=userName+msg.charAt(i);
            }
            start=start+ userName.length()+1;
            String password="";
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                password=password+msg.charAt(i);
            }
            start=start+password.length()+1;
            byte captcha=0;
            if(start>=msg.length()+1){
                captcha =-1;
            }
            if(captcha!=-1&&msg.charAt(start)=='1')
               captcha=1;
            String response=MessageImpl.logIn(userName,password,captcha,user,connections,connectionId);
            connections.send(connectionId,response);
        }
        if(messageOp.equals("3")) {
            String response = MessageImpl.logOut(user);
            connections.send(connectionId, response);
        }

        if(messageOp.equals("4")){
            start=2;
            byte follow=0;
            String userName="";
            if(msg.charAt(start)=='1'){
                follow=1;
            }
            start=start+2;
            for(int i=start;i<msg.length()&&msg.charAt(i)!=' ';i++){
                userName=userName+msg.charAt(i);
            }
            String response=MessageImpl.follow(userName,follow,user);
            connections.send(connectionId, response);
        }

        if(messageOp.equals("5")){
            start=2;
            String content="";
            for(int i=start;i<msg.length();i++) {
                content = content + msg.charAt(i);
            }
            MessageImpl.post(content,user,connections,connectionId);
            //connections.send(connectionId, response);
        }
        if(messageOp.equals("6")){
            start=2;
            String userName = "";
            for (int i = start; i < msg.length() && msg.charAt(i) != ' '; i++) {
                userName = userName + msg.charAt(i);
            }
            start = start + userName.length() + 1;
            String content="";
            String sendingDateAndTime="";
            for(int i=msg.length()-1;i>0&&msg.charAt(i)!=' ';i--){
                sendingDateAndTime =msg.charAt(i)+ sendingDateAndTime ;
            }
            int length= sendingDateAndTime.length();
            for(int i=start;i<msg.length()-length-1;i++) {
                content = content + msg.charAt(i);
            }
            start = start + content.length() + 1;

            MessageImpl.pm(userName,content,sendingDateAndTime,user,connections,connectionId);
           // connections.send(connectionId, response);
        }
        if(messageOp.equals("7")){
            String response=MessageImpl.logStat(user);
            connections.send(connectionId, response);
        }
        if(messageOp.equals("8")){
            start=2;
            List<String> userNames=new LinkedList<>();
            //for (int i = start; i < msg.length() && msg.charAt(i) != ' '; i=i+j) {
            int i=start;
            while (i < msg.length() && msg.charAt(i) != ' '){
                String userName="";
                for (int j = i; j < msg.length() && msg.charAt(j)!='|'; j++) {
                    userName = userName + msg.charAt(j);
                }
                userNames.add(userName);
                i=i+userName.length()+1;
            }
            String response= MessageImpl.stat(userNames,user);
            connections.send(connectionId, response);
        }

        if(messageOp.equals("12")) {
            start = 3;
            String userName = "";
            for (int i = start; i < msg.length() && msg.charAt(i) != ' '; i++) {
                userName = userName + msg.charAt(i);
            }
            String response = MessageImpl.block(userName,user);
            connections.send(connectionId, response);
        }
    }

        @Override
        public boolean shouldTerminate() {
            return false;
        }
    }