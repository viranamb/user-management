package com.capitalone.migration.constants;

public final class Constants {

    public static final String PLAYLIST_RESOURCE_PATH = "/playlists";

    public static final String CONTENT_IDENTIFIER = "contentIdentifier";

    public static final String COUNTRY_CODE = "countryCode";

    public static final String CONTENT = "content";

    public static final String PREROLL = "preroll";

    public static final String COUNTRIES = "countries";

    public static final String LANGUAGE = "language";

    public static final String ASPECT = "aspect";

    // Response messages
    public static final String INVALID_DATA_FOR_PLAYLIST = "Playlist cannot be created for provided inputs";

    public static final String INVALID_CONTENT_IDENTIFIER = "Invalid content identifier passed";

    public static final String INVALID_COUNTRY_CODE = "Invalid country code passed";

    public static final String LEGAL_PLAYLIST_NOT_POSSIBLE = "No legal playlist possible because the Pre-Roll Video isn't compatible with the aspect ratio of the Content Video";

    private Constants() {
        // empty constructor for SonarQube analysis
    }
}
