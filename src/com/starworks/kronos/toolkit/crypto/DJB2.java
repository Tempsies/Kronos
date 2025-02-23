package com.starworks.kronos.toolkit.crypto;

public final class DJB2 {
    
    private static final int MAGIC_NUMBER = 0x1505; // 5381

    DJB2() {
    }
    
    public int hash(final byte[] bytes) {
        int hash = MAGIC_NUMBER;
        
        for (int i = 0; i < bytes.length; i++) {
            hash = ((hash << 5) + hash) + bytes[i]; // hash * 33 + c
        }

        return hash;
    }

    public int hash(final String str) {
        int hash = MAGIC_NUMBER;
        
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i); // hash * 33 + c
        }

        return hash;
    }
}
