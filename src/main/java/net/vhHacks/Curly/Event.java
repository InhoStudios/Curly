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

    private DateFormat readableDateFormat;

    public Event(String title) {
        this.title = title;

        readableDateFormat = new SimpleDateFormat("MMM dd HH:mm");
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setHost(String host) {
        this.host = host;
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


        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(Color.BLUE);
        eb.setTitle(title);
        eb.addField("Host", host, false);
        eb.addField("Time", readableDateFormat.format(time), false);

        msgCh.sendMessage(eb.build()).queue();

    }

    public void print() {

        System.out.println("Host: " + host + "\nTime: " + readableDateFormat.format(time));

    }

}
