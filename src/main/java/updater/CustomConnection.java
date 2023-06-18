package updater;

import persistence.dao.Executable;

public interface CustomConnection {
    Object execQuery(Executable<Object> query);
    void initConnection();
    void closeConnection();
}
