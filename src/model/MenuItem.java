package model;

public class MenuItem {
    private String name;
    private int price; // rupiah
    private String category; // "Makanan" atau "Minuman"

    public MenuItem(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
