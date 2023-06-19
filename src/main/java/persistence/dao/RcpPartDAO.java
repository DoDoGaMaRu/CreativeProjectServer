package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class RcpPartDAO extends DAO<RcpPart, Long> {
    private static RcpPartDAO rcpPartDAO;

    public static RcpPartDAO getInstance() {
        if (rcpPartDAO == null) {
            rcpPartDAO = new RcpPartDAO();
        }
        return rcpPartDAO;
    }

    private RcpPartDAO() {
        super(RcpPart.class);
    }
}