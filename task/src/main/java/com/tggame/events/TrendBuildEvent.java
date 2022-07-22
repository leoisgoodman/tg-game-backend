package com.tggame.events;

import com.tggame.open.entity.OpenRecord;
import org.springframework.context.ApplicationEvent;

public class TrendBuildEvent extends ApplicationEvent {

    private OpenRecord openRecord;

    public TrendBuildEvent(Object source, OpenRecord openRecord) {
        super(source);
        this.openRecord = openRecord;
    }

    public OpenRecord getOpenRecord() {
        return openRecord;
    }
}
