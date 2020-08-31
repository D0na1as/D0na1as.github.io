import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {

    private HashMap<String, ArrayList<String>> table;
    private ArrayList<String> dates;
    private ArrayList<String> slotList;
    private String specialist_name;
    private int specialist_number;
    private String client;
    private String currentClient;
    private String clientDate;
    private String clientTime;
    private String clientStartTime;
    private String clientEndTime;

    public Schedule() {
        this.table = new HashMap<>();
        this.dates = new ArrayList<>();
        this.slotList = new ArrayList<>();
        this.client = "0";
        this.currentClient = "";
    }

    public HashMap<String, ArrayList<String>> getTable() {
        return table;
    }

    public String getSpecialist() {
        return specialist_name;
    }

    public ArrayList<String> getDates() {
        return dates;
    }
    public String getDate(int x) {
            return dates.get(x);
        }

    public ArrayList<String> getSlotList() {
        return slotList;
    }

    public int getNumber() {
        return specialist_number;
    }

    public int getSlotCount() {
        return slotList.size();
    }

    public int getDaysCount() {
        return dates.size();
    }

    public String getData (String day, String Slot){
        ArrayList<String> list = table.get(day);
        int x = Integer.parseInt(Slot);
        return list.get(x-1);
    }

    public void addDate(String date) {
        this.dates.add(date);
    }
    public void addTable(String day, ArrayList<String> data) {
        this.table.put(day, data);
    }

    public void addSlot(String slot) {
        this.slotList.add(slot);
    }

    public void addSpecialist(String specialist_name) {
        this.specialist_name = specialist_name;
    }

    public void setNumber(int specialist_number) {
        this.specialist_number = specialist_number;
    }
    
    //Clients getters and setters
    
    public String getClient() {
        return client;
    }

    public String getClientDate() {
        return clientDate;
    }

    public void setClientDate(String clientDate) {
        this.clientDate = clientDate;
    }
    public void setClient(String client) {
        this.client = client;
    }

    public String getClientTime() {
        return clientTime;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    public String getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(String currentClient) {
        this.currentClient = currentClient;
    }
}
