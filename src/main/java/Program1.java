import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Program1 {
    private static final String[] twodigits = {"", " Ten", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"};
    private static final String[] onedigit = {"", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten", " Eleven", " Twelve", " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen", " Eighteen", " Nineteen"};
    //This method is used to convert a no. into word (upto 1000) -- helper function
    private static String convertUptoThousand(int number) {
        String soFar;
        if (number % 100 < 20) {
            soFar = onedigit[number % 100];
            number = number/ 100;
        } else {
            soFar = onedigit[number % 10];
            number = number/ 10;
            soFar = twodigits[number % 10] + soFar;
            number = number/ 10;
        }
        if (number == 0)
            return soFar;
        return onedigit[number] + " Hundred " + soFar;
    }
    //method that converts a long number (0 to 999999999) to string
    public static String Number_to_Words(long number){
        if (number == 0) {
            return "zero";
        }
        String num = Long.toString(number);
        //for creating a mask padding with "0"
        String pattern = "000000000000";
        //creates a DecimalFormat using the specified pattern and also provides the symbols for the default locale
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        //format a number of the DecimalFormat instance
        num = decimalFormat.format(number);
        //format: XXXnnnnnnnnn
        int billions = Integer.parseInt(num.substring(0,3));
        //format: nnnXXXnnnnnn
        int millions  = Integer.parseInt(num.substring(3,6));
        //format: nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(num.substring(6,9));
        //format: nnnnnnnnnXXX
        int thousands = Integer.parseInt(num.substring(9,12));
        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1 :
                tradBillions = convertUptoThousand(billions)+ " Billion ";
                break;
            default :
                tradBillions = convertUptoThousand(billions)+ " Billion ";
        }
        String result =  tradBillions;
        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1 :
                tradMillions = convertUptoThousand(millions)+ " Million ";
                break;
            default :
                tradMillions = convertUptoThousand(millions)+ " Million ";
        }
        result =  result + tradMillions;
        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1 :
                tradHundredThousands = "One Thousand ";
                break;
            default :
                tradHundredThousands = convertUptoThousand(hundredThousands)+ " Thousand ";
        }
        result =  result + tradHundredThousands;
        String tradThousand;
        tradThousand = convertUptoThousand(thousands);
        result =  result + tradThousand;
        //removing extra space if any
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
        //creating http-request object for URL
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
        //creating http-client object
        HttpClient client = HttpClient.newBuilder().build();
        //calling API and storing in response object
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //check for error from API Call
        if(response.statusCode()!=200){
            System.out.println("Error fetching Data from Server");
            return;
        }
        //Extracting JSONString from response body
        String jsonString = response.body().toString();

        //converting JSON-string to object
        Object obj = JSONValue.parse(jsonString);
        //converting object to JSONObject
        JSONObject jsonObj = (JSONObject)obj;
        //Extracting bpi object from main object
        JSONObject bpi = (JSONObject) jsonObj.get("bpi");

        //Extracting currency object from bpi object
        JSONObject EUR = (JSONObject) bpi.get("EUR");
        JSONObject GBP = (JSONObject) bpi.get("GBP");
        JSONObject USD = (JSONObject) bpi.get("USD");

        //Extracting exchange_rate from currency object
        Double eur_rate = (Double) EUR.get("rate_float");
        Double gbp_rate = (Double) GBP.get("rate_float");
        Double usd_rate = (Double) USD.get("rate_float");

        //Converting each exchange rate to words
        String eur_rate_In_words = Number_to_Words(eur_rate.longValue());
        String gbp_rate_In_words = Number_to_Words(gbp_rate.longValue());
        String usd_rate_In_words = Number_to_Words(usd_rate.longValue());

        //Printing Exchange rates to console:
        System.out.println("Euro : \n"+eur_rate+"\t: "+eur_rate_In_words);
        System.out.println("GBP : \n"+gbp_rate+"\t: "+gbp_rate_In_words);
        System.out.println("USD : \n"+usd_rate+"\t: "+usd_rate_In_words);
    }
}
