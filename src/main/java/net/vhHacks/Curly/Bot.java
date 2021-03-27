package net.vhHacks.Curly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Bot extends ListenerAdapter implements Runnable {

    private Calendar calendar;
    private Date currentDate;
    private DateFormat df;
    private Date defaultDate;

    public static JDA jda;

    private boolean running = false;

    private boolean bound = false;

    private ArrayList<Event> events;

    private MessageChannel msgCh;

    public Bot() throws LoginException {
        init();
    }

    public void init() throws LoginException {
        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("MMMddHH:mm");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        events = new ArrayList<>();

        jda = new JDABuilder(AccountType.BOT)
                .setToken(Token.token)
                .addEventListeners(this)
                .setActivity(Activity.playing("Friendly Neighbourhood Orca Messenger"))
                .build();

        addEvents();

        try {
            defaultDate = df.parse("Jan0100:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        start();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String msgContent = msg.getContentRaw();
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();

        if (msgContent.equalsIgnoreCase("c!bind")) {
            if (!bound) {
                msgCh = channel;
                bound = true;
                msg.delete().queue();
            }
        }


        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        } else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                    event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }
    }

    public void addEvents() {
        addEvent("Nature Walk / Photo Contest", "Andy Shen", "Mar2707:00", "#ship-and-share", Event.TYPE.ACTIVITY);
        addEvent("Sandboxing with Discord Bots", "Jason Hsu", "Mar2711:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Microsoft Paint Bob Ross", "Sophie Liu", "Mar2712:00", "#gaming-room", Event.TYPE.ACTIVITY);
        addEvent("Previewing Protein Properties", "Jason Hsu", "Mar2713:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Growth 101", "Galicia Gordon", "Mar2715:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Databases in Space", "Sophie Liu + Ben Taylor", "Mar2716:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Flask Primer Applied", "Frank Hui", "Mar2717:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Among Us", "Andy Shen", "Mar2718:00", "#gaming-room", Event.TYPE.ACTIVITY);
        addEvent("UI/Design Workshop", "Sophie Liu", "Mar2719:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Atari Breakout Workshop", "Andy Shen", "Mar2720:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("Movie Group Watch", "Frank Hui", "Mar2721:00", "#multipurpose-1", Event.TYPE.ACTIVITY);
        addEvent("Among Us", "Andy Shen", "Mar2800:00", "#gaming-room", Event.TYPE.ACTIVITY);
        addEvent("Nature Walk / Photo Contest", "Andy Shen", "Mar2807:00", "#ship-and-share", Event.TYPE.ACTIVITY);
        addEvent("Pitching your Hackathon Idea", "Andy Shen", "Mar2810:00", "#workshop-room", Event.TYPE.WORKSHOP);
        addEvent("One hour until submissions close!", "vhHacks 2021", "Mar2814:00", "https://vhhacks-2021.devpost.com/", Event.TYPE.VHHACKS);
        addEvent("Thirty minutes until submissions close!", "vhHacks 2021", "Mar2814:30", "https://vhhacks-2021.devpost.com/", Event.TYPE.VHHACKS);
        addEvent("Submission Deadline / Judging Period", "vhHacks 2021", "Mar2815:00", "https://vhhacks-2021.devpost.com/", Event.TYPE.VHHACKS);
        addEvent("Skribbl.io", "Emilie Ma", "Mar2815:00", "#gaming-room", Event.TYPE.ACTIVITY);
        addEvent("Closing Ceremonies", "vhHacks 2021", "Mar2817:00", "#announcements-room", Event.TYPE.VHHACKS);
    }

    public void addEvent(String title, String host, String date, String location, Event.TYPE type) {
        Event ev = new Event(title, host, location, type);
        try {
            ev.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        events.add(ev);
    }

    public void start() {
        running = true;
    }

    public void run() {
        // check date
        while(running) {
            calendar.setTime(new Date(System.currentTimeMillis()));
            currentDate = calendar.getTime();
            System.out.println(df.format(currentDate));

            Event currentEvent = null;
            
            for(Event e : events) {
                if (checkDate(currentDate, e.getTime())) {
                    if (msgCh != null) {
                        currentEvent = e;

                        e.post(msgCh);

                        e.setTime(defaultDate);
                    }
                    System.out.println("Event!");
                }
            }

            for(Event e : events) {
                if (checkDate(defaultDate, e.getTime())) {
                    events.remove(e);
                }
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean checkDate(Date currentDate, Date chckDate) {
        return df.format(currentDate).equalsIgnoreCase(df.format(chckDate));
    }

}
