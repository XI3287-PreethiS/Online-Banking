package com.online.banking.account.util;
import java.util.UUID;

public class UUIDGenerator {
        public static String generateUniqueAccountNumber() {
            return String.format("%011d", (int) (Math.random() * 1_000_000_000));
        }

}

