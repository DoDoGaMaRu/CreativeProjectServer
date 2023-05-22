package persistence.dao;

import javax.persistence.EntityManager;

public interface Executable<T> {
    T exec(EntityManager em) throws Exception;
}
