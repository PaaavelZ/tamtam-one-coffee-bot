package one.coffee.sql.tables;

import one.coffee.sql.entities.User;
import one.coffee.sql.entities.UserConnection;
import one.coffee.sql.entities.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserConnectionsTableTest extends TableTest {

    @Test
    void ok() {
        final long user1Id = 123;
        final String user1City = "St. Petersburg";
        final UserState user1State = UserState.DEFAULT;
        UserConnection userConnection = null;
        User user1 = new User(user1Id, user1City, user1State, userConnection);

        final long user2Id = 124;
        final String user2City = "St. Petersburg";
        final UserState user2State = UserState.DEFAULT;
        User user2 = new User(user2Id, user2City, user2State, userConnection);

        userConnection = new UserConnection(user1, user2);

        UserConnection savedUserConnection = UserConnectionsTable.getUserConnectionsByUserId(user1Id).get(0);
        assertEquals(savedUserConnection.getUser1(), user1);
        assertEquals(savedUserConnection.getUser2(), user2);
        assertEquals(savedUserConnection.getId(), userConnection.getId());
    }

    @Test
    void twoParallelConnections() {
        final long user1Id = 123;
        final String user1City = "St. Petersburg";
        final UserState user1State = UserState.DEFAULT;
        UserConnection user1Connection = null;
        User user1 = new User(user1Id, user1City, user1State, user1Connection);

        final long user2Id = 124;
        final String user2City = "St. Petersburg";
        final UserState user2State = UserState.DEFAULT;
        UserConnection user2Connection = null;
        User user2 = new User(user2Id, user2City, user2State, user2Connection);

        final long user3Id = 125;
        final String user3City = "St. Petersburg";
        final UserState user3State = UserState.DEFAULT;
        UserConnection user3Connection = null;
        User user3 = new User(user3Id, user3City, user3State, user3Connection);

        user1Connection = new UserConnection(user1, user2);

        assertThrows(Exception.class, () -> new UserConnection(user2, user3));

        UserConnection savedUserConnection = UserConnectionsTable.getUserConnectionsByUserId(user1Id).get(0);
        assertEquals(savedUserConnection.getId(), user1Connection.getId());
        assertEquals(savedUserConnection.getUser1(), user1);
        assertEquals(savedUserConnection.getUser2(), user2);
    }

    @Test
    void breakConnection1() {
        final long user1Id = 123;
        final String user1City = "St. Petersburg";
        final UserState user1State = UserState.DEFAULT;
        UserConnection userConnection = null;
        User user1 = new User(user1Id, user1City, user1State, userConnection);

        final long user2Id = 124;
        final String user2City = "St. Petersburg";
        final UserState user2State = UserState.DEFAULT;
        User user2 = new User(user2Id, user2City, user2State, userConnection);

        userConnection = new UserConnection(user1, user2);
        userConnection.breakConnection();

        assertEquals(user1.getUserConnection(), null);
        assertEquals(user2.getUserConnection(), null);

        assertEquals(user1.getState(), UserState.DEFAULT);
        assertEquals(user2.getState(), UserState.DEFAULT);
    }

    @Test
    void breakConnection2() {
        final long user1Id = 123;
        final String user1City = "St. Petersburg";
        final UserState user1State = UserState.DEFAULT;
        UserConnection userConnection = null;
        User user1 = new User(user1Id, user1City, user1State, userConnection);

        final long user2Id = 124;
        final String user2City = "St. Petersburg";
        final UserState user2State = UserState.DEFAULT;
        User user2 = new User(user2Id, user2City, user2State, userConnection);

        userConnection = new UserConnection(user1, user2);
        assertThrows(Exception.class, userConnection::breakConnection);
    }

    @Override
    protected Table getTable() {
        return UserConnectionsTable.INSTANCE;
    }

}
