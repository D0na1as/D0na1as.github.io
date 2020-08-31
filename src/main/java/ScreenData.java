import java.util.ArrayList;
import java.util.HashMap;

public class ScreenData {

    private ArrayList<String> specialists;
    private HashMap<String, String> upComing;
    private ArrayList<String> infoLines;

    public ScreenData(ArrayList<String> specialists, HashMap<String, String> upComing) {
        this.specialists = specialists;
        this.upComing = upComing;
        this.infoLines = new ArrayList<String>();
        infoLines.add("Specialist");
        infoLines.add("Current");
        infoLines.add("Second");
        infoLines.add("Third");
        infoLines.add("Fourth");
        infoLines.add("Fifth");
        infoLines.add("Sixth");
    }

    public ArrayList<String> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(ArrayList<String> specialists) {
        this.specialists = specialists;
    }

    public HashMap<String, String> getUpComing() {
        return upComing;
    }

    public void setUpComing(HashMap<String, String> upComing) {
        this.upComing = upComing;
    }

    public ArrayList<String> getInfoLines() {
        return infoLines;
    }

    public void setInfoLines(ArrayList<String> infoLines) {
        this.infoLines = infoLines;
    }

    public ArrayList<String> getLine (int lineNr) {

        ArrayList<String> list = new ArrayList();

        list.add(upComing.get(specialists.get(lineNr)+0));
        list.add(upComing.get(specialists.get(lineNr)+1));
        list.add(upComing.get(specialists.get(lineNr)+2));
        list.add(upComing.get(specialists.get(lineNr)+3));
        list.add(upComing.get(specialists.get(lineNr)+4));
        list.add(upComing.get(specialists.get(lineNr)+5));

        return list;
    }
    public ArrayList<Integer> getCounter (int counts) {

        ArrayList<Integer> list = new ArrayList<>();
        for (int i=0; i<counts; i++) {
            list.add(i);
        }
        return list;
    }
}
