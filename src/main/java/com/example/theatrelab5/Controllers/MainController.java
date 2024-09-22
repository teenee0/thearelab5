package com.example.theatrelab5.Controllers;


import com.example.theatrelab5.Repository.PlaysRepository;
import com.example.theatrelab5.Models.Plays;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final PlaysRepository PlaysRepository;

    public MainController(PlaysRepository PlaysRepository) {
        this.PlaysRepository = PlaysRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная Страница");
        return "greeting";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Страница входа");
        return "login";
    }
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("title", "Поддержка");
        return "support";
    }
    @GetMapping("/Plays")
    public String Plays(Model model) {
        Iterable<Plays> Play = PlaysRepository.findAll();
        model.addAttribute("title","Страница сеансов");
        model.addAttribute("Plays", Play);
        return "Plays";
    }
    @GetMapping("/addPlay")
    public String addPlay(Model model) {
        model.addAttribute("title","Страница добавления фильма");
        return "addPlay";
    }

    @GetMapping("/Plays/{id}")
    public String updatePlay(@PathVariable long id, Model model) {
        if (!PlaysRepository.existsById(id)){
            return "redirect:/Plays";
        }
        Optional<Plays> Play = PlaysRepository.findById(id);
        ArrayList<Plays> res = new ArrayList<>();
        Play.ifPresent(res::add);
        model.addAttribute("Plays", res);
        model.addAttribute("title", "Страница редактирования");
        return "PlayDetails";

    }

    @GetMapping("/Plays/filter")
    public String searchPlays(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String studio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) Integer ticketCount,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            Model model) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, "sessionDateTime");

        LocalDateTime startDateTime = start_date != null ? start_date.atStartOfDay() : null;
        LocalDateTime endDateTime = end_date != null ? end_date.atTime(23, 59, 59) : null;

        List<Plays> Plays;

        if (title != null || studio != null || start_date != null || end_date != null|| ticketCount != null) {
            Plays = PlaysRepository.findByParams(title, studio, startDateTime, endDateTime, ticketCount, sortBy);
        } else {
            Plays = PlaysRepository.findAll(sortBy);
        }

        model.addAttribute("Plays", Plays);
        return "Plays";
    }

    @GetMapping("/Plays/stats")
    public String stats(Model model) {
        List<Object[]> stats = PlaysRepository.findPlayIssueStats();

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            dates.add(row[0].toString());
            counts.add((Long) row[1]);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("counts", counts);
        return "Play_stats";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addPlay")
    public String addPlay(@RequestParam String title,
                          @RequestParam String studio,
                          @RequestParam LocalDateTime sessionDateTime,
                          @RequestParam int ticketCount,
                          @RequestParam int freeTickets,
                          Model model) {
        Plays Play = new Plays(title, studio, sessionDateTime, ticketCount, freeTickets);
        PlaysRepository.save(Play);
        return "redirect:/Plays";

    }

    @PostMapping("/Plays/save")
    public String saveFils(
            @RequestParam("id") long id,
            @RequestParam String title,
            @RequestParam String studio,
            @RequestParam LocalDateTime sessionDateTime,
            @RequestParam int ticketCount,
            @RequestParam int freeTickets,
            Model model) {
        Plays Play = PlaysRepository.findById(id).orElseThrow();
        Play.setTitle(title);
        Play.setStudio(studio);
        Play.setSessionDateTime(sessionDateTime);
        Play.setTicketCount(ticketCount);
        Play.setFreeTickets(freeTickets);
        PlaysRepository.save(Play);
        return "redirect:/Plays";

    }

    @PostMapping("/Plays/{id}/remove")
    public String removePlay(@PathVariable long id, Model model) {
        Plays Play = PlaysRepository.findById(id).orElseThrow();
        PlaysRepository.delete(Play);
        return "redirect:/Plays";
    }






}
