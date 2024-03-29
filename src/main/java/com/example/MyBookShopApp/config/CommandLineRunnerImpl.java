package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.TestEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Transient;

@Configuration
public class CommandLineRunnerImpl  implements CommandLineRunner {

    EntityManagerFactory entityManagerFactory;
    @Autowired
    public CommandLineRunnerImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        for(int i=0; i < 5 ; i++){
            createTestEntity(new TestEntity());
        }
    }

    private void createTestEntity(TestEntity entity) {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            entity.setData(entity.getClass().getSimpleName()+entity.hashCode());
            session.save(entity);
            tx.commit();
        } catch (HibernateException ex){
            if(tx != null){
                tx.rollback();
            }else {
                ex.printStackTrace();
            }
        }
        finally {
            session.close();
        }
    }
}
