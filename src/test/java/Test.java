import backend.dao.impl.HibernateDao;
import backend.entity.User;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        System.out.println("hello world");
        HibernateDao<User> dao1=new HibernateDao<User>(new User(0,")"));
        ArrayList<User> userlist=dao1.executeQuerySql("select u from User u where u.id<5");
        for(User usr:userlist){
            System.out.println(usr.getUsername());
        }

    }
}
