package service;

import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;

// Configuration file for application
public class UniLinkConfig {
    public static final String POST_TYPE[] = { "Event", "Sale", "Job" };
    public static final String POST_TYPE_FILTER[] = { "All", "Event", "Sale", "Job" };
    public static final String STATUS[] = { "Closed", "Open" };
    public static final String STATUS_FILTER[] = { "All", "Closed", "Open" };
    public static final String USER_FILTER[] =  { "All", "My"};
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final String DEFAULT_IMAGE_PATH = "images/";
    public static final String DEFAULT_SUMMARY_IMAGE = "default.png";
    public static final int MAX_IMAGE_SIZE = 250*1024;
    public static final Color EVENT_BG = Color.rgb(224, 255, 255);
    public static final Color SALE_BG = Color.rgb(255, 192, 203);
    public static final Color JOB_BG = Color.rgb(255, 255, 153);
    public static final String RESPONSE_UNIT = "$";

}
