package com.online.banking.card.util;

import java.util.UUID;

public class UUIDGenerator {
    public static String generateUniqueAccountNumber() {
        return String.format("%016d", (int) (Math.random() * 1_000_000_000));
    }

}

