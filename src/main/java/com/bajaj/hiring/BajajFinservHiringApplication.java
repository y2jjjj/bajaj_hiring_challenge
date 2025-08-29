package com.bajajfinserv.hiring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BajajFinservHiringApplication implements CommandLineRunner {

    // ✅ Update these with your details
    private static final String NAME = "Yuvraj Kumar";
    private static final String REG_NO = "22BCE2394";
    private static final String EMAIL = "yuvraj.kr2022@gmail.com";

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(BajajFinservHiringApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            System.out.println("🚀 Application started...");

            // Step 1: Generate webhook
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", NAME);
            requestBody.put("regNo", REG_NO);
            requestBody.put("email", EMAIL);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    requestBody,
                    Map.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                System.err.println("❌ Failed to generate webhook.");
                return;
            }

            String webhookUrl = (String) response.getBody().get("webhook");
            String accessToken = (String) response.getBody().get("accessToken");

            System.out.println("✅ Webhook generated: " + webhookUrl);
            System.out.println("🔑 Access Token received.");

            // Step 2: Get SQL Query based on regNo
            String finalQuery = getSqlQueryBasedOnRegNo(REG_NO);
            System.out.println("📄 Final Query Prepared: \n" + finalQuery);

            // Step 3: Submit solution
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            Map<String, String> answerBody = new HashMap<>();
            answerBody.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(answerBody, headers);

            ResponseEntity<String> submitResponse =
                    restTemplate.postForEntity(webhookUrl, entity, String.class);

            if (submitResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("🎉 Solution submitted successfully!");
                System.out.println("Server Response: " + submitResponse.getBody());
            } else {
                System.err.println("❌ Failed to submit solution. Status: "
                        + submitResponse.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decide SQL query based on registration number (odd/even last two digits).
     */
    private String getSqlQueryBasedOnRegNo(String regNo) {
        int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));
        boolean isOdd = (lastTwoDigits % 2 == 1);

        if (isOdd) {
            // Not used for you
            return "SELECT ... -- SQL for Question 1";
        } else {
            // ✅ Your final SQL query for Question 2
            return """
                   SELECT e1.EMP_ID,
                          e1.FIRST_NAME,
                          e1.LAST_NAME,
                          d.DEPARTMENT_NAME,
                          COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
                   FROM EMPLOYEE e1
                   JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
                   LEFT JOIN EMPLOYEE e2 
                          ON e1.DEPARTMENT = e2.DEPARTMENT
                         AND e2.DOB > e1.DOB
                   GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
                   ORDER BY e1.EMP_ID DESC;
                   """;
        }
    }
}
