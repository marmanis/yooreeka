package org.yooreeka.examples.newsgroups.reco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class PredictedNewsStoryRating {

    private String userId;
    private String storyId;
    private double rating;

    public PredictedNewsStoryRating() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Returns rounded rating value with number of digits after decimal point
     * specified by <code>scale</code> parameter.
     *
     * @param scale number of digits to keep after decimal point.
     * @return rounded value.
     */
    public double getRating(int scale) {
        BigDecimal bd = new BigDecimal(rating);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
	public String toString() {
        return this.getClass().getSimpleName() + "[userId: " + userId
                + ", storyId: " + storyId + ", rating: " + rating + "]";
    }

    /**
     * Sorts list by rating value in descending order. Items with higher ratings
     * will be in the head of the list.
     *
     * @param values list to sort.
     */
    public static void sort(List<PredictedNewsStoryRating> values) {
        Collections.sort(values, new Comparator<PredictedNewsStoryRating>() {

            public int compare(PredictedNewsStoryRating f1, PredictedNewsStoryRating f2) {

                int result = 0;
                if( f1.getRating() < f2.getRating() ) {
                    result = 1; // reverse order
                }
                else if( f1.getRating() > f2.getRating() ) {
                    result = -1;
                }
                else {
                    result = 0;
                }
                return result;
            }

			@Override
			public Comparator<PredictedNewsStoryRating> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<PredictedNewsStoryRating> thenComparing(
					Comparator<? super PredictedNewsStoryRating> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<PredictedNewsStoryRating> thenComparing(
					Function<? super PredictedNewsStoryRating, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<PredictedNewsStoryRating> thenComparing(
					Function<? super PredictedNewsStoryRating, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<PredictedNewsStoryRating> thenComparingInt(
					ToIntFunction<? super PredictedNewsStoryRating> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<PredictedNewsStoryRating> thenComparingLong(
					ToLongFunction<? super PredictedNewsStoryRating> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<PredictedNewsStoryRating> thenComparingDouble(
					ToDoubleFunction<? super PredictedNewsStoryRating> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

        });
    }

    /**
     * Sorts list of recommendations in descending order and return topN elements.
     *
     * @param recommendations
     * @param topN
     * @return
     */
    public static List<PredictedNewsStoryRating> getTopNRecommendations(
            List<PredictedNewsStoryRating> recommendations, int topN) {

        PredictedNewsStoryRating.sort(recommendations);

        List<PredictedNewsStoryRating> topRecommendations = new ArrayList<PredictedNewsStoryRating>();
        for(PredictedNewsStoryRating r : recommendations) {
            if( topRecommendations.size() >= topN ) {
                // have enough recommendations.
                break;
            }
            topRecommendations.add(r);
        }

        return topRecommendations;
    }

    public static void printUserRecommendations(
            NewsPortalUser user,
            List<PredictedNewsStoryRating> recommendedStories) {
        System.out.println("\nRecommendations for user " + user.getName() + ":\n");
        for(PredictedNewsStoryRating r : recommendedStories) {
            System.out.printf("Story: %-36s, predicted rating: %f\n",
                    r.getStoryId(), r.getRating(4));
        }
    }

}