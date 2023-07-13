package test;

import org.bean3.Test4;

import java.util.Arrays;

public class TestRunner12 {
    public static void main(String[] args) {
        Test4 test4 = Test4.newBuilder()
                .setId(10000).build();

        byte[] serialisedProto = test4.toByteArray();
        System.out.println(Arrays.toString(serialisedProto));
    }
}
