import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderLoadTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final int NUM_THREADS = 10;
    private static final int RAMP_UP_PERIOD = 5;
    private static final int LOOP_COUNT = 2;

    public static void main(String[] args) throws Exception {
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // JMeter 초기화
        String jmeterHome = System.getProperty("user.dir");
        String jmeterProperties = jmeterHome + "/src/main/resources/jmeter.properties";
        JMeterUtils.setJMeterHome(jmeterHome);
        JMeterUtils.loadJMeterProperties(jmeterProperties);
        JMeterUtils.initLocale();

        // 테스트 플랜 생성
        HashTree testPlanTree = new HashTree();

        // HTTP Header Manager 생성
        HeaderManager headerManager = new HeaderManager();
        headerManager.add(new Header("Content-Type", "application/json"));
        headerManager.add(new Header("X-Auth-User-ID", "1"));  // 테스트용 사용자 ID

        // 1. 주문 준비 요청
        HTTPSampler prepareOrderSampler = createHttpSampler("/api/v1/orders/prepare", "POST");
        prepareOrderSampler.setPostBodyRaw(true);
        prepareOrderSampler.addNonEncodedArgument("", createPrepareOrderJson(), "");

        // 2. 주문 생성 요청
        HTTPSampler createOrderSampler = createHttpSampler("/api/v1/orders/create/${orderId}", "POST");
        createOrderSampler.setPostBodyRaw(true);
        createOrderSampler.addNonEncodedArgument("", createOrderJson(), "");

        // Loop Controller 설정
        LoopController loopController = new LoopController();
        loopController.setLoops(LOOP_COUNT);
        loopController.setFirst(true);

        // Thread Group 설정
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(NUM_THREADS);
        threadGroup.setRampUp(RAMP_UP_PERIOD);
        threadGroup.setSamplerController(loopController);

        // 테스트 플랜 구성
        TestPlan testPlan = new TestPlan("Order Process Load Test Plan");
        testPlanTree.add(testPlan);

        HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
        threadGroupHashTree.add(headerManager);
        threadGroupHashTree.add(prepareOrderSampler);
        threadGroupHashTree.add(createOrderSampler);

        // 리스너 추가
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        String logFile = "order_test_results.jtl";
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(logFile);
        testPlanTree.add(testPlanTree.getArray()[0], logger);

        // 테스트 실행
        jmeter.configure(testPlanTree);
        jmeter.run();

        System.out.println("Test completed. Results file: " + new File(logFile).getAbsolutePath());
    }

    private static HTTPSampler createHttpSampler(String path, String method) {
        HTTPSampler sampler = new HTTPSampler();
        sampler.setDomain(BASE_URL.replace("http://", "").replace("https://", ""));
        sampler.setProtocol(BASE_URL.startsWith("https") ? "https" : "http");
        sampler.setPath(path);
        sampler.setMethod(method);
        return sampler;
    }

    private static String createPrepareOrderJson() throws Exception {
        Map<String, Object> json = new HashMap<>();
        json.put("orderAmount", 100000);
        json.put("paymentAmount", 95000);
        json.put("items", List.of(
                Map.of("productId", 1, "price", 50000, "quantity", 1),
                Map.of("productId", 2, "price", 45000, "quantity", 1)
        ));
        return new ObjectMapper().writeValueAsString(json);
    }

    private static String createOrderJson() throws Exception {
        Map<String, Object> json = new HashMap<>();
        json.put("orderAddressRequest", Map.of(
                "name", "홍길동",
                "streetAddress", "서울시 강남구",
                "addressDetail", "123-456",
                "zipcode", "12345",
                "phone", "010-1234-5678",
                "message", "문 앞에 놓아주세요",
                "isDefault", true
        ));
        json.put("paymentDetailsRequest", Map.of(
                "paymentMethod", "KB",
                "cardNumber", "1234-5678-9012-3456",
                "cardHolderName", "홍길동",
                "expirationDate", "12/25",
                "cvv", "123"
        ));
        return new ObjectMapper().writeValueAsString(json);
    }
}