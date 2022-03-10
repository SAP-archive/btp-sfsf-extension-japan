package com.sap.sfsf.reshuffle.applicants.backend.config;

import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;

import org.springframework.stereotype.Component;

import cds.gen.CdsModel_;
import cds.gen.reshuffleservice.Configs;

@Component
public class ConfigService {

    private PersistenceService db;

    ConfigService(PersistenceService db) {
        this.db = db;
    }

    public Configs getConfig() throws EmptyConfigException {

        Result configResult = db.run(Select.from(CdsModel_.CONFIGS).columns(c -> c._all()));
        Configs ret = configResult.first(Configs.class).orElse(null);

        if (ret == null)
            throw new EmptyConfigException();

        return ret;
    }
}
