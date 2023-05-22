package persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// TODO rename
public class CEntityManagerFactory {
    private static EntityManagerFactory entityManagerFactory;

    public static void initialization(){
        entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
    }

    public static EntityManager createEntityManger(){
        return entityManagerFactory.createEntityManager();
    }

    public static void close(){
        entityManagerFactory.close();
    }
}