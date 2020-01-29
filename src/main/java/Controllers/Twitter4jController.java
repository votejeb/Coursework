package Controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static Controllers.DataSetsController.UpdateSet;
import static Controllers.RawDatasController.CreateTable;
import static Controllers.RawDatasController.InsertToTable;
import static Controllers.SearchandSort.SortData;
import static Util.customUtil.getTime;
import static Util.customUtil.listToString;

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
    @Produces(MediaType.APPLICATION_JSON)
    public String runStream(@FormDataParam("ConsumerKey") String ConsumerKey,
                                   @FormDataParam("ConsumerSecret") String ConsumerSecret,
                                   @FormDataParam("AccessToken") String AccessToken,
                                   @FormDataParam("AccessSecret") String AccessSecret,
                                   @FormDataParam("Keyword") String filterCond,
                                   @FormDataParam("Language") String filterLang,
                                   @FormDataParam("TableID") Integer TableID,
                                   @FormDataParam("PublicPrivate")Boolean PublicPrivate,
                                   @FormDataParam("RunTime") Integer runtime,
                                   @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        List<String> RawSets = new ArrayList<>();
        for (int i = 0; i < runtime; ++i) {
            TwitterStream twitterStream = configAuth(ConsumerKey,ConsumerSecret,AccessToken,AccessSecret);
            FilterQuery tweetFilterQuery=setFilter(filterCond,filterLang);
            //here StatusAdapter or StatusListener can be used, however StatusAdapter automatically creates the unwritten public void methods that are not added to the .addListener for us
            String TimeID=getTime();
            RawSets.add(TimeID);
            CreateTable(TimeID);
            twitterStream.addListener(new StatusAdapter() {
                public void onStatus(Status status) {
                    //inserts data to table, retweet data is essentially useless
                    if (!status.isRetweet()){
                        String TweetContentsLower=status.getText().toLowerCase();
                        InsertToTable(TweetContentsLower,TimeID);
                    }
                }

            });
        //filters data
            twitterStream.filter(tweetFilterQuery);
            Thread.sleep(900000);
            twitterStream.cleanUp();
            twitterStream.shutdown();
        }
        String RawSets1 =listToString(RawSets);
        UpdateSet(TableID, runtime, PublicPrivate, RawSets1, token);
        SortData(TableID.toString(),RawSets1,100);
        return "{\"Success\": \"Stream Successful.\"}";
    }
}
