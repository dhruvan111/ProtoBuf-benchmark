package test;

import org.bean3.Test3;

import java.util.Arrays;

public class TestRunner11 {
    public static void main(String[] args) {
        Test3 test3 = Test3.newBuilder()
                .setId(10000).build();

        byte[] serialisedProto = test3.toByteArray();
        System.out.println(Arrays.toString(serialisedProto));
    }
}
