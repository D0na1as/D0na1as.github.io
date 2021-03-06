import j2html.TagCreator;
import j2html.tags.ContainerTag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static j2html.TagCreator.*;

public class Templates {
    
    private Main main = new Main();
    public String loginSpecialist(String title, boolean success) {
        return html(
                head(
                        meta().withCharset("utf-8"),
                        meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                        link().withRel("stylesheet").withType("text/css").withHref("style.css"),
                        title(title)
                ), body(
                        div(
                                getHome().withClass("home__btn")
                        ).withClass("top"),
                        h1("Log in"),
                        form(
                                input().withType("number").withName("number").withPlaceholder("Enter your number in the system").isRequired(),
                                button("Log in").withClass("button").withType("submit"),
                                TagCreator.iff(!success, p("User doesn't exist"))
                        ).withClass("reg__spec").withMethod("get").withAction("/specialistPage")
                ).withClass("main")
        ).render();
    }

    public String specialistPage(String title, Schedule schedule) {

        return html(
                head(
                        meta().withCharset("utf-8"),
                        meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                        link().withRel("stylesheet").withType("text/css").withHref("style.css"),
                        title(title)
                ), body(
                        div(
                                getHome().withClass("home__btn"),
                                h3(getTime()).withClass("clock")
                        ).withClass("top"),
                        h1(schedule.getSpecialist()).withClass("h1__spc"),
                        infoTable(schedule),
                        hr(),
                        h2("Time table:").withClass("h2__spc"),
                        table(
                                tr(
                                   th(
                                         div(
                                              div("Date").withClass("c2"),
                                              div("Time").withClass("c3")
                                         ).withClass("c1")
                                     ),
                                     each(schedule.getDates(), text -> TagCreator.th(div(text)))
                                ),
                                each(schedule.getSlotList(), slot -> tr(
                                             th(slotToTime(slot)),
                                             each(schedule.getDates(), day ->
                                               iffElse(!schedule.getClient().equals("0") || schedule.getData(day, slot).equals("empty") || schedule.getData(day, slot).equals("occurred") ,
                                                       td(schedule.getData(day, slot)),
                                                       td(form(
                                                               input().withName("number").withValue(String.valueOf(schedule.getNumber())).withType("hidden"),
                                                               button(schedule.getData(day, slot)).withName("client_id").
                                                                       withValue(schedule.getData(day, slot)).withType("submit").
                                                                       withMethod("get").withAction("/specialistPage"))).withClass("td__active"))
                                               )
                                            )
                                    )
                        ).withClass("table__spc")
                  ).withClass("main")
        ).render();
    }
    public ContainerTag infoTable (Schedule schedule) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar current = Calendar.getInstance();
        String current_date = format.format(current.getTime());

        if (schedule.getCurrentClient().equals("no clients") && schedule.getClient().equals("0")) {
            return section(
                    h2("You have no appointments").withClass("h2__spc")
                    );
        } else if (!schedule.getClient().equals("0")) {
            return section(
                        h2("Current client Nr.:" + schedule.getClient()).withClass("h2__spc"),
                    table(tr(th("date"),th("time")),
                            tr(td(schedule.getClientDate()),td(slotToTime(schedule.getClientTime())))).withClass("table__client"),
                    form(
                            input().withName("number").withValue(String.valueOf(schedule.getNumber())).withType("hidden"),
                            input().withName("client_id").withValue(schedule.getClient()).withType("hidden"),
                            input().withName("specialist_name").withValue(schedule.getSpecialist()).withType("hidden"),
                            input().withName("slot").withValue(schedule.getClientTime()).withType("hidden"),
                            button("End meeting").withClass("button").withName("end_time").withValue(current_date).withType("submit")
                            ).withMethod("get").withAction("/specialistPage")
                    );
        } else {
            return section(
                    h2("Next client Nr.:" + schedule.getCurrentClient()).withClass("h2__spc"),
                    table(tr(th("date"),th("time")),
                            tr(td(schedule.getClientDate()),td(slotToTime(schedule.getClientTime())))).withClass("table__client"),
                    form(
                            input().withName("client_id").withValue(schedule.getCurrentClient()).withType("hidden"),
                            input().withName("number").withValue(String.valueOf(schedule.getNumber())).withType("hidden"),
                            button("Start meeting").withClass("button").withName("start_time").withValue(current_date).withType("submit"),
                            button("Cancel").withClass("button").withName("cancel").withValue("cancel").withType("submit")
                    ).withMethod("get").withAction("/specialistPage")
            );
        }

    }

    public String chooseSpecialist(String title, ArrayList<String> specialistList, boolean daysAppear,
                                          ArrayList<String> daysList, boolean slotsAppear, ArrayList<String> slotList) {
        boolean condition;
        condition = daysAppear && slotsAppear;
        return html(
                head(
                        meta().withCharset("utf-8"),
                        meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                        link().withRel("stylesheet").withType("text/css").withHref("style.css"),
                        title(title)
                ), body(
                        div(
                                getHome().withClass("home__btn")
                        ).withClass("top"),
                        h1("Choose Specialist"),
                        specialistButton(specialistList, daysAppear),
                        iff(daysAppear, h3("Available day's:")),
                        iff(daysAppear, daysButton(daysList, specialistList.get(0), slotsAppear)),
                        iff(slotsAppear, h3("Available time:")),
                        iff(condition, timeButton(slotList, specialistList.get(0), daysList.get(0)))
                    ).withClass("main")
            ).render();
    }

    public String clientPage(String title, String client_id, String date, String specialist, String slot, String status ) {

        String lineNr = slotToLineNr(slot);

        return html(
                head(
                        meta().withCharset("utf-8"),
                        meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                        link().withRel("stylesheet").withType("text/css").withHref("style.css"),
                        title(title)
                ), body(
                        div(
                                getHome().withClass("home__btn"),
                                h3(getTime()).withClass("clock")
                        ).withClass("top"),
                        h1("Client Nr.: " + client_id),
                        table(
                                tr(
                                      th("Date"),
                                      th("Time"),
                                      th("Nr. in Line"),
                                      th("Specialist")
                              ),
                              tr(
                                      td(date),
                                      td(slotToTime(slot)),
                                      td(lineNr),
                                      td(specialist)
                              )
                        ).withClass("table__client"),
                        TagCreator.iff(status.equals("booked"), h3("Left util appointment: " + calculateRemainTime(date, slot, status))),
                        TagCreator.iff(status.equals("occurred"), h3("Appointment successfully occurred.")),
                        TagCreator.iff(status.equals("booked"),
                                form( input().withName("client_id").withValue(client_id).withType("hidden"),
                                    button("Cancel Appointment").withClass("button").withName("cancel").withValue("cancel").withType("submit")).withMethod("get").withAction("/clientPage")),
                        TagCreator.iff(status.equals("canceled"), p("Your visit has been canceled!"))
                ).withClass("main")
               ).render();
    }

    public String infoScreen(String title, ScreenData data) {

            ArrayList<String> infoLines = data.getInfoLines();
            return html(
                    head(
                            meta().withCharset("utf-8"),
                            meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                            meta().attr("http-equiv", "refresh").withContent("5").withHref("https://appfornfq.herokuapp.com/getScreen"),
                            link().withRel("stylesheet").withType("text/css").withHref("style.css"),
                            title(title)
                    ), body(
                            getHome(),
                            table(
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    colgroup().attr("width", "14%"),
                                    tr(
                                            each(data.getInfoLines(), text -> TagCreator.th(text).withClass("th__first"))
                                    ),
                                    each(data.getSpecialists(), spec -> tr(
                                            th(spec),
                                            each(data.getCounter(6), line ->
                                                      td(data.getLine(data.getSpecialists().indexOf(spec)).get(line)
                                                    )
                                            )
                                            )
                                    )
                            ),
                            h1("Time: " + getTime())
                    ).withClass("info")
            ).render();
    }

    public String slotToTime (String slot) {
        switch (slot) {
            case "slot_1": case "1":
                return "08:00";
            case "slot_2": case "2":
                return "08:30";
            case "slot_3": case "3":
                return "09:00";
            case "slot_4": case "4":
                return "09:30";
            case "slot_5": case "5":
                return "10:00";
            case "slot_6": case "6":
                return "10:30";
            case "slot_7": case "7":
                return "11:00";
            case "slot_8": case "8":
                return "11:30";
            case "slot_9": case "9":
                return "12:00";
            case "slot_10": case "10":
                return "12:30";
            case "slot_11": case "11":
                return "13:00";
            case "slot_12": case "12":
                return "13:30";
            case "slot_13": case "13":
                return "14:00";
            case "slot_14": case "14":
                return "14:30";
            case "slot_15": case "15":
                return "15:00";
            case "slot_16": case "16":
                return "15:30";
            default:
                return "Error";
        }
    }
    public String slotToLineNr (String slot) {

        String lineNr = slot.split("_")[1];
        if (lineNr.length()<2) {
            lineNr = "0" + lineNr;
        }

        return lineNr;
    }

    public String calculateRemainTime(String date, String slot, String status){

        // date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // two dates
        java.util.Date scheduledDate;
        Calendar current = Calendar.getInstance();
        java.util.Date currentDate;
        String current_date = format.format(current.getTime());
        String scheduled_date = date + " " +slotToTime(slot);
        try {
            scheduledDate = format.parse(scheduled_date);
            currentDate = format.parse(current_date);
            long diffInMillies = scheduledDate.getTime() - currentDate.getTime();
            long diffence_in_minute = TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
            String left_time = "";
            if (diffence_in_minute>0 && status.equals("booked")) {
                if (diffence_in_minute/1440>0) {
                    left_time = diffence_in_minute / 1440 + "d. ";
                    diffence_in_minute =  diffence_in_minute % 1440;
                    System.out.println(diffence_in_minute);
                }
                if (diffence_in_minute/60>1) {
                    left_time = left_time + diffence_in_minute / 60 + "h. ";
                    diffence_in_minute =  diffence_in_minute % 60;
                    System.out.println(diffence_in_minute);
                }
                    left_time = left_time + diffence_in_minute + "min.";
            }

            return left_time;

        } catch (ParseException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private ContainerTag getHome() {
        return form(button(
                img().withSrc("./home.png")
        ).withClass("button home")).withMethod("get").withAction("/");
    }

    private ContainerTag specialistButton(ArrayList<String> specialistList, boolean daysAppear) {
        if (daysAppear) {
            return form(input().withName("person").withValue("client").withType("hidden"),
                    each(specialistList, specialist ->
                            button(specialist).withClass("button").withType("submit"))
            ).withMethod("get").withAction("/clientStart");
        } else {
            return form(input().withName("person").withValue("client").withType("hidden"),
                    each(specialistList, specialist ->
                            button(specialist).withClass("button").withName("name").withValue(specialist).withType("submit"))
            ).withMethod("get").withAction("/clientStart");
        }
    }

    private ContainerTag daysButton(ArrayList<String> daysList, String specialist, boolean timeAppear) {
        if (timeAppear) {
            return form(
                    input().withName("person").withValue("client").withType("hidden"),
                    input().withName("name").withValue(specialist).withType("hidden"),
                    each(daysList, day ->
                            button(day).withClass("button").withType("submit"))
            ).withMethod("get").withAction("/clientStart");
        } else {
            return form(
                    input().withName("person").withValue("client").withType("hidden"),
                    input().withName("name").withValue(specialist).withType("hidden"),
                    each(daysList, day ->
                            button(day).withClass("button").withName("date").withValue(day).withType("submit"))
            ).withMethod("get").withAction("/clientStart");
        }
    }

    private ContainerTag timeButton(ArrayList<String> slotList, String specialist, String day) {
        return form( input().withName("person").withValue("client").withType("hidden"),
                    input().withName("name").withValue(specialist).withType("hidden"),
                    input().withName("date").withValue(day).withType("hidden") ,
                each(slotList, slot ->
                        button(slotToTime(slot)).withClass("button").withName("timeSlot").withValue(slot).withType("submit"))
        ).withMethod("post").withAction("/clientRegister");
    }


    private String getTime() {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd ' ' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }

}
