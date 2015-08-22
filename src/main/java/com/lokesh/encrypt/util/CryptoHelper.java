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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Amogh Chitnis
 */
public class CryptoHelper {
    
    private static final KeyProvider keyProvider = new KeyProvider();
    
    public static String encrypt(String toEncrypt) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(KeyProvider.ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keyProvider.getKey());
            byte[] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());
            String encoded = Hex.encodeHexString(encryptedBytes);
            return encoded;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error occured while encrypting", ex);
        }
    }
    
    public static String decrypt(String toDecrypt) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(KeyProvider.ALGORITHM);
            byte[] encryptedBytes = Hex.decodeHex(toDecrypt.toCharArray());
            cipher.init(Cipher.DECRYPT_MODE, keyProvider.getKey());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decrypted = new String(decryptedBytes);            
            return decrypted;
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | DecoderException | 
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new CryptoException("Error occured while decrypting", ex);
        }
    }

}
