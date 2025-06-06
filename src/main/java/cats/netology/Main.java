package cats.netology;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(org.apache.http.client.config.RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {

            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());

                ObjectMapper mapper = new ObjectMapper();
                List<Facts> facts = mapper.readValue(json, new TypeReference<List<Facts>>() {});

                // Фильтрация: только записи с upvotes != null
                List<Facts> filteredFacts = facts.stream()
                        .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 2)
                        .collect(Collectors.toList());

                // Вывод результата
                System.out.println("Факты о котиках:");
                for (Facts fact : filteredFacts) {
                    System.out.println(fact);
                }

            } else {
                System.out.println("Ошибка запроса. Код: " + response.getStatusLine().getStatusCode());
            }
        }

    }
}