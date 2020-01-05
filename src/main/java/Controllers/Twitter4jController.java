package Controllers;

import Util.customUtil;
import org.glassfish.jersey.media.multipart.FormDataParam;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("twitter4j/")
public class Twitter4jController {
// Creates new object configuration builder and returns an of of TwitterStreamfactory, Configuration builder is
// as it allows us to easily create the proper twitter credentials and apply them to the stream factory
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
            return ((TwitterStream)exception);
        }
    }

    //creates new instance of Filterquery, more conditions can be given to filterQuery if needed
    public static FilterQuery setFilter(String filterCond, String filterLang){
        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(filterCond);
        tweetFilterQuery.language(filterLang);
        return tweetFilterQuery;
    }

    //runs stream
    @POST
    @Path("streamdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static void runStream(@FormDataParam("ConsumerKey") String ConsumerKey,
                                 @FormDataParam("ConsumerSecret") String ConsumerSecret,
                                 @FormDataParam("AccessKey") String AccessKey,
                                 @FormDataParam("AccessSecret") String AccessSecret,
                                 @FormDataParam("filterCond") String filterCond,
                                 @FormDataParam("filterlang") String filterLang,
                                 @FormDataParam("TableID") String TableID,
                                 @FormDataParam("runtime") Integer runtime) throws InterruptedException {
        TwitterStream twitterStream = configAuth(ConsumerKey,ConsumerSecret,AccessKey,AccessSecret);
        FilterQuery tweetFilterQuery=setFilter(filterCond,filterLang);
        //here StatusAdapter or StatusListener can be used, however StatusAdapter automatically creates the unwritten public void methods that are not added to the .addListener for us
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                //inserts data to table, retweet data is essentially useless
                if (!status.isRetweet()){
                    RawDatasController.InsertToTable(status.getText(), customUtil.SqlToStr(status.getCreatedAt()), TableID);
                }
            }

        });
        //filters data
        twitterStream.filter(tweetFilterQuery);
        Thread.sleep(runtime*1000);
        twitterStream.cleanUp();
        twitterStream.shutdown();
    }
}
