package service;

import model.Review;
import utils.CsvReadWrite;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class ReviewService {

    private static ReviewService instance;

    public static ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService();
        }
        return instance;
    }

    private ArrayList<Review> reviews;


    private ReviewService() {
        this.reviews = new ArrayList<Review>();
    }


    public void addReview(Review rev) {
        this.reviews.add(rev);
    }


    public Optional<Review> getReviewByUserId(UUID userId) {

        return this.reviews.stream()
                .filter(r -> r.getUserId().equals(userId))
                .findFirst();
    }


    public void saveAll(String file_path) {

        if(this.reviews == null) return;
        CsvReadWrite.writeAll(this.reviews, file_path);
    }

    public void readAll(String file_path) {

        CsvReadWrite.readAll(file_path).ifPresent((csvs) -> {
            var lst = csvs.stream()
                    .map(csv -> Review.parse(csv))
                    .collect(Collectors.toList());
            this.reviews = new ArrayList(lst);
        });
    }
}
