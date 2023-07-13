package test;

import org.bean3.Test1;

import java.util.Arrays;

public class TestRunner10 {
    public static void main(String[] args) {
        Test1 test1 = Test1.newBuilder()
                .setName("dhruvan").build();

        byte[] serialisedProto = test1.toByteArray();
        System.out.println(Arrays.toString(serialisedProto));


        String jsonTest = "{\"name\":\"dhruvan\"}";
        System.out.println(Arrays.toString(jsonTest.getBytes()));
    }
}
