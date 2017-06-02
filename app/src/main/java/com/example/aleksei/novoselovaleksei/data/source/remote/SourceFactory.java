package com.example.aleksei.novoselovaleksei.data.source.remote;

import com.example.aleksei.novoselovaleksei.data.source.remote.common.BaseSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.gazeta.GazetaSource;
import com.example.aleksei.novoselovaleksei.data.source.remote.lenta.LentaSource;

import java.util.ArrayList;
import java.util.List;

public class SourceFactory  {

    private List<BaseSource> sources;

    public SourceFactory() {
        sources = new ArrayList<>();
        sources.add(new LentaSource());
        sources.add(new GazetaSource());
    }

    public List<BaseSource> getSources() {
        return sources;
    }
}
