package bgu.spl.net.impl.rci;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private boolean isRegister;
    private String password;
    private boolean loggedIn;
    private String birthday;
    private String userName;
    private short age;
    private short numPosts;
    private ConcurrentLinkedQueue<String> notifications;
//    private short numFollowings;
//    private short numFollowers;
    private int id;


    public User() {
        isRegister = false;
        password = null;
        birthday = null;
        loggedIn = false;
        userName = null;
        age = 0;
        numPosts = 0;
        id=-1;
        notifications=new ConcurrentLinkedQueue<String>();
//        numFollowings = 0;
//        numFollowers = 0;

    }

    public String getPassword() {
        return password;
    }

    public short getAge() {
        return age;
    }

    public short getNumPosts() {
        return numPosts;
    }


    public String getBirthday() {
        return birthday;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setLoggedIn(Boolean b) {
        loggedIn = b;
    }

    public void setUserName(String _userName) {
        userName = _userName;
    }

    public void setPassword(String _password) {
        password = _password;
    }

    public void setBirthday(String _birthday) {
        birthday = _birthday;
        String year = "";
        for (int i = _birthday.length() - 1; i > 0 && _birthday.charAt(i) != '-'; i--) {
            year = _birthday.charAt(i) + year;
        }
        if (year.length()==0)
            age=-1;
        for (int i = 0; i < year.length(); i++) {
            if (!(year.charAt(i) >= '0' && year.charAt(i) <= '9')) {
                age = -1;
                break;
            }
        }
        if (age != -1) {
            age = (short) (2022 - Integer.parseInt(year));
        }
    }
    public void setNumPosts() {
        numPosts++;
    }

//    public void setNumFollowings(boolean b) {
//        if (b) {
//            numFollowings++;
//        } else {
//            numFollowings--;
//        }
//    }

    public boolean getIsRegister() {
        return isRegister;
    }
    public void setIsRegister() {
        isRegister = true;
    }
    public void setNotification(ConcurrentLinkedQueue<String> not){
        notifications=not;
    }
    public void setId(int _id){
        id=_id;
    }
    public int getId(){
        return id;
    }
    public void addNotification(String s){
        notifications.add(s);
    }
   public ConcurrentLinkedQueue getNotifications(){
        return notifications;
   }
   public void setUser(User use){
        this.setUserName(use.getUserName());
       this.setPassword(use.getPassword());
       this.setBirthday(use.getBirthday());
       this.setNotification(use.getNotifications());
       numPosts=use.getNumPosts();
        id=use.getId();
       isRegister=use.getIsRegister();

   }

//    public short getNumFollowers() {
//        return numFollowers;
//    }
//
//    public void setNumFollowers(boolean b) {
//        if (b) {
//            numFollowers++;
//        } else {
//            numFollowers--;
//        }
//    }
}