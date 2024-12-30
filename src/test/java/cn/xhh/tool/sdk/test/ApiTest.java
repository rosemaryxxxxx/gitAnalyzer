package cn.xhh.tool.sdk.test;

import cn.xhh.tool.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;
import cn.xhh.tool.sdk.types.utils.BearerTokenUtils;
import com.alibaba.fastjson2.JSON;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @Author rosemaryxxxxx
 * @Date 2024/12/30 16:04
 * @PackageName:cn.xhh.tool.sdk.test
 * @ClassName: ApiTest
 * @Description: TODO
 * @Version 1.0
 */
public class ApiTest {
    public static void main(String[] args) {
        String apiKeySecret = "45a1add8cc9f54029cc11aa49b963b09.TZxLmZaBu33hJF8e";
        String token = BearerTokenUtils.getToken(apiKeySecret);
        System.out.println(token);
    }

    @Test
    public void test_http() throws IOException {
        String apiKeySecret = "45a1add8cc9f54029cc11aa49b963b09.TZxLmZaBu33hJF8e";
        String token = BearerTokenUtils.getToken(apiKeySecret);

        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        String code = "1+1";

        String jsonInpuString = "{"
                + "\"model\":\"glm-4-flash\","
                + "\"messages\": ["
                + "    {"
                + "        \"role\": \"user\","
                + "        \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + code + "\""
                + "    }"
                + "]"
                + "}";

        try(OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInpuString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
        System.out.println(response.getChoices().get(0).getMessage().getContent());

    }

    @Test
    public void scv(){
        String filePath = "D:\\data\\scvtest\\example.csv";
        String[] headerCommit = {"projectName", "commit", "parentCommit","author", "message", "date", "addLineNum", "deleteLineNum"};

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(headerCommit);
            String[] rowCommit = new String[8];

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 8; j++) {
                    rowCommit[j] = String.valueOf(i * j);
                }
                writer.writeNext(rowCommit);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void largescv(){
        String filePath = "D:\\data\\scvtest\\large_data.csv";
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                // 在这里处理每一行数据
                System.out.println(String.join(", ", row));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }


}
