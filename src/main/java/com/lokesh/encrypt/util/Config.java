/**
 * ***********************************************************************
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

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amogh Chitnis
 */
public enum Config {

    SENDER_EMAIL_ID("sender.email.id"),
    SENDER_ENCRYPTED_PASSWORD("sender.encrypted.password"),
    DECRYPT_REQUIRED("decrypt.required"),
    RECIPIENT_EMAIL_ID("recipient.email.id"),
    MOTION_CAPTURE_FILE_PATH("motion.captured.file.path"),
    MAIL_SMTP_HOST("mail.smtp.host"),
    MAIL_SMTP_PORT("mail.smtp.port"),
    MAIL_SMTP_STARTTLS_ENABLE("mail.smtp.starttls.enable"),
    
    MAIL_SMTP_AUTH("mail.smtp.auth");
    
    private static final Properties props = new Properties();
    private static final String CONFIG_FILE_NAME = "/config/usbcam.properties";
    private static final Logger LOGGER = Logger.getLogger(Config.class.getSimpleName());
    private static final String CONF_FILE_SYS_PROP_NAME = "motion.cam.config";

    //TODO: Basic implementation to read app config. Requires a configuration manager
    static {
        if(false == loadFromFileOnDisk()) {
            loadFromDefaultFileInJar();
        }
    }

    private String key;

    private Config(String key) {
        this.key = key;
    }

    private static boolean loadFromFileOnDisk() {
        try {
            String configFileLoc = System.getProperty(CONF_FILE_SYS_PROP_NAME);
            if (configFileLoc != null) {
                InputStream in = new FileInputStream(configFileLoc);
                props.load(in);
                in.close();
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Could not find system property " + CONF_FILE_SYS_PROP_NAME + 
                        " to load config file. Will use the default config");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not load the config file from disk. Will try to load the default config file from the jar - " + e.getMessage());
        }
        return false;
    }
    
    private static boolean loadFromDefaultFileInJar() {
        try {
            InputStream in = Config.class.getResourceAsStream(CONFIG_FILE_NAME);
            props.load(in);
            in.close();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not load the config file " + CONFIG_FILE_NAME
                    + " from the jar. Config will not work!", e);
        }
        return false;
    }    
    
    public String getKey() {
        return key;
    }

    public String getValue() {
        return props.getProperty(key);
    }

    public String getValue(String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getValueAsInt() {
        return Integer.parseInt(props.getProperty(key));
    }

    public int getValueAsInt(int defaultValue) {
        return Integer.parseInt(props.getProperty(key, "" + defaultValue));
    }

    public boolean getValueAsBoolean() {
        return Boolean.parseBoolean(props.getProperty(key));
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return Boolean.parseBoolean(props.getProperty(key, "" + defaultValue));
    }

}
