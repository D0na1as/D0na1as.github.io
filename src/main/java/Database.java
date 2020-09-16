import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private final BasicDataSource dataSource;

    public Database(String databaseName) {

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("b394f371a160e8");
        dataSource.setPassword("6b2c6686");
        dataSource.setUrl("jdbc:mysql://eu-cdbr-west-03.cleardb.net:3306/" + databaseName + "?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setValidationQuery("SELECT 1");
    }

    public void registerClient(String name, String date, String slot, String client_id) {

        String query1 = "UPDATE time_table SET " + slot + "='" + client_id + "' WHERE date=? AND specialist_name=?;";

        slot = slot.split("_")[1];
        String query2 = "INSERT INTO `clients` (`date`, `client_id`, `time_slot`, `specialist_name`) VALUES (?,?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2)) {

            statement1.setString(1, date);
            statement1.setString(2, name);
            statement2.setString(1, date);
            statement2.setString(2, client_id);
            statement2.setString(3, slot);
            statement2.setString(4, name);
            statement1.executeUpdate();
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSpecialistsList() {

        String query = "SELECT specialist_name FROM specialists";
        ArrayList<String> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("specialist_name"));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getClient(String client_id) {

        String query = "SELECT * FROM clients WHERE client_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
             statement.setString(1, client_id);
             ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                HashMap<String, String> list = new HashMap<>();
                list.put("day", resultSet.getString("date"));
                list.put("client_id", resultSet.getString("client_id"));
                list.put("que_place", resultSet.getString("time_slot"));
                list.put("status", resultSet.getString("status"));
                list.put("name", resultSet.getString("specialist_name"));

                return list;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getDates(String name) {

        String query = "SELECT date From time_table WHERE  date>=NOW() and (slot_1='empty' or slot_2='empty' or slot_3='empty' " +
                "or slot_4='empty' or slot_5='empty' or slot_6='empty' or slot_7='empty' or slot_8='empty'" +
                "or slot_9='empty' or slot_10='empty' or slot_11='empty' or slot_12='empty' or slot_13='empty'" +
                "or slot_14='empty' or slot_15='empty' or slot_16='empty') and specialist_name=? order BY date asc;";

        ArrayList<String> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("date"));
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getSlots(String name, String day) {

        String query = " SELECT slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, slot_7, slot_8, slot_9, " +
                "slot_10, slot_11, slot_12, slot_13, slot_14, slot_15, slot_16 FROM time_table where date=? " +
                "and specialist_name=?";

        ArrayList<String> list = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, day);
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                for (int i = 1; i < 17; i++) {
                    if (resultSet.getString("slot_" + i).equals("empty")) {
                        list.add("slot_" + i);
                    }
                }
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startMeeting(String time, String client_id, String number) {

        String query1 = "UPDATE clients SET start_time=? where client_id=?;";
        String query2 = "UPDATE specialists SET current_client=? where number=?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2)) {

            statement1.setString(1, time);
            statement1.setString(2, client_id);
            statement2.setString(1, client_id);
            statement2.setString(2, number);
            statement1.executeUpdate();
            statement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endMeeting(String time, String client_id, String name, String slot) {

        String query1 = "UPDATE time_table SET " + slot + "='occurred' where specialist_name=? and date=(select date from clients where client_id=?);";
        String query2 = "UPDATE clients SET end_time='" + time + "', status='occurred' where client_id=?;";
        String query3 = "UPDATE specialists SET current_client='0' where specialist_name=?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(query1);
             PreparedStatement statement2 = connection.prepareStatement(query2);
             PreparedStatement statement3 = connection.prepareStatement(query3)) {

            statement1.setString(1, name);
            statement1.setString(2, client_id);
            statement2.setString(1, client_id);
            statement3.setString(1, name);

            statement1.executeUpdate();
            statement2.executeUpdate();
            statement3.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelBooking(String client_id) {

        String query1 = "SELECT time_slot from `clients` where client_id=?;";
        String slot = "slot_";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query1)) {

            statement.setString(1, client_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                slot = slot + resultSet.getString("time_slot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query2 = "UPDATE time_table SET " + slot + "='empty' WHERE date=(Select date from `clients` " +
                "where client_id=?) and specialist_name=(Select specialist_name from `clients` " +
                "where client_id=?);";
        String query3 = "UPDATE clients SET status='canceled' where client_id=?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement2 = connection.prepareStatement(query2);
             PreparedStatement statement3 = connection.prepareStatement(query3)) {

            statement2.setString(1, client_id);
            statement2.setString(2, client_id);
            statement3.setString(1, client_id);
            statement2.executeUpdate();
            statement3.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Schedule getSpecialist(String number, String client_id) {

        String query = "select specialist_name, current_client from specialists where number=?;";

        Schedule schedule = new Schedule();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                schedule.addSpecialist(resultSet.getString("specialist_name"));
                schedule.setClient(resultSet.getString("current_client"));

            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        schedule.setNumber(Integer.parseInt(number));

        if (client_id != null) {

            query = "select date, time_slot from clients where client_id=?;";
            schedule.setCurrentClient(client_id);

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, client_id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    schedule.setClientDate(resultSet.getString("date"));
                    schedule.setClientTime(resultSet.getString("time_slot"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (!schedule.getClient().equals("0")) {

            query = "select date, time_slot from clients where client_id=?;";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, schedule.getClient());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    schedule.setClientDate(resultSet.getString("date"));
                    schedule.setClientTime(resultSet.getString("time_slot"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            query = "select client_id, date, time_slot from clients where status='booked' and specialist_name=? and date>=CURDATE() order BY date asc limit 1;";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, schedule.getSpecialist());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    schedule.setCurrentClient(resultSet.getString("client_id"));
                    schedule.setClientDate(resultSet.getString("date"));
                    schedule.setClientTime(resultSet.getString("time_slot"));
                } else {
                    schedule.setCurrentClient("no clients");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        query = "select date, slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, " +
                "slot_7, slot_8, slot_9, slot_10, slot_11, slot_12, slot_13, slot_14, " +
                "slot_15, slot_16 from time_table where specialist_name=? and " +
                "date >= CURDATE() order BY date asc limit 5; ";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, schedule.getSpecialist());
            ResultSet resultSet = statement.executeQuery();
            int counter = 0;
            while (resultSet.next()) {
                schedule.addDate(resultSet.getString("date"));
                ArrayList<String> slotsList = new ArrayList<>();
                for (int i = 1; i < 17; i++) {
                    if (counter == 0) {
                        schedule.addSlot(String.valueOf(i));
                    }
                    slotsList.add(resultSet.getString("slot_" + i));
                }
                schedule.addTable(schedule.getDate(counter), slotsList);
                counter++;
            }
            return schedule;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ScreenData getScreen() {

        String query = "SELECT specialist_name From specialists";

        ArrayList<String> specList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                specList.add(resultSet.getString("specialist_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int count;
        HashMap<String, String> timeList = new HashMap<String, String>();
        Templates temp = new Templates();
        for (String spec : specList) {

            count = getSlot();

            if (count == 16) {
                for (int i = 0; i < 7; i++) {
                    timeList.put(spec + i, "--");
                }
            } else {

                query = "select slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, " +
                        "slot_7, slot_8, slot_9, slot_10, slot_11, slot_12, slot_13, slot_14, " +
                        "slot_15, slot_16 from time_table where date=CURDATE() and " +
                        "specialist_name='" + spec + "';";

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    int line = 0;
                    if (resultSet.next()) {
                        do {
                            for (int i = count; i < 25; i++) {
                                if (count > 16) {
                                    timeList.put(spec + line, "--");
                                    count++;
                                    line++;
                                    if (line == 7) {
                                        break;
                                    }
                                } else {
                                    String slot = resultSet.getString("slot_" + i);
                                    if (slot.equals("empty") || slot.equals("occurred")) {
                                        count++;

                                    } else {

                                        if (count < 10) {
                                            timeList.put(spec + line, "0" + count);
                                        } else {
                                            timeList.put(spec + line, "" + count);
                                        }
                                        count++;
                                        line++;

                                        if (line == 7) {
                                            break;
                                        }
                                    }
                                }
                            }
                        } while (resultSet.next());
                    } else {
                        for (int i = 0; i < 7; i++) {
                            timeList.put(spec + i, "--");
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        ScreenData data = new ScreenData(specList, timeList);
        return data;

    }
    private int getSlot () {

        int slot;

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
         if (hour == 8) {
            if (minute < 30) {
                return slot = 1;
            } else {
                return slot = 2;
            }

        } else if (hour == 9) {
            if (minute < 30) {
                return slot = 3;
            } else {
                return slot = 4;
            }
        } else if (hour == 10) {
            if (minute < 30) {
                return slot = 5;
            } else {
                return slot = 6;
            }
        } else if (hour == 11) {
            if (minute < 30) {
                return slot = 7;
            } else {
                return slot = 8;
            }
        } else if (hour == 12) {
            if (minute < 30) {
                return slot = 9;
            } else {
                return slot = 10;
            }
        } else if (hour == 13) {
            if (minute < 30) {
                return slot = 11;
            } else {
                return slot = 12;
            }
        } else if (hour == 14) {
            if (minute < 30) {
                return slot = 13;
            } else {
                return slot = 14;
            }
        } else if (hour == 15) {
            if (minute < 30) {
                return slot = 15;
            } else {
                return slot = 16;
            }
        } else if ( hour>=16 && hour<=23) {
                return slot = 16;
        } else { return slot = 1;}

    }
}

