package backend.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class HibernateDao<T> {
    T type;
    private SessionFactory sessionFactory;
    private Session session = null;

    public HibernateDao(T t) {
        type = t;
        sessionFactory = new Configuration().configure().addPackage("../../entity").addAnnotatedClass(type.getClass()).buildSessionFactory();
    }


    public synchronized boolean add(T t0) {

        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.save(t0);

            tx.commit();

        } catch (HibernateException e) {
            if (tx != null) {
                System.out.println("新增失败，发生回滚");
                tx.rollback();

                e.printStackTrace();

            }

        } catch (PersistenceException e) {        //数据库中已有此主键
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("主键已经存在");
            return false;

        } finally {
            session.close();
        }
        return true;
    }




}
