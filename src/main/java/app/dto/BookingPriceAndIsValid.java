package app.dto;

public class BookingPriceAndIsValid {

    private boolean isValid;
    private int price;

    public BookingPriceAndIsValid() {
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
