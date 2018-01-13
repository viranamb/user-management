package com.cloud.constants;

public final class Constants {

    public static final String USERS = "/users";

    public static final String USERS_SECURE = "/secure/users";

    public static final String USER_RESOURCE_PATH = "/{userId}";

    public static final String USER_SIGN_IN = "/sign-in";

    public static final String API_VERSION_1 = "application/vnd.cloud.v1+json";

    // Response messages
    public static final String USER_NOT_FOUND = "User not found";

    public static final String DATABASE_ERROR = "Error encountered when accessing database. Please try again";

    public static final String INVALID_CREDENTIALS = "Invalid sign-in credentials provided. No user exists for the provided inputs";

    public static final String INTERNAL_SERVER_ERROR = "Server encountered an error. Please try again";

    private Constants() {
        // empty constructor for SonarQube analysis
    }
}
