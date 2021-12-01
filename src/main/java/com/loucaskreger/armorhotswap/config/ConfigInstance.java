package com.loucaskreger.armorhotswap.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigInstance {

    public boolean preventCurses;
    public List<String> itemBlacklist;

    public ConfigInstance() {
        preventCurses = true;
        itemBlacklist = new ArrayList();
    }

}