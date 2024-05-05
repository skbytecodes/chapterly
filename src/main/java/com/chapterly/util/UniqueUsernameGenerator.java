package com.chapterly.util;

import java.util.HashMap;
import java.util.Map;

public class UniqueUsernameGenerator {
    private Map<String, Integer> usernameCountMap;

    public UniqueUsernameGenerator() {
        usernameCountMap = new HashMap<>();
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        // Create a base username using first name and last name
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();

        // If the base username is already taken, add a counter to make it unique
        int count = usernameCountMap.getOrDefault(baseUsername, 0);
        String uniqueUsername = baseUsername + (count == 0 ? "" : count);

        // Update the count for this base username
        usernameCountMap.put(baseUsername, count + 1);

        return uniqueUsername;
    }

    public static void main(String[] args) {
        UniqueUsernameGenerator generator = new UniqueUsernameGenerator();

        // Example usage
        System.out.println(generator.generateUniqueUsername("John", "Doe"));
        System.out.println(generator.generateUniqueUsername("Jane", "Smith"));
        System.out.println(generator.generateUniqueUsername("John", "Doe")); // This will be "john.doe1"
    }
}

