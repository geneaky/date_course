package me.toy.server.config;

import static me.toy.server.utils.constants.DataSourceType.MASTER;
import static me.toy.server.utils.constants.DataSourceType.SLAVE;

import java.util.Map;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

  public RoutingDataSource(Map<Object, Object> dataSourceMap) {
    super.setTargetDataSources(dataSourceMap);
    super.setDefaultTargetDataSource(dataSourceMap.get(MASTER));
  }

  @Override
  protected Object determineCurrentLookupKey() {
    Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
    boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    Integer currentTransactionIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
    return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? SLAVE : MASTER;
  }
}
