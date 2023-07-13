package test.accuracy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner2 {
    public static void main(String[] args) {
        byte[] originalByteArr = {-128, 0, 127};

        List<Integer> integerList = new ArrayList<>();
        for (byte b : originalByteArr) {
            integerList.add((int) b);
        }

        byte[] convertedBytes = new byte[integerList.size()];
        for (int i = 0; i < integerList.size(); i++) {
            convertedBytes[i] = integerList.get(i).byteValue();
        }


        if (Arrays.equals(originalByteArr, convertedBytes)){
            System.out.println("equal");
        } else {
            System.out.println("Not equal");
        }
    }
}
