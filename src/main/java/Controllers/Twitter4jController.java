package Controllers;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
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
    public static FilterQuery setFilter(){
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(new String[]{"Trump", "Teletubbies"});

        tweetFilterQuery.language(new String[]{"en"});
        return tweetFilterQuery;
    }
}
