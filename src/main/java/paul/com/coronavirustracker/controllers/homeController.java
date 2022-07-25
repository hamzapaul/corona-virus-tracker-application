package paul.com.coronavirustracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import paul.com.coronavirustracker.models.LocationStats;
import paul.com.coronavirustracker.services.CoronaVirusDataService;

import java.util.List;

@Controller
@RequestMapping("/coronavirus")
public class homeController {

    private final CoronaVirusDataService coronaVirusDataService;

    @Autowired
    public homeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<LocationStats> stats = coronaVirusDataService.getStats();
        int totalCases = stats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        model.addAttribute("stats", stats);
        model.addAttribute("totalCases", totalCases);
        return "home";
    }

}
