import java.util.*;
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {
    private static Map<String, Integer> stats = new HashMap<>();
    private static int points = 0;

    public static void main(String[] args) {
        //inputStats();
        scrapeData();
        calculateFantasy(stats);
        System.out.println(points);
    }
    private static int calculateFantasy(Map<String, Integer> stats) {

        points += stats.getOrDefault("pTD", 0)*4; //adds the value stored in the has to the total of their fantasy points
        points += stats.getOrDefault("TDs",0)*6;
        points += stats.getOrDefault("2p", 0)*2;
        points += (stats.getOrDefault("rush",0)+stats.getOrDefault("receive",0))/10;
        //points += stats.getOrDefault("receive",0)/10;
        points += stats.getOrDefault("int",0)*(-2);
        points += stats.getOrDefault("fumbles",0)*(-2);
        points += stats.getOrDefault("pass",0)/25;
        points += stats.getOrDefault("40",0)*2;

        return points;
    }
    public static void scrapeData() {
        try {
            Document doc = Jsoup.connect("https://www.pro-football-reference.com/players/F/ForeDO00.htm").get();

            // Safe extraction of passing yards
            Element passingYardsElement = doc.select("#passing\\.2023 td[data-stat='pass_yds']").first();
            safelyParseAndPut(passingYardsElement, "pass");

            // Safe extraction of rushing yards
            Element rushingYardsElement = doc.select("#rushing_and_receiving\\.2023 > td:nth-child(9)").first();
            safelyParseAndPut(rushingYardsElement, "rush");

            // Safe extraction of receiving yards
            Element receiveYardsElement = doc.select("#rushing_and_receiving\\.2023 > td:nth-child(18)").first();
            safelyParseAndPut(receiveYardsElement, "receive");

        } catch (IOException e) {
            System.out.println("Error during web scraping: " + e.getMessage());
        }
    }

    public static void inputStats(){
        Scanner sc = new Scanner(System.in);


        System.out.println("Enter the passing yards: ");
        stats.put("pass", sc.nextInt());

        System.out.println("Enter the receiving yards: ");
        stats.put("receive", sc.nextInt());

        System.out.println("Enter the punt return yards: ");
        stats.put("punt", sc.nextInt());

        System.out.println("Enter the rushing yards: ");
        stats.put("rush", sc.nextInt());

        System.out.println("Enter passing touchdowns: ");
        stats.put("pTD", sc.nextInt());

        System.out.println("Enter rushing, receiving and return touchdowns: ");
        stats.put("TDs", sc.nextInt());

        System.out.println("Enter the 2 point conversions: ");
        stats.put("2p", sc.nextInt());

        System.out.println("Enter interceptions: ");
        stats.put("int", sc.nextInt());

        System.out.println("Enter fumbles: ");
        stats.put("fumbles", sc.nextInt());

        System.out.println("Enter the number of 40+ yard touch downs: ");
        stats.put("40", sc.nextInt());

        int fantasyPoints = calculateFantasy(stats);
        System.out.println("Total fantasy points: " + fantasyPoints);

        sc.close();
    }
    private static void safelyParseAndPut(Element element, String key) {
        if (element != null && !element.text().isEmpty()) {
            try {
                int yards = Integer.parseInt(element.text());
                stats.put(key, yards);
                System.out.println(key + " Yards: " + yards);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for " + key);
                stats.put(key, 0);
            }
        } else {
            System.out.println(key + " data not available");
            stats.put(key, 0);
        }
    }
}