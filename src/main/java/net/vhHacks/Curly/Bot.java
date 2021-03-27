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

    public static JDA jda;

    private boolean running = false;

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

        try {
            addEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        start();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event. getMessage();
        String msgContent = msg.getContentRaw();
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();

        if (msgContent.equalsIgnoreCase("c!bind")) {
            channel.sendMessage("Binded!").queue();
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

    public void addEvents() throws ParseException {
        Event ev = new Event("Primer Workshop");
        ev.setHost("Andy");
        ev.setTime(df.parse("Mar2619:35"));
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

            for(Event e : events) {
                if (checkDate(currentDate, e.getTime())) {
                    if(msgCh != null) {
                        e.print();
                    }
                    System.out.println("Event!");
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
