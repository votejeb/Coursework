package Controllers;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4jController {

    public static TwitterStream configAuth(String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessSecret) {
        try {
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder
                    .setOAuthConsumerKey(ConsumerKey)
                    .setOAuthConsumerSecret(ConsumerSecret)
                    .setOAuthAccessToken(AccessToken)
                    .setOAuthAccessTokenSecret(AccessSecret);
            return new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        } catch (Exception exception) {
            System.out.println("Authentication Error: " + exception.getMessage());
        }
        return null;
    }
    public static FilterQuery setFilter(String filterCond, String filterLang){
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(filterCond);
        tweetFilterQuery.language(filterLang);
        return tweetFilterQuery;
    }

    public static void runStream(String ConsumerKey, String ConsumerSecret, String AccessKey, String AccessSecret, String filterCond, String filterLang){
        TwitterStream twitterStream = configAuth(ConsumerKey,ConsumerSecret,AccessKey,AccessSecret);
        FilterQuery tweetFilterQuery=setFilter(filterCond,filterLang);
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                System.out.println(status.getText());
            }
        });
        twitterStream.filter(tweetFilterQuery);
    }

}
