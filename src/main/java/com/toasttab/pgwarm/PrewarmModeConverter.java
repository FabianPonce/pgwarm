package com.toasttab.pgwarm;

import com.beust.jcommander.IStringConverter;
import com.toasttab.pgwarm.db.PrewarmMode;

public class PrewarmModeConverter implements IStringConverter<PrewarmMode> {
    public PrewarmMode convert(String value) {
        for(PrewarmMode mode : PrewarmMode.values()) {
            if(value.equalsIgnoreCase(mode.toString()))
                return mode;
        }

        throw new IllegalArgumentException("Unsupported PrewarmMode: " + value);
    }
}
