package bgu.spl.net.srv;

import bgu.spl.net.api.Connections;
import bgu.spl.net.impl.rci.PM;
import bgu.spl.net.impl.rci.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageImpl {
   // private static ConcurrentHashMap<String, String> usePas;//
   public static MessageImpl instance = new MessageImpl();
    private static ConcurrentHashMap<String, Boolean> useLog;//userName loggedIn
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> useFollowers;//userName and followers
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> useFollowing;// userName and following
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> userPosts;
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> userSeenPosts;
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> blockUsers;
    private static ConcurrentHashMap<String, User> userNames;
    private static ConcurrentLinkedQueue<String> filteredWords;
    private static Connections connections;
  //  private static Vector<User> users;
  public static <T>MessageImpl getInstance() {
      return instance;
  }

    public MessageImpl() {
      //  usePas = new ConcurrentHashMap<>();
        useLog = new ConcurrentHashMap<>();
        useFollowers = new ConcurrentHashMap<>();
        useFollowing = new ConcurrentHashMap<>();
        userPosts = new ConcurrentHashMap<>();
        userSeenPosts = new ConcurrentHashMap<>();
        userNames=new ConcurrentHashMap<>();
        blockUsers=new ConcurrentHashMap<>();
        Collection<String> listOfFiltered= Arrays.asList("terror","bibi","bitch");
        filteredWords=new ConcurrentLinkedQueue<>( listOfFiltered);
        connections= ConnectionsImp.getInstance();
       // users = new Vector<>();
    }

    public static String register(String userName, String password, String birthday, User user, int conid) {


        if (userNames!=null&&userNames.containsKey(userName))
            return "11 1";
        if(birthday.length()==0||password.length()==0||userName.length()==0)
            return "11 1";
         else {
            User newUse=new User();
            newUse.setUserName(userName);
            newUse.setPassword(password);
            newUse.setBirthday(birthday);
            newUse.setIsRegister();
            //newUse.setId(conid);
            userNames.put(userName,newUse);
            //userNames.put(userName,user);
            useLog.put(userName, false);
            useFollowers.put(userName, new ConcurrentLinkedQueue<String>());
            useFollowing.put(userName, new ConcurrentLinkedQueue<String>());
            userPosts.put(userName, new ConcurrentLinkedQueue<>());
            userSeenPosts.put(userName, new ConcurrentLinkedQueue<>());
            blockUsers.put(userName,new ConcurrentLinkedQueue<>());
            return "10 1";
        }
    }

    public static String logIn(String userName, String password, byte captcha, User user,Connections con,int conId) {
//        System.out.println("insidelogin");
//        System.out.println("CONTAINS USERNAME");
//        System.out.println(userNames.containsKey(userName));
//        System.out.println("PASSWORD IS");
//       System.out.println((userNames.get(userName).getPassword().equals(password) ));
//        System.out.println("KEEP PASSWORD");
//        System.out.println(userNames.get(userName).getPassword());
//        System.out.println((userNames.get(userName).getPassword() ));
        //System.out.println(password);
        // System.out.println(captcha == 1);
        if (user != null && user.getLoggedIn() && user.getUserName().equals(userName)) {
            return "11 2";
        }
//        if (userNames.containsKey(userName)) {
//            user.setUser(userNames.get(userName));
//            user.setId(conId);
//            // user.setLoggedIn();
//        }
        if (userNames.containsKey(userName) && (userNames.get(userName).getPassword().equals(password)) && (captcha == 1)) {
            user.setUser(userNames.get(userName));
            user.setId(conId);
            useLog.replace(userName, true);
            user.setLoggedIn(true);
            userNames.get(userName).setLoggedIn(true);
            userNames.get(userName).setId(conId);
            while (user.getNotifications().size() > 0) {
                String content = (String) user.getNotifications().poll();
                connections.send(user.getId(), content);
            }
            return "10 2";
        } else {
            return "11 2";
        }
    }

    public static String logOut(User user) {
        if (user != null) {
            if (user.getLoggedIn()) {
                user.setLoggedIn(false);
                useLog.replace(user.getUserName(), false);
                return "10 3";
            } else//user is not loggedin
                return "11 3";
        } else {
            return "11 3";
        }
    }

    public static String follow(String userName, byte folUn, User user) {
        // if (user != null) {
        if (user == null)
            return "11 4";
        String myUserName = user.getUserName();
        if (!userNames.containsKey(userName))
            return "11 4";
        if(!userNames.containsKey(user.getUserName()))
            return "11 4";
//        if(useFollowing.get(user.getUserName()).contains(userName))
//            return "11 4";
        if(user.getUserName().equals(userName))
            return "11 4";
        if (folUn == 0) {
            if (blockUsers.get(user.getUserName()) != null && blockUsers.get(user.getUserName()).contains(userName)) {
                return "11 4";
            }
            if (blockUsers.get(user.getUserName()) != null && blockUsers.get(userName).contains(user.getUserName())) {
                return "11 4";
            }
            //System.out.println("contain"+!(useFollowing.get(myUserName).contains(userName)));
            if (useFollowing.get(user.getUserName()) != null && !(useFollowing.get(myUserName).contains(userName))) {
                useFollowing.get(myUserName).add(userName);
                useFollowers.get(userName).add(myUserName);
                String result = "10 4 " + userName;//need to check!!
                return result;
            } else {
                return "11 4";
            }
        } else {//folun=1, user want to unfollow userName
            if (useFollowing.get(user.getUserName()) != null && useFollowing.get(myUserName).contains(userName)) {//USER FOLLOW USERNAME
                useFollowing.get(myUserName).remove(userName);
                useFollowers.get(userName).remove(myUserName);
                String result = "10 4 " + userName;//need to check!!
                return result;
            } else {
                return "11 4";
            }
        }
    }
//    public static String post(String content, User user) {
//
//        if (user != null) {
//            if (usePas.containsKey(user.getUserName())) {
//                if (!user.getLoggedIn()) {
//                    return "11 5";
//                } else {
//                    int start;
//                    int end;
//                    for (int i = 0; i < content.length() & content.charAt(i) != '@'; i++) {
//                        if (content.charAt(i) == '@') {
//                            start = i + 1;
//                            for (int j = i; j < content.length() & content.charAt(j) != ' '; j++) {
//                                if (content.charAt(j) != ' ' || content.charAt(j) != '\n') {
//                                    end = j;
//                                    String currContent = content;
//                                    String userName = currContent.substring(start, end);
//                                    if (usePas.containsKey(userName)) {
//                                        if (!(useFollowers.get(user.getUserName()).contains(userName))) {
//                                            userSeenPosts.get(userName).add(content);
//                                        }
//                                        break;
//                                    }
//
//
//                                }
//                            }
//
//
//                        }
//                    }
//
//                }
//                for (String userN : useFollowers.get(user.getUserName())) {
//                    userSeenPosts.get(userN).add(content);
//                }
//
//                userPosts.get(user.getUserName()).add(content);
//            }
//            return "10 5";
//        } else
//            return "11 5";
//    }

    public static void post(String content, User user,Connections con, int id) {
        if (user == null) {
            con.send(id, "11 6");
        }
        //return "11 5";
        if (!(userNames.containsKey(user.getUserName())) || !user.getLoggedIn()) {
            con.send(id, "11 6");
        }
        //  return "11 5";
        String[] splitStr = content.split(" ");
        List<String> taggedUseres = new LinkedList<>();
        for (int i=0;i<splitStr.length;i++) {
            if (splitStr[i].charAt(0) == '@') {
                String userName = splitStr[i].substring(1);
                if (userNames.containsKey(userName)) {//username is register
                    if (!(useFollowers.get(user.getUserName()).contains(userName)) && !(blockUsers.get(user.getUserName()).contains(userName))
                            && !(blockUsers.get(userName).contains(user.getUserName()))) {
                        taggedUseres.add(userName);
                    }
                }
            } else {//word is not a userName, may have to be filtered
                if (filteredWords.contains(splitStr[i])) {
                    splitStr[i] = "<filtered>";
                }
            }
        }
        String filteredContent = "";
        for (int i = 0; i < splitStr.length; i++) {
//            if (i > 0 && i < splitStr.length - 1)
//                filteredContent = filteredContent + " " + splitStr[i];
//            else
                filteredContent = filteredContent + splitStr[i];
        }
        con.send(id, "10 6");
        String notification = "9 1 " + user.getUserName() + " " + filteredContent;
        for (String userN : taggedUseres) {
            int idnotify = userNames.get(userN).getId();
            userSeenPosts.get(userN).add(filteredContent);//adding filtered post to all tagged users
            if (userNames.get(userN).getLoggedIn()|| useLog.get(userN)==true)
                con.send(idnotify, notification);
            else
                userNames.get(userN).addNotification(notification);
        }

        for (String userN : useFollowers.get(user.getUserName())) {
            int idnotify = userNames.get(userN).getId();
            userSeenPosts.get(userN).add(filteredContent);
            if (userNames.get(userN).getLoggedIn())
                con.send(idnotify, notification);
            else
                userNames.get(userN).addNotification(notification);
            userPosts.get(user.getUserName()).add(filteredContent);//adding filtered post to all writer's post followers
            user.setNumPosts();
        }
    }
    public static void pm(String userName, String content, String dateTime, User user, Connections con, int id) {
        if (user == null) {
            con.send(id, "11 6");
        } else if (!(userNames.containsKey(user.getUserName())) || !user.getLoggedIn())
            // return "11 6";
            con.send(id, "11 6");
        else if (!(userNames.containsKey(userName))) {
            // return "11 6";
            con.send(id, "11 6");
        } else if (!(useFollowing.get(user.getUserName()).contains(userName))) {
            // return "11 6";
            con.send(id, "11 6");
        } else if (blockUsers.get(user.getUserName()).contains(userName)) {
            //return "11 4";
            con.send(id, "11 6");
        } else {
            String[] splitStr = content.split(" ");
//            System.out.println(splitStr[1]);
//            for (String word : splitStr) {
//                if (filteredWords.contains(word)) {
//                    System.out.println("inside contains filt");
//                    word = "<filtered>";
//                }
//            }
            for (int i = 0; i < splitStr.length; i++) {
                if (filteredWords.contains(splitStr[i])) {
                    splitStr[i] = "<filtered>";
                }
            }

            String filteredContent = "";
            for (int i = 0; i < splitStr.length; i++) {
//                if (i > 0 && i < splitStr.length - 1)
                filteredContent = filteredContent + " " + splitStr[i];
//                else
//                    filteredContent = filteredContent + splitStr[i];
            }
            PM pm = new PM(filteredContent, dateTime);
            userPosts.get(user.getUserName()).add(pm);//adding pm to the sender posts
            //send notification
            userSeenPosts.get(userName).add(pm);//adding pm to the user that recieve it
            con.send(id, "10 6");
            String notification = "9 0 " + user.getUserName() + " " + filteredContent + " " + dateTime;
            if (userNames.get(userName).getLoggedIn()|| useLog.get(userName)==true) {
                int idnotify = userNames.get(userName).getId();
                con.send(idnotify, notification);
            } else
                userNames.get(userName).addNotification(notification);
        }
    }


//    public static String logStat(User user) {
//        if (user != null) {
//            if (!(user.getIsRegister())) {
//                return "11 7";
//            } else if (!(user.getLoggedIn())) {
//                return "11 7";
//            } else {
//                String tosend = "10 7";
//                for (int i = 0; i < userNames.size(); i++) {
//                    User use = userNames.get(i);
//                    if (use.getIsRegister()) {
//                        if (use.getLoggedIn()) {
//                            tosend = tosend + " <" + use.getAge() + ">" + "<" + use.getNumPosts() + ">" + useFollowers.get(use.getUserName()).size() + "'<" + useFollowing.get(use.getUserName()).size() + ">" + " ";//change to the length of followers
//                        }
//
//                    }
//                }
//                return tosend;
//            }
//
//
//        } else
//            return "11 7";
//    }
public static String logStat(User user) {
    if (user == null)
        return "11 7";
    else {
        String tosend = "10 7";
        tosend = tosend + " " + user.getAge()  + " " + user.getNumPosts() + " " + useFollowers.get(user.getUserName()).size() + " " + useFollowing.get(user.getUserName()).size();
        return tosend;
    }
//            } else if (!(user.getLoggedIn())) {
//                return "11 7";
//            } else {
//                String tosend = "10 7";
//                for (int i = 0; i < userNames.size(); i++) {
//                    User use = userNames.get(i);
//                    if (use.getIsRegister()) {
//                        if (use.getLoggedIn()) {
//                            tosend = tosend + " <" + use.getAge() + ">" + "<" + use.getNumPosts() + ">" + useFollowers.get(use.getUserName()).size() + "'<" + useFollowing.get(use.getUserName()).size() + ">" + " ";//change to the length of followers
//                        }
//
//                    }
//                }
//                return tosend;
//            }
//
//
//        } else
//            return "11 7";
//    }
}
    public static String stat(List<String> statUsers, User user) {
      if(user==null)
          return "11 8";;
        String tosend = "10 8";
        for (int i = 0; i < statUsers.size(); i++) {//find all the users with the userNames from the list
                      if(userNames.containsKey(statUsers.get(i))){
                          User use=userNames.get(statUsers.get(i));
                            tosend = tosend + " <" + use.getAge() + "> <" +use.getNumPosts() + "> <" +  useFollowers.get( use.getUserName()).size() +"> <" + useFollowing.get(use.getUserName()).size()+">" ;
                        }
                    }
                return tosend;
                }
        //    }

  //  }


//    public static String stat(List<String> statUsers, User user) {
//        if (user != null) {
//            if (!(user.getIsRegister())) {
//                return "11 8";
//            } else if (!(user.getLoggedIn())) {
//                return "11 8";
//            } else {
//                String tosend = "10 8";
//                for (int i = 0; i < statUsers.size(); i++) {//find all the users with the userNames from the list
//                      if(userNames.containsKey(statUsers.get(i))){
//                          User use=userNames.get(statUsers.get(i));
//                            tosend = tosend + " <" + use.getAge() + ">" + "<" +use.getNumPosts() + ">" + "<" +  useFollowers.get( use.getUserName()).size() + ">" + useFollowing.get(use.getUserName()).size() + " ";
//                        }
//                    }
//                return tosend;
//                }
//            }
//        else {
//            return "11 7";
//        }
//    }

    public static String block(String userName, User user){
        if(user==null)
            return "11 12";
        if (!userNames.containsKey(userName))
            return "11 12";
        if (blockUsers.get(user.getUserName()).contains(userName))
            return "11 12";
        blockUsers.get(user.getUserName()).add(userName);
        if (useFollowing.get(user.getUserName()).contains(userName))
            useFollowing.get(user.getUserName()).remove(userName);
        if(useFollowers.get(user.getUserName()).contains(userName))
            useFollowers.get(user.getUserName()).remove(userName);
        return "10 12";
    }
}
