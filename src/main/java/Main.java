import spark.Request;
import spark.Response;
import spark.Route;
import spark.Service;
import spark.servlet.SparkApplication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Service.ignite;

public class Main implements  spark.servlet.SparkApplication {

    private String chosenSpec;

    Database item = new Database("heroku_47fd00a889de629");

    Templates template = new Templates();

    //public static void main(String[] args) throws Exception {

    public static void main(String[] args) {
        SparkApplication app = new Main();
        app.init();

    }
        @Override
        public void init() {
            Service http = ignite().port(getPort()).threadPool(20);
            http.get("/", (request, response) -> {
                return renderContent("./index.html");
                //               return getTextFromFile("./index.html");
            });
        //}

         http.get("/clientStart", new Route() {
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

         http.post("/clientRegister", new Route() {
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
         http.get("/clientPage", new Route() {
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
         http.get("/specialistPage", new Route() {
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

         http.get("/getScreen", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {

                ScreenData data = item.getScreen();
                return template.infoScreen("Management System", data);
            }
        });

    }
    private String renderContent(String htmlFile) {
        try {

            URL url = getClass().getResource(htmlFile);

            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            return "Loading error";
        }
    }

    static int getPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            System.out.println(processBuilder.environment().get("PORT"));
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}