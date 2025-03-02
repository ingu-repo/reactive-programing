package ingu.flux.service;

import ingu.flux.common.Util;
import ingu.flux.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static final int MAX_SHOW_CNT = 3;
    public static final int MAX_USER_CNT = 100;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final DBService dbService;
    public UserService() {
        this.dbService = new DBService();
    }
    /**
     * Create Flux from List / Stream / Array
     *  - fromIterable(List)
     *  - fromStream(List.stream())
     *  - fromArray
     */
    public Flux<User> getUsers(String tableName) {
        List<User> users = getDBUsers(tableName);
        Flux<User> flux = Flux.fromIterable(users);
        // or Flux<User> flux = Flux.fromStream(users.stream());
        return flux;
    }
    private List<User> getDBUsers(String tableName) {
        List<User> users = new ArrayList<>();
        String sql = "select * from " + tableName;
        ResultSet rs = this.dbService.execQuery(sql);
        try {
            while(rs.next()) {
                User user = this.setUserInfo(rs);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public List<User> getUsersList(String tableName) {
        List<User> users = getDBUsersList(tableName);
        return users;
    }
    private List<User> getDBUsersList(String tableName) {
        List<User> users = new ArrayList<>();
        String sql = "select * from " + tableName;
        ResultSet rs = this.dbService.execQuery(sql);
        try {
            while(rs.next()) {
                User user = this.setUserInfo(rs);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public User setUserInfo(ResultSet rs) {
        try {
            int id = rs.getInt(User.ID);
            String name = rs.getString(User.NAME);
            String lastName = rs.getString(User.LAST_NAME);
            String country = rs.getString(User.COUNTRY);
            Date birthday = rs.getDate (User.BIRTHDAY);
            return new User(id, name, lastName, country, birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private User setUserInfoByFaker() {
        try {
            int id = -1;
            String name = Util.faker().name().firstName();
            String lastName = Util.faker().name().lastName();
            String country = Util.faker().country().name();
            java.util.Date birthday = Util.faker().date().birthday();
            return new User(id, name, lastName, country, birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean truncateTable(String tableName) {
        if (! dbService.execCmd("truncate table " + tableName)) {
            log.error("truncate failed");
            return false;
        } else {
            log.info("truncated table: {}", tableName);
        }
        return true;
    }
    public boolean setUsers(String tableName, int maxCnt) {
        // TRUNCATE
        this.truncateTable(tableName);

        // LOOP COUNT
        for (int i=0; i<maxCnt; i++) {
            User user = setUserInfoByFaker();
            String iSql = insertUser(user, tableName, i);

            if (iSql == null) {
                log.error("failed to insert");
                return false;
            } else if (i < UserService.MAX_SHOW_CNT) {
                log.info(iSql);
            }
        }
        return true;
    }
    public boolean setUsers(Flux<User> fluxUsers, String tableName) {
        // TRUNCATE
        this.truncateTable(tableName);

        // LOOP FLUX
        fluxUsers
//            .take(3)
            .handle((user, sink) -> {
                String iSql = insertUser(user, tableName, user.getId());
//                log.info(iSql);
            })
            .subscribe(Util.subscriber());
        return true;
    }
    private String insertUser(User user, String tableName, int user_id) {
        // INSERT
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        int id = user_id;
        String name = user.getName();
        String lastName = user.getLastName();
        String country = user.getCountry();
        java.util.Date birthday = user.getBirthday();
        String sDate = sdf.format(birthday);

        name = name.replace("'", "''");
        lastName = lastName.replace("'", "''");
        country = country.replace("'", "''");
        country = String.format("%.50s", country);

        String iSql = String.format("insert %s select %d, '%s', '%s', '%s', '%s'"
            , tableName, id, name, lastName, country, sDate);

        if (! dbService.execCmd(iSql)) {
            return null;
        }
        return iSql;
    }

}
