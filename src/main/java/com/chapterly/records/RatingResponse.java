package com.chapterly.records;

import java.io.Serializable;

public record RatingResponse(double rating, int totalReviews, int five, int four, int three, int two, int one) implements Serializable {
}
