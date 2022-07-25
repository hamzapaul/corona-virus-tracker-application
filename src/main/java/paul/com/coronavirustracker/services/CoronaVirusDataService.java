package paul.com.coronavirustracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import paul.com.coronavirustracker.models.LocationStats;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> stats = new ArrayList<>();


    //spring execute this method when we start the app
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")   //execute every day
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();

        //Http client to make a request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Read csv file using common csv library
        StringReader csvReader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);

        for (CSVRecord record : records) {
            LocationStats ls = new LocationStats();
            ls.setCountry(record.get("Country/Region"));
            ls.setState(record.get("Province/State"));
            ls.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            newStats.add(ls);
        }
        this.stats = newStats;
    }

    public List<LocationStats> getStats() {
        return stats;
    }
}
