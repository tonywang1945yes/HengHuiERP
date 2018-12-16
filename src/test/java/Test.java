import backend.dao.impl.HibernateDao;
import backend.entity.User;

public class Test {
    public static void main(String[] args) {
        System.out.println("hello world");
        HibernateDao<User> dao1=new HibernateDao<User>(new User(0,")"));
        User u=new User(2,"wrh");
        dao1.add(u);
    }
}
