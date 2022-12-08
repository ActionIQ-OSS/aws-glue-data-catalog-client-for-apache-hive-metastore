package com.amazonaws.glue.catalog.metastore;

import com.amazonaws.services.glue.AWSGlue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.api.MetaException;

import static com.amazonaws.glue.catalog.util.AWSGlueConfig.AWS_GLUE_DB_CACHE_ENABLE;
import static com.amazonaws.glue.catalog.util.AWSGlueConfig.AWS_GLUE_TABLE_CACHE_ENABLE;

public class AWSGlueMetastoreFactory {

    public AWSGlueMetastore newMetastore(Configuration conf) throws MetaException {
        AWSGlue glueClient = new AWSGlueClientFactory(conf).newClient();
        AWSGlueMetastore defaultMetastore = new DefaultAWSGlueMetastore(conf, glueClient);
        if(isCacheEnabled(conf)) {
            return new AWSGlueMetastoreCacheDecorator(conf, defaultMetastore);
        }
        return defaultMetastore;
    }

    private boolean isCacheEnabled(Configuration conf) {
        boolean databaseCacheEnabled = conf.getBoolean(AWS_GLUE_DB_CACHE_ENABLE, false);
        boolean tableCacheEnabled = conf.getBoolean(AWS_GLUE_TABLE_CACHE_ENABLE, false);
        return (databaseCacheEnabled || tableCacheEnabled);
    }
}