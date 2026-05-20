package model;

public class OrderItem {
    private MenuItem menu;
    private int qty;

    public OrderItem(MenuItem menu, int qty) {
        this.menu = menu;
        this.qty = qty;
    }

    public MenuItem getMenu() { return menu; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public int getTotal() { return menu.getPrice() * qty; }
}
