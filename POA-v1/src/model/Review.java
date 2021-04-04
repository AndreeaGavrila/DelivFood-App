package model;
import java.util.UUID;

public class Review {
    private UUID userId;
    private UUID restaurantId;
    protected int stars;
    protected String content;

    public Review(UUID userId, UUID restaurantId, int stars, String content) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.stars = stars;
        this.content = content;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }


    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }


    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return this.stars + " stars " + this.content;
    }
}
