package org.yooreeka.examples.newsgroups.reco;

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.collab.recommender.PredictedItemRating;
import org.yooreeka.algos.reco.collab.recommender.Recommender;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;

public class StoryRecommender {

    private DatasetAdapter rDs;
    private Recommender delphi;

    public StoryRecommender(NewsDataset ds) {
        this.rDs = new DatasetAdapter(ds);
    }

    public void calculateRecommendations() {
        Delphi d = new Delphi(rDs, RecommendationType.ITEM_PENALTY_BASED, false);
        d.setVerbose(true);

        this.delphi = d;
    }

    public List<PredictedNewsStoryRating> recommendStories(String newsUserId) {

        if( delphi == null ) {
            String msg = "Recommender not initialized. You need calculate recommnedations first.";
            throw new RuntimeException(msg);
        }
        // Mapping news user to User used by recommender
        User user = rDs.getUserForNewsUserId(newsUserId);

        List<PredictedItemRating> predictedRatings = delphi.recommend(user);

        List<PredictedNewsStoryRating> ratings =
            new ArrayList<PredictedNewsStoryRating>();

        for(PredictedItemRating iR : predictedRatings) {
            PredictedNewsStoryRating r = new PredictedNewsStoryRating();
            r.setUserId(newsUserId);
            r.setRating(iR.getRating());
            // map Item into NewsStory
            NewsStory newsStory = rDs.getNewsStoryForItemId(iR.getItemId());
            r.setStoryId(newsStory.getId());

            ratings.add(r);
        }

        return ratings;
    }

}