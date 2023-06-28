package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean3.Test1;

import java.io.IOException;
import java.util.Arrays;

import static test.TestRunner5.mega;

public class TestRunner8 {

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();
        System.out.println("Serialization Json: " + (t2-t1)/mega);
        return serData;
    }

    private static void deserializeJson(String serData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean3.Test1 test1 = objectMapper.readValue(serData, test.bean3.Test1.class);
        double t2 = System.nanoTime();
        System.out.println("Deser Json: " + (t2-t1)/mega);
    }

    private static byte[] serializeProto(Test1 test1){
        double t1 = System.nanoTime();
        byte[] serData = test1.toByteArray();
        double t2 = System.nanoTime();
        System.out.println("Ser Proto : " +  (t2-t1)/mega);
        return serData;
    }

    private static void deserializeProto(byte[] serData) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        Test1 test1 = Test1.parseFrom(serData);
        double t2 = System.nanoTime();
        System.out.println("Dese Proto: " + (t2-t1)/mega);
    }

    public static void main(String[] args) throws IOException {

        int fre = 100;
        StringBuilder tem = new StringBuilder();
        tem.append("dhruvan".repeat(fre));

        // Proto Obj
        Test1 test1 = Test1.newBuilder()
                .setName(String.valueOf(tem))
                .build();

        // Json Obj
        test.bean3.Test1 test11 = new test.bean3.Test1();
        test11.setName(String.valueOf(tem));

        String serJson = serializeJson(test11);
        deserializeJson(serJson);

        byte[] serProto = serializeProto(test1);
        deserializeProto(serProto);

        System.out.println("size of Json: " + ((double)serJson.getBytes().length)/1024);
        System.out.println("Size of Proto: " + ((double)serProto.length)/1024);
    }
}
