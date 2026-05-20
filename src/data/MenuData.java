package data;

import model.MenuItem;
import java.util.ArrayList;

public class MenuData {
    private static ArrayList<MenuItem> menuList = new ArrayList<>();

    static {
        // Makanan (minimal 4)
        menuList.add(new MenuItem("Nasi Campur", 25000, "Makanan"));
        menuList.add(new MenuItem("Nasi Padang", 30000, "Makanan"));
        menuList.add(new MenuItem("Sate Ayam", 35000, "Makanan"));
        menuList.add(new MenuItem("Mie Goreng", 20000, "Makanan"));
        // Minuman (minimal 4)
        menuList.add(new MenuItem("Es Teh", 8000, "Minuman"));
        menuList.add(new MenuItem("Es Jeruk", 10000, "Minuman"));
        menuList.add(new MenuItem("Kopi Tubruk", 12000, "Minuman"));
        menuList.add(new MenuItem("Jus Alpukat", 20000, "Minuman"));
    }

    public static ArrayList<MenuItem> getMenuList() {
        return menuList;
    }

    public static void addMenu(MenuItem m) {
        menuList.add(m);
    }

    public static void updateMenu(int index, MenuItem m) {
        menuList.set(index, m);
    }

    public static void removeMenu(int index) {
        menuList.remove(index);
    }
}
