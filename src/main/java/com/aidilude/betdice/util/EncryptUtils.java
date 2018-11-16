package com.aidilude.betdice.util;

import java.security.MessageDigest;

public class EncryptUtils
{
    private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static final int[] abcde = { 1732584193, -271733879, -1732584194, 271733878, -1009589776 };

    private static int[] digestInt = new int[5];

    private static int[] tmpData = new int[80];

    public static String MD5Encode(String origin)
    {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");

            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static String byteArrayToHexString(byte[] b)
    {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return new StringBuilder().append(hexDigits[d1]).append(hexDigits[d2]).toString();
    }

    private static int process_input_bytes(byte[] bytedata)
    {
        System.arraycopy(abcde, 0, digestInt, 0, abcde.length);

        byte[] newbyte = byteArrayFormatData(bytedata);

        int MCount = newbyte.length / 64;

        for (int pos = 0; pos < MCount; pos++)
        {
            for (int j = 0; j < 16; j++) {
                tmpData[j] = byteArrayToInt(newbyte, pos * 64 + j * 4);
            }

            encrypt();
        }
        return 20;
    }

    private static byte[] byteArrayFormatData(byte[] bytedata)
    {
        int zeros = 0;

        int size = 0;

        int n = bytedata.length;

        int m = n % 64;

        if (m < 56) {
            zeros = 55 - m;
            size = n - m + 64;
        } else if (m == 56) {
            zeros = 63;
            size = n + 8 + 64;
        } else {
            zeros = 63 - m + 56;
            size = n + 64 - m + 64;
        }

        byte[] newbyte = new byte[size];

        System.arraycopy(bytedata, 0, newbyte, 0, n);

        int l = n;

        newbyte[(l++)] = -128;

        for (int i = 0; i < zeros; i++) {
            newbyte[(l++)] = 0;
        }

        long N = n * 8L;
        byte h8 = (byte)(int)(N & 0xFF);
        byte h7 = (byte)(int)(N >> 8 & 0xFF);
        byte h6 = (byte)(int)(N >> 16 & 0xFF);
        byte h5 = (byte)(int)(N >> 24 & 0xFF);
        byte h4 = (byte)(int)(N >> 32 & 0xFF);
        byte h3 = (byte)(int)(N >> 40 & 0xFF);
        byte h2 = (byte)(int)(N >> 48 & 0xFF);
        byte h1 = (byte)(int)(N >> 56);
        newbyte[(l++)] = h1;
        newbyte[(l++)] = h2;
        newbyte[(l++)] = h3;
        newbyte[(l++)] = h4;
        newbyte[(l++)] = h5;
        newbyte[(l++)] = h6;
        newbyte[(l++)] = h7;
        newbyte[(l++)] = h8;
        return newbyte;
    }

    private static int f1(int x, int y, int z) {
        return x & y | (x ^ 0xFFFFFFFF) & z;
    }

    private static int f2(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int f3(int x, int y, int z) {
        return x & y | x & z | y & z;
    }

    private static int f4(int x, int y) {
        return x << y | x >>> 32 - y;
    }

    private static void encrypt()
    {
        for (int i = 16; i <= 79; i++) {
            tmpData[i] = f4(tmpData[(i - 3)] ^ tmpData[(i - 8)] ^ tmpData[(i - 14)] ^ tmpData[(i - 16)], 1);
        }

        int[] tmpabcde = new int[5];
        for (int i1 = 0; i1 < tmpabcde.length; i1++) {
            tmpabcde[i1] = digestInt[i1];
        }
        for (int j = 0; j <= 19; j++)
        {
            int tmp = f4(tmpabcde[0], 5) +
                    f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) +
                    tmpabcde[4] + tmpData[j] + 1518500249;

            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int k = 20; k <= 39; k++)
        {
            int tmp = f4(tmpabcde[0], 5) +
                    f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) +
                    tmpabcde[4] + tmpData[k] + 1859775393;

            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int l = 40; l <= 59; l++)
        {
            int tmp = f4(tmpabcde[0], 5) +
                    f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) +
                    tmpabcde[4] + tmpData[l] + -1894007588;

            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int m = 60; m <= 79; m++)
        {
            int tmp = f4(tmpabcde[0], 5) +
                    f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) +
                    tmpabcde[4] + tmpData[m] + -899497514;

            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int i2 = 0; i2 < tmpabcde.length; i2++) {
            digestInt[i2] += tmpabcde[i2];
        }
        for (int n = 0; n < tmpData.length; n++)
            tmpData[n] = 0;
    }

    private static int byteArrayToInt(byte[] bytedata, int i)
    {
        return (bytedata[i] & 0xFF) << 24 | (bytedata[(i + 1)] & 0xFF) << 16 | (bytedata[(i + 2)] & 0xFF) << 8 | bytedata[(i + 3)] & 0xFF;
    }

    private static void intToByteArray(int intValue, byte[] byteData, int i)
    {
        byteData[i] = ((byte)(intValue >>> 24));
        byteData[(i + 1)] = ((byte)(intValue >>> 16));
        byteData[(i + 2)] = ((byte)(intValue >>> 8));
        byteData[(i + 3)] = ((byte)intValue);
    }

    private static byte[] getDigestOfBytes(byte[] byteData)
    {
        process_input_bytes(byteData);
        byte[] digest = new byte[20];
        for (int i = 0; i < digestInt.length; i++) {
            intToByteArray(digestInt[i], digest, i * 4);
        }
        return digest;
    }

    public static String SHAEncode(byte[] byteData)
    {
        return byteArrayToHexString(getDigestOfBytes(byteData));
    }

    public static void main(String[] args) throws InterruptedException {

    }
}