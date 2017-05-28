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
        sources.add(new GazetaSource());
        sources.add(new LentaSource());
    }

    public List<BaseSource> getSources() {
        return sources;
    }
}
