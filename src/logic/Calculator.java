package logic;

import model.OrderItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator {
    public static class Result {
        public int subtotal;
        public int discount; // diskon 10% jika >100k
        public int afterDiscount;
        public boolean bogoApplies; // true jika subtotal > 50k dan ada minuman
        public int bogoPotentialValue; // nilai gratisan (hanya untuk info, tidak mengurangi total)
        public int tax; // 10% dari (subtotal - discount)
        public int service = 20000;
        public int total;
    }

    public static Result calculate(ArrayList<OrderItem> orders) {
        Result r = new Result();
        r.subtotal = orders.stream().mapToInt(OrderItem::getTotal).sum();

        // Diskon 10% jika subtotal > 100000
        r.discount = (r.subtotal > 100000) ? (int)(0.10 * r.subtotal) : 0;

        // Tentukan apakah BOGO berlaku (informasi saja)
        r.bogoApplies = false;
        r.bogoPotentialValue = 0;
        if (r.subtotal > 50000) {
            r.bogoApplies = true;
        }

        // Pajak dihitung setelah diskon, tetapi BOGO tidak mengurangi subtotal
        r.afterDiscount = r.subtotal - r.discount;
        r.tax = (int)(0.10 * r.afterDiscount);
        r.total = r.afterDiscount + r.tax + r.service;
        return r;
    }
}