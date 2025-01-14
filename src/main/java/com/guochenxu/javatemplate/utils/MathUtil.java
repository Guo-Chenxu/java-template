package com.guochenxu.javatemplate.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * math
 *
 * @author: guochenxu
 * @create: 2024-10-16 23:07
 * @version: 1.0
 */
public class MathUtil {
    public static String divWith1(Integer a, Integer b) {
        if (b.equals(0)) {
            return "0";
        }
        Double result = (double) a / b;
        return String.format("%.1f", result);
    }

    public static String divWith2(Integer a, Integer b) {
        if (b.equals(0)) {
            return "0";
        }
        Double result = (double) a / b;
        return String.format("%.2f", result);
    }

    public static BigDecimal divWith1(BigDecimal num, Integer count) {
        if (count.equals(0)) {
            return num;
        }
        BigDecimal pricePerPerson = num.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        BigDecimal roundedPrice = pricePerPerson.setScale(1, RoundingMode.DOWN);
        if (pricePerPerson.compareTo(roundedPrice) != 0) {
            roundedPrice = roundedPrice.add(new BigDecimal("0.1"));
        }
        return roundedPrice;
    }

    public static BigDecimal divWith2(BigDecimal num, Integer count) {
        if (count.equals(0)) {
            return num;
        }
        BigDecimal pricePerPerson = num.divide(BigDecimal.valueOf(count), 3, RoundingMode.HALF_UP);
        BigDecimal roundedPrice = pricePerPerson.setScale(2, RoundingMode.DOWN);
        if (pricePerPerson.compareTo(roundedPrice) != 0) {
            roundedPrice = roundedPrice.add(new BigDecimal("0.01"));
        }
        return roundedPrice;
    }

    public static BigDecimal avgWith1(BigDecimal... nums) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal num : nums) {
            sum = sum.add(num);
        }
        return divWith1(sum, nums.length);
    }

    public static BigDecimal avgWith2(Integer... nums) {
        Integer sum = 0;
        for (Integer num : nums) {
            sum += num;
        }
        return divWith2(BigDecimal.valueOf(sum), nums.length);
    }
}
