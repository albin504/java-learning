package com.network;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.put("hello".getBytes());
        System.out.println(buffer);
        buffer.position(2);
        byte b = buffer.get();
        byte[] o = buffer.array();
        byte[] c = trim(o);
        int offset = buffer.arrayOffset();
    }

    public  static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
        {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }
}
