package com.example.theatrelab5.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Plays {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String studio;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sessionDateTime;
    private int ticketCount;
    private int freeTickets;



    public Plays() {
    }

    public Plays(String title, String studio, LocalDateTime sessionDateTime, int ticketCount, int freeTickets) {
        this.title = title;
        this.studio = studio;
        this.sessionDateTime = sessionDateTime;
        this.ticketCount = ticketCount;
        this.freeTickets = freeTickets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getFreeTickets() {
        return freeTickets;
    }

    public void setFreeTickets(int freeTickets) {
        this.freeTickets = freeTickets;
    }
}

