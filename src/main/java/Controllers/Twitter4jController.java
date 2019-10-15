package Controllers;

import Util.customUtil;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter4jController {
// Creates new object configuration builder and returns an of of TwitterStreamfactory, Conficguration builder is
// as it allows us to easily create the proper twiter credentials andd apply them to the stream factory
    public static TwitterStream configAuth(String ConsumerKey, String ConsumerSecret, String AccessToken, String AccessSecret) {
        try {
            //configurationBuilder Constructor
            ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
            configurationBuilder
                    .setOAuthConsumerKey(ConsumerKey)
                    .setOAuthConsumerSecret(ConsumerSecret)
                    .setOAuthAccessToken(AccessToken)
                    .setOAuthAccessTokenSecret(AccessSecret);
            //returns new instance of twitterstreamfactory, constructor
            return new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        } catch (Exception exception) {
            System.out.println("Authentication Error: " + exception.getMessage());
        }
        return null;
    }
    //creates new instance of Filterquery, more conditions can be given to filterQuery if needed
    public static FilterQuery setFilter(String filterCond, String filterLang){
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(filterCond);
        tweetFilterQuery.language(filterLang);
        return tweetFilterQuery;
    }
//initialises the stream
    public static void runStream(String ConsumerKey, String ConsumerSecret, String AccessKey, String AccessSecret, String filterCond, String filterLang,String TableID, int runtime) throws InterruptedException {
        TwitterStream twitterStream = configAuth(ConsumerKey,ConsumerSecret,AccessKey,AccessSecret);
        FilterQuery tweetFilterQuery=setFilter(filterCond,filterLang);
        //here StatusAdapter or StatusListener can be used, however StatusAdapter automatically creates the unwritten public void methods that are not added to the .addListener for us
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                //inserts data to table
                RawDatasController.InsertRawData(status.getText(),customUtil.SqlToStr(status.getCreatedAt()),TableID);
            }
        });
        //filters data
        twitterStream.filter(tweetFilterQuery);
        Thread.sleep(runtime*1000);
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }
}
