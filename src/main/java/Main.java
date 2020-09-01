import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static j2html.TagCreator.*;

public class Main {

    private String chosenSpec;

    public static void main(String[] args) throws Exception {

        Database item = new Database("heroku_47fd00a889de629");

        Templates template = new Templates();

        Spark.port(80);

        Spark.get("/home", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                System.out.println(request.queryParams("person"));
                if (request.queryParams("person").equals("specialist")) {

                }
                //item.registerRecipient();
                return html(
                        head(
                                title("Title")
                        ),
                        body(
                                h1("Heading!")
                        )
                );
            }
        });
        Spark.get("/clientStart", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                if (request.queryParams("person").equals("specialist")) {

                    return template.loginSpecialist("Management System", true);

                } else if (request.queryParams("person").equals("client")) {

                    ArrayList<String> list = item.getSpecialistsList();
                    ArrayList<String> listDays = new ArrayList<>();
                    ArrayList<String> listSlots = new ArrayList<>();

                    if (request.queryParams("name")!=null) {
                        list.clear();
                        list.add(request.queryParams("name"));
                        listDays = item.getDates(request.queryParams("name"));

                        if (request.queryParams("date")!=null) {

                            listDays.clear();
                            listDays.add(request.queryParams("date"));
                            listSlots = item.getSlots(request.queryParams("name"), request.queryParams("date"));

                            return template.chooseSpecialist("Management System", list, true, listDays, true, listSlots );
                        }

                        return  template.chooseSpecialist("Management System", list, true, listDays, false, listSlots );
                    }
                        listDays.add("FirstDay");
                    return template.chooseSpecialist("Management System", list, false, listDays, false, listSlots );
                }
                return "Error";
            }
        });

        Spark.post("/clientRegister", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String name = request.queryParams("name");
                String day = request.queryParams("date");
                String slot = request.queryParams("timeSlot");
                String client_id = ""+( System.currentTimeMillis() % (int)Math.pow(10, 7) );
                item.registerClient(name, day, slot, client_id);
                HashMap<String, String> list = item.getClient(client_id);
                return template.clientPage("Management System", list.get("client_id"),
                        list.get("day"), list.get("name"), "slot_"+list.get("que_place"), list.get("status") );
            }
        });
        Spark.get("/clientPage", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String client_id = request.queryParams("client_id");
                if (request.queryParams("cancel")!=null) {
                    item.cancelBooking(client_id);
                }
                HashMap<String, String> list = item.getClient(client_id);
                if (list!=null) {
                    return template.clientPage("Management System", list.get("client_id"),
                            list.get("day"), list.get("name"), "slot_" + list.get("que_place"), list.get("status"));
                } else {
                    return  "Kazkas";// getTextFromFile("index.html");
                }
            }
        });

        // TO-DO
        Spark.get("/specialistPage", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                String number = request.queryParams("number");
                Schedule schedule;
                if (number.equals("0000")) {
                    return template.infoScreen("Management System", item.getScreen());
                }

                if (request.queryParams("client_id") != null) {

                    String client_id = request.queryParams("client_id");

                    if (request.queryParams("cancel")!=null) {
                        item.cancelBooking(client_id);
                        schedule = item.getSpecialist(number, null);
                        return template.specialistPage("Management System", schedule);

                    } else if ((request.queryParams("start_time")!=null)) {

                        String time = request.queryParams("start_time");
                        item.startMeeting(time, client_id, number);
                        schedule = item.getSpecialist(number, null);

                        return template.specialistPage("Management System", schedule);

                    } else if ((request.queryParams("end_time")!=null)) {

                        String spec_name = request.queryParams("specialist_name");
                        String time = request.queryParams("end_time");
                        String slot = "slot_"+request.queryParams("slot");
                        item.endMeeting(time, client_id, spec_name, slot);
                        schedule = item.getSpecialist(number, null);

                        return template.specialistPage("Management System", schedule);

                    } else {

                        schedule = item.getSpecialist(number, client_id);
                        return template.specialistPage("Management System", schedule);
                    }


                } else {
                    schedule = item.getSpecialist(number, null);
                    if (schedule != null) {
                        return template.specialistPage("Management System", schedule);
                    } else {
                        return template.loginSpecialist("Management System", false);
                    }
                }

            }
        });

        Spark.get("/getScreen", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                ScreenData data = item.getScreen();
                return template.infoScreen("Management System", data);
            }
        });

        Spark.get("/", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return getTextFromFile("index.html");
            }
        });
    }

    private static String getTextFromFile(String path) {
        try {
            URI fullPath = com.sun.tools.javac.Main.class.getClassLoader().getResource(path).toURI();
            return Files.readString(Paths.get(fullPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Loading error";
    }
}