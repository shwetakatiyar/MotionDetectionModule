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

/**
 *
 * @author Amogh Chitnis
 */
public class CryptoException extends Exception {

    /**
     * Creates a new instance of <code>CryptoException</code> without detail
     * message.
     */
    public CryptoException() {
    }

    /**
     * Constructs an instance of <code>CryptoException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CryptoException(String msg) {
        super(msg);
    }
    
    public CryptoException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
}
