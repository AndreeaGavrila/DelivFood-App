package model;

import utils.ICsvConvertible;

import java.util.ArrayList;
import java.util.UUID;

public class Review implements ICsvConvertible<Review> {

    private UUID userId;
    private UUID restaurantId;
    protected int stars;
    protected String content;

    public Review(UUID userId, UUID restaurantId,
                  int stars, String content) {

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

    @Override
    public String[] stringify() {
        // userId, restaurantId, stars, content
        ArrayList s = new ArrayList<String>();

        s.add(this.userId.toString());
        s.add(this.restaurantId.toString());
        s.add(Integer.toString(this.stars));
        s.add(this.content);

        return (String[])s.toArray(new String[0]);
    }

    public static Review parse(String csv) {

        var parts = csv.split(",");
        UUID userId = UUID.fromString(parts[0]);
        UUID restaurantId = UUID.fromString(parts[1]);
        int stars = Integer.parseInt(parts[2]);
        String content = parts[3];

        return new Review(userId, restaurantId, stars, content);
    }
}
