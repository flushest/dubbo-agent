package com.alibaba.dubbo.agent.common;

import java.util.regex.Pattern;

/**
 * Constants
 */
public class Constants {

    public static final String GROUP_KEY = "group";

    public static final String INTERFACE_KEY = "interface";

    public static final String COMMA_SEPARATOR = ",";

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");

    public static final String VERSION_KEY = "version";


}
