package com.moviebuzz.database.cassandra.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.generated.ManagerFactory;
import info.archinnov.achilles.generated.ManagerFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
/**
 * This is Configuration class to initialize Datastax Cassandra Objects.
 * This Configuration class also initializes Cassandra Achilles ManagerFactory.
 * https://github.com/doanduyhai/Achilles
 */
public class CassandraConfig
{

    @Value("${cassandra.host}")
    private String cassandraHost;

    @Value("${cassandra.port}")
    private int cassandraPort;

    @Value("${cassandra.creds.username}")
    private String userName;

    @Value("${cassandra.creds.password}")
    private String password;

    @Value("${cassandra.cluster.name}")
    private String clusterName;

    @Value("${cassandra.keyspace.name}")
    private String keyspaceName;

    @Value("${cassandra.keyspace.readConsistency}")
    private String readConsistency;

    @Value("${cassandra.keyspace.writeConsistency}")
    private String writeConsistency;

    @Value("${cassandra.cluster.connections.pooling.local.core}")
    private int localCoreConnections;

    @Value("${cassandra.cluster.connections.pooling.local.max}")
    private int localMaxConnections;

    @Value("${cassandra.cluster.connections.pooling.local.maxConnectionsPerHost}")
    private int maxConnectionsPerLocalHost;

    @Value("${cassandra.cluster.connections.pooling.remote.core}")
    private int remoteCoreConnections;

    @Value("${cassandra.cluster.connections.pooling.remote.max}")
    private int remoteMaxConnections;

    @Value("${cassandra.cluster.connections.pooling.remote.maxConnectionsPerHost}")
    private int maxConnectionsPerRemoteHost;

    @Value("${cassandra.cluster.generateSchema}")
    private Boolean generateSchema;


    @Bean(destroyMethod = "close")
    public Cluster getCluster()
    {
        log.info("Initializing Cassandra Cluster..");

        // https://stackoverflow.com/questions/62144579/understand-cassandra-pooling-options
        // -setcoreconnectionsperhost-and-setmaxconnec

        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions
            .setCoreConnectionsPerHost(HostDistance.LOCAL, localCoreConnections)
            .setMaxConnectionsPerHost(HostDistance.LOCAL, localMaxConnections)
            .setCoreConnectionsPerHost(HostDistance.REMOTE, remoteCoreConnections)
            .setMaxConnectionsPerHost(HostDistance.REMOTE, remoteMaxConnections)
            .setMaxRequestsPerConnection(HostDistance.LOCAL, maxConnectionsPerLocalHost)
            .setMaxRequestsPerConnection(HostDistance.REMOTE, maxConnectionsPerRemoteHost);

        PlainTextAuthProvider authProvider = new PlainTextAuthProvider(userName, password);

        // If you don’t explicitly configure the policy, you get the default,
        // which is a datacenter-aware, token-aware policy:
        // .withLoadBalancingPolicy(new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().build
        // ());)
        // auto-discovers the local data-center.

        Cluster cluster = Cluster.builder()
            .addContactPoints(cassandraHost.split(","))
            .withPort(cassandraPort)
            .withAuthProvider(authProvider)
            .withClusterName(clusterName)
            .withPoolingOptions(poolingOptions)
            .withoutJMXReporting()
            .build();
        cluster.init();

        cluster.connect().execute("CREATE KEYSPACE IF NOT EXISTS " + keyspaceName
            + " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");

        return cluster;
    }

    @Bean(destroyMethod = "shutDown")
    public ManagerFactory init()
    {
        final ManagerFactory factory = ManagerFactoryBuilder
            .builder(getCluster())
            .doForceSchemaCreation(generateSchema)
            .withDefaultKeyspaceName(keyspaceName)
            .validateSchema(false)
            .withDefaultReadConsistency(ConsistencyLevel.valueOf(readConsistency))
            .withDefaultWriteConsistency(ConsistencyLevel.valueOf(writeConsistency))
            .build();

        return factory;
    }

    @Bean
    public Session session()
    {
        return getCluster().connect(keyspaceName);
    }


}
