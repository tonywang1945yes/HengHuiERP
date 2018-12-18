package backend.dao.impl;

import backend.dao.service.BasicDatabaseService;
import backend.enums.resultMessage.DatabaseRM;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HibernateDao<T> implements BasicDatabaseService<T> {
    T type;
    private SessionFactory sessionFactory;
    private Session session = null;

    public HibernateDao(T t) {
        type = t;
        sessionFactory = new Configuration().configure().addPackage("../../entity").addAnnotatedClass(type.getClass()).buildSessionFactory();
    }


    public synchronized DatabaseRM add(T t0) {
        DatabaseRM res=DatabaseRM.SUCCESS;
        String s = "";
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
            res=DatabaseRM.ROLL_BACK;
        } catch (PersistenceException e) {        //数据库中已有此主键
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("主键已经存在");
            res=DatabaseRM.KEY_EXITS;

        } finally {
            session.close();
        }
        return res;
    }


    public synchronized DatabaseRM delete(String keyValue) {

        DatabaseRM res = DatabaseRM.SUCCESS;
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(session.get(type.getClass(), keyValue));
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
                System.out.println("删除失败,发生回滚");
            }
            res = DatabaseRM.ROLL_BACK;
        } finally {
            session.close();
        }
        return res;
    }

    public synchronized DatabaseRM update(Object t0) {
        DatabaseRM res = DatabaseRM.SUCCESS;
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(t0);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
                System.out.println("更新失败");

            }
            res = DatabaseRM.ROLL_BACK;

        } finally {
            session.close();
        }
        return res;
    }

    public synchronized T findByKey(String keyValue) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        T po = null;
        try {
            tx = session.beginTransaction();
            po = (T) session.get(type.getClass(), keyValue);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
                System.out.println("获取失败");

            }

        } finally {
            session.close();
        }
        return po;
    }

    public synchronized  boolean checkKeyExists(String keyValue) {
        if(findByKey(keyValue)==null) {
            return false;
        }else{
            return true;
        }
    }

    public synchronized ArrayList<T> getAllObjects() {
        session=sessionFactory.openSession();
        Transaction tx=null;
        List<T> list=new ArrayList<T>();//初始化
        try {
            tx=session.beginTransaction();
            String s0="FROM "+type.getClass().getName();
            list=session.createQuery(s0).list();
            tx.commit();
        }catch(HibernateException e){
            if(tx!=null){
                tx.rollback();
                System.out.println("新增失败");

            }

        }finally {
            session.close();
        }
        return (ArrayList<T>) list;
    }

    @Override
    public ArrayList<T> executeQuerySql(String sqlString) {
        session=sessionFactory.openSession();
        Transaction tx=null;
        List<T> list=new ArrayList<T>();//初始化
        try {
            tx=session.beginTransaction();

            list=session.createQuery(sqlString).list();

            tx.commit();
        }catch(HibernateException e){
            if(tx!=null){
                tx.rollback();
                System.out.println("新增失败");

            }

        }finally {
            session.close();
        }
        return (ArrayList<T>) list;
    }
}
