package net.vhHacks.Curly;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    private Date time;
    private String title;
    private String host;

    private String location;

    private DateFormat readableDateFormat;

    private TYPE thisType;

    public static enum TYPE {
        ACTIVITY,
        VHHACKS,
        WORKSHOP
    }

    public Event(String title, String host, String location, TYPE type) {
        this.title = title;
        this.host = host;
        this.location = location;
        this.thisType = type;

        readableDateFormat = new SimpleDateFormat("MMM dd HH:mm");
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getHost() {
        return host;
    }

    public Date getTime() {
        return time;
    }

    public void post(MessageChannel msgCh) {

        String message = "Hey <@&761319214106804254>, I've got an event for you!";

        msgCh.sendMessage(message).queue();


        EmbedBuilder eb = new EmbedBuilder();

        switch (thisType) {
            case ACTIVITY:
                eb.setColor(Color.RED);
                break;
            case WORKSHOP:
                eb.setColor(Color.MAGENTA);
                break;
            case VHHACKS:
                eb.setColor(Color.CYAN);
                break;
            default:
                eb.setColor(Color.GRAY);

        }
        eb.setTitle(title);
        eb.addField("Time (PDT, 24h)", readableDateFormat.format(time), false);
        eb.addField("Location", location, false);
        eb.addField("Hosted by", host, true);

        msgCh.sendMessage(eb.build()).queue();

    }

    public void print() {

        System.out.println("Host: " + host + "\nTime " + readableDateFormat.format(time));

    }

}
