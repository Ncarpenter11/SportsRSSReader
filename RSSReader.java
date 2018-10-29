package rssreader;

/**
 * This class reads, and displays RSS Feeds From multiple sports news sources
 * Once I figure out how to use the link, I'll be done.
 * 
 * @author Noah Carpenter
 * @Version April 9, 2018
 */

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class RSSReader extends Application {

    private final int WIDTH = 768;
    private final int HEIGHT = 512;

    private final List<String> titles = new ArrayList<>();
    private final List<String> description = new ArrayList<>();
    private final List<String> link = new ArrayList<>();
    private final List<String> publicationDate = new ArrayList<>();
    private final List<String> author = new ArrayList<>();


    private final Random rng = new Random();

    /**
    * Here we establish the ESPN feed
    */
    private final String ESPN_NAME = "ESPN";
    private final String ESPN_URL
            = "http://www.espn.com/espn/rss/news";

    /**
    * Here we establish the Fox Sports feed
    */
    private final String FOX_SPORTS_NAME = "FOX Sports";
    private final String FOX_SPORTS_URL
            = "https://api.foxsports.com/v1/rss?partnerKey=zBaFxRyGKCfxBagJG9b8pqLyndmvo7UU";

    /**
    * Here we establish the CBS Sports feed
    */
    private final String CBS_SPORTS_NAME = "CBS Sports";
    private final String CBS_SPORTS_URL
            = "https://www.cbssports.com/rss/headlines/";
    
    /**
    * Here we establish the Sports Illustrated feed
    */
    private final String SPORTS_ILLUSTRATED_NAME = "Sports Illustrated";
    private final String SPORTS_ILLUSTRATED_URL
            = "https://www.si.com/rss/si_topstories.rss.";

    private TextFlow textArea = new TextFlow();
    private BorderPane topLevel = new BorderPane();

    /**
    * This is where we initialize the window
    */
    @Override
    public void init() {
        readChannel(ESPN_URL, getTopLevel());
    } // RSSReader()

    /**
    * This is where the majority of the activity happens in the program
    * First we call the border pane, then the Menu, then we put the RSS 
    * Feeds into the program, and then we show the information itself.
     * @param stage
    */
    @Override
    public void start(Stage stage) {

        getTopLevel().getStyleClass().add("border-pane");

        //topLevel.setCenter(textArea);

        MenuItemHandler handler = new MenuItemHandler(this);

        MenuBar menuBar = new MenuBar();
        Menu depthOfRecursion = new Menu("Channel");
        menuBar.getMenus().add(depthOfRecursion);

        MenuItem channel0 = createMenuItem(ESPN_NAME,
                ESPN_URL, handler);
        MenuItem channel1 = createMenuItem(FOX_SPORTS_NAME,
                FOX_SPORTS_URL, handler);
        MenuItem channel2 = createMenuItem(CBS_SPORTS_NAME,
                CBS_SPORTS_URL, handler);
        MenuItem channel3 = createMenuItem(SPORTS_ILLUSTRATED_NAME,
                SPORTS_ILLUSTRATED_URL, handler);

        depthOfRecursion.getItems().addAll(
                channel0,
                channel1,
                channel2,
                channel3);

        getTopLevel().setTop(menuBar);

        Scene scene = new Scene(getTopLevel(), WIDTH, HEIGHT);
        scene.getStylesheets().add(RSSReader.class
                .getResource("RSSReader.css")
                .toExternalForm());

        stage.setTitle("Sports News RSS Feed");
        stage.setScene(scene);
        stage.show();
    } // start( Stage )

    /**
    * This pulls up the bar we created above
    */
    private MenuItem createMenuItem(String name,
            String url, MenuItemHandler handler) {
        MenuItem result = new MenuItem(name);
        result.setId(url);
        result.setOnAction(handler);
        return result;
    } // createMenuItem( String )

    /**
    *  We read the URL, and we go on to return the title, description,
    * publication date, and the link. After that, this puts the previous
    * information onto the screen.
     * @param url
     * @param topLevel
    */
    public void readChannel(String url, BorderPane topLevel) {
        boolean ok = false;
        try {
            URL feedUrl = new URL(url);

            SyndFeedInput input = new SyndFeedInput();
            XmlReader reader = new XmlReader(feedUrl);
            SyndFeed feed = input.build(reader);

            this.titles.clear();
            this.description.clear();
            this.publicationDate.clear();
            this.link.clear();
            for (Object o : feed.getEntries()) {
                if (o instanceof SyndEntryImpl) {
                    SyndEntryImpl e = (SyndEntryImpl) o;
                    this.titles.add(e.getTitle());
                    this.description.add(e.getDescription().getValue());
                    this.publicationDate.add(e.getPublishedDate().toString());
                    this.link.add(e.getLink());
                    
                    // Here is how to get other kinds
                    // of information from the RSS feed
                    System.out.println("Description = "
                            + e.getDescription().getValue());
                    System.out.println("Link =  "
                            + e.getLink());
                    System.out.println("Publication Date = "
                            + e.getLink());
                } // if
            } // for

            ok = true;
        } // try
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        } // catch( Exception )

        if (!ok) {
            System.out.println();
            System.out.println("FeedReader reads and prints "
                    + "any RSS/Atom feed type.");
            System.out.println("The first parameter must be "
                    + "the URL of the feed to read.");
            System.out.println();
        } // if

        textArea = new TextFlow();
        
        /**
         * this for loop creates the format for the text in the RSS
         */
        for (int i=0; i < titles.size(); i++) {
            Text Title = new Text(titles.get(i).trim() + "\n\n");
            Text Description = new Text(description.get(i).trim() + "\n\n");
            Text PublishedDate = new Text(publicationDate.get(i) + "\n\n");
            Hyperlink Link = new Hyperlink(link.get(i) + "\n\n");
            
            Title.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
            Description.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            PublishedDate.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            Link.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            this.textArea.getChildren().addAll(Title, Description,
                    PublishedDate, Link);

        } // for
        
        textArea.setMaxWidth(WIDTH - 25);
        ScrollPane scrollPane = new ScrollPane(textArea);
        topLevel.setCenter(scrollPane);
    } // readChannel( String )

    /**
    * This is the main method
     * @param args
    */
    public static void main(String[] args) {
        launch(args);
    } // main( String [] )

    /**
     This returns the top level, where the channel bar is located
     * @return 
    */
    public BorderPane getTopLevel() {
        return topLevel;
    } //getTopLevel()

} // RSSReader
