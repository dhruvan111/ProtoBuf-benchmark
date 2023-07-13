package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class TestRunner9 {
    public static void main(String[] args) throws JsonProcessingException {
        byte[] audioBytes = new byte[]{12,10};

        Test9 test9 = new Test9();
        test9.setBytes(audioBytes);

        ObjectMapper objectMapper = new ObjectMapper();
        String serializedJson = objectMapper.writeValueAsString(test9);

        System.out.println(serializedJson);

        byte[] byteArr = serializedJson.getBytes();

        // Size: 16B
        System.out.println(Arrays.toString(byteArr));
    }

    static class Test9{
        byte[] bytes;

        public Test9() {
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }
    }
}
