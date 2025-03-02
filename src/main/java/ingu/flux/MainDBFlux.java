package ingu.flux;

import ingu.flux.common.Util;
import ingu.flux.model.User;
import ingu.flux.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * DB Handling
 *  - ResultSet
 *  - Flux
 */
public class MainDBFlux {
    private static final Logger log = LoggerFactory.getLogger(MainDBFlux.class);

    public static void main(String[] args) {
        UserService userService = new UserService();

        createUsers(userService, UserService.MAX_USER_CNT);
        showUsersFromResultSet(userService, "users", UserService.MAX_SHOW_CNT);
        showUsersFromFlux(userService, "users", UserService.MAX_SHOW_CNT);

        createCustomers(userService);
        showUsersFromResultSet(userService, "customers", UserService.MAX_SHOW_CNT);
        showUsersFromFlux(userService, "customers", UserService.MAX_SHOW_CNT);
    }
    public static void showUsersFromResultSet(UserService userService, String tableName, int showCnt) {
        log.info("showing from ResultSet of {}", tableName);
        List<User> users = userService.getUsersList(tableName);
        int i = 0;
        for (var user : users) {
            log.info("{}|{}|{}|{}|{}", user.getId(), user.getName(), user.getLastName(), user.getCountry(), user.getBirthday());
            if (i >= showCnt) break;
            i++;
        }
        log.info("");
    }
    private static void showUsersFromFlux(UserService userService, String tableName, int showCnt) {
        log.info("showing from Flux of {}", tableName);
        Flux<User> fluxUsers = userService.getUsers("users");
        fluxUsers
            .take(showCnt)
            .map(user -> user.getName())
            .subscribe(Util.subscriber());
        log.info("");
    }
    /**
     * create new users into users table
     */
    private static void createUsers(UserService userService, int cnt) {
        String tableName = "users";
        userService.setUsers(tableName, cnt);
    }
    /**
     * insert users to customers table from users data
     */
    private static void createCustomers(UserService userService) {
        String tableName = "customers";
        Flux<User> fluxUsers = userService.getUsers("users");
        userService.setUsers(fluxUsers, tableName);
    }

}
