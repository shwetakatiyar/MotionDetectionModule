/**
 * ****************************************************************************
 *
 * INVICARA INC CONFIDENTIAL __________________
 *
 * Copyright (C) [2012] - [2014] INVICARA INC, INVICARA Pte Ltd, INVICARA INDIA
 * PVT LTD All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Invicara Inc and its suppliers, if any. The intellectual and technical
 * concepts contained herein are proprietary to Invicara Inc and its suppliers
 * and may be covered by U.S. and Foreign Patents, patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Invicara Inc.
 */
package com.lokesh.encrypt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Amogh Chitnis
 */
class KeyProvider {
    
    private static final String SECRET_KEY_FILE_LOC = "/secret";
    static final String ALGORITHM = "AES";
    
    private SecretKey key;
    
    private void init() throws CryptoException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                KeyProvider.class.getResourceAsStream(SECRET_KEY_FILE_LOC)))) {
            String encoded = in.readLine();
            byte[] keyStuff = Hex.decodeHex(encoded.toCharArray());
            key = new SecretKeySpec(keyStuff, ALGORITHM);
        } catch (IOException | DecoderException ex) {
            throw new CryptoException("Error occured while loading secret key", ex);
        }
    }
    
    Key getKey() throws CryptoException {
        if(key == null) {
            synchronized(KeyProvider.class) {
                init();
            }
        }
        return key;
    } 
    
    static String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator gen = KeyGenerator.getInstance(ALGORITHM);
        SecretKey sk = gen.generateKey();
        String encoded = Hex.encodeHexString(sk.getEncoded());
        return encoded;
    }
            
}
