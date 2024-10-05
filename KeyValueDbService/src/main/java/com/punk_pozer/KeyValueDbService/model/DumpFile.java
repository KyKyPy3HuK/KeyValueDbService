package com.punk_pozer.KeyValueDbService.model;

import java.util.List;

public class DumpFile {

    protected DumpMode mode;

    protected List<KeyValueWithTtl> keyValueWithTtlList;


    public DumpFile(){}


    public DumpFile(DumpMode softMode, List<KeyValueWithTtl> keyValueWithTtllist){
        this.mode = softMode;
        this.keyValueWithTtlList = keyValueWithTtllist;
    }


    public DumpMode getMode() {
        return mode;
    }

    public void setMode(DumpMode mode) {
        this.mode = mode;
    }

    public List<KeyValueWithTtl> getKeyValueWithTtlList() {
        return keyValueWithTtlList;
    }

    public void setKeyValueWithTtlList(List<KeyValueWithTtl> keyValueWithTtllist) {
        this.keyValueWithTtlList = keyValueWithTtllist;
    }
}
