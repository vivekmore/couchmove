package com.github.couchmove.container;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import lombok.Getter;
import org.junit.ClassRule;
import org.slf4j.LoggerFactory;

/**
 * Created by tayebchlyah on 28/05/2017.
 */
public abstract class AbstractCouchbaseTest {
    public static final String CLUSTER_USER = "Administrator";
    public static final String CLUSTER_PASSWORD = "password";
    public static final String DEFAULT_BUCKET = "default";

    static {
        ((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
    }

    @ClassRule
    public final static CouchbaseContainer couchbaseContainer = initCouchbaseContainer();

    @Getter(lazy = true)
    private final static Bucket bucket = openBucket(DEFAULT_BUCKET);

    private static CouchbaseContainer initCouchbaseContainer() {
        return new CouchbaseContainer()
                .withFTS(false)
                .withIndex(false)
                .withQuery(false)
                .withClusterUsername(CLUSTER_USER)
                .withClusterPassword(CLUSTER_PASSWORD)
                .withNewBucket(DefaultBucketSettings.builder()
                        .enableFlush(true)
                        .name(DEFAULT_BUCKET)
                        .quota(100)
                        .replicas(0)
                        .type(BucketType.COUCHBASE)
                        .build());
    }

    private static Bucket openBucket(String bucketName) {
        CouchbaseCluster cluster = couchbaseContainer.getCouchbaseCluster();
        return cluster.openBucket(bucketName);
    }

    public static void flush() {
        getBucket().bucketManager().flush();
    }
}
