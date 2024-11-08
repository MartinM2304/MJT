package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

public class Rater {
    private int numberOfRatings;
    private double rating;


    public void rate(double rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating += rating;
            numberOfRatings++;
        }
    }

    public double getRating() {
        return (rating / numberOfRatings);
    }

    public int compareTo(double rating) {
        if (rating == this.rating) {
            return 0;
        }

        if (rating > this.rating) {
            return -1;
        }
        return 1;
    }
}
