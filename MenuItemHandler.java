package rssreader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

public class MenuItemHandler implements EventHandler<ActionEvent> {

    private final RSSReader reader;

    public MenuItemHandler(RSSReader reader) {
        this.reader = reader;
    } // MenuItemHandler( Canvas )

    @Override
    public void handle(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String id = menuItem.getId();
        this.reader.readChannel(id, this.reader.getTopLevel());
    } // handle( ActionEvent )

} // MenuItemHandler
