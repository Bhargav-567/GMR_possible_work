import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;

public class SecretSharing {
    

    public static void main(String[] args) throws Exception {
        String filePath1 = "testcase1.json";
        String filePath2 = "testcase2.json";

        BigInteger secret1 = findSecret(filePath1);
        BigInteger secret2 = findSecret(filePath2);

        System.out.println("Secret from Testcase 1: " + secret1);
        System.out.println("Secret from Testcase 2: " + secret2);
    }

    public static BigInteger findSecret(String filePath) throws Exception {
        String content = new String(java.nio.file.Files.readAllBytes(new File(filePath).toPath()));
        JSONObject json = new JSONObject(content);

        JSONObject keys = json.getJSONObject("keys");
        int k = keys.getInt("k");

        List<BigInteger> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject point = json.getJSONObject(key);
            int base = point.getInt("base");
            String value = point.getString("value");

            BigInteger x = new BigInteger(key);
            BigInteger y = new BigInteger(value, base);

            xList.add(x);
            yList.add(y);
        }

        return lagrangeInterpolationAtZero(xList.subList(0, k), yList.subList(0, k));
    }

    // Lagrange interpolation to find f(0)
    public static BigInteger lagrangeInterpolationAtZero(List<BigInteger> x, List<BigInteger> y) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < x.size(); i++) {
            BigInteger xi = x.get(i);
            BigInteger yi = y.get(i);

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < x.size(); j++) {
                if (i == j) continue;
                BigInteger xj = x.get(j);
                num = num.multiply(xj.negate());
                den = den.multiply(xi.subtract(xj));
            }

            BigInteger li0 = num.divide(den);
            result = result.add(yi.multiply(li0));
        }

        return result;
    }
}
