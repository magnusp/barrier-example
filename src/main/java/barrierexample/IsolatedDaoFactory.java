package barrierexample;

import rx.Observable;
import rx.schedulers.Schedulers;
import se.fortnox.reactivewizard.db.ConnectionProvider;
import se.fortnox.reactivewizard.db.DbProxy;
import se.fortnox.reactivewizard.db.config.DatabaseConfig;
import se.fortnox.reactivewizard.db.statement.DbStatementFactoryFactory;
import se.fortnox.reactivewizard.json.JsonSerializerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static rx.Observable.just;

@Singleton
public class IsolatedDaoFactory {
	private final DbProxy   dbProxy;
	private final DatabaseConfig databaseConfig;

	@Inject
	public IsolatedDaoFactory(
		DatabaseConfig databaseConfig,
		DbStatementFactoryFactory dbStatementFactoryFactory,
		JsonSerializerFactory jsonSerializerFactory
	) {
		this.databaseConfig = databaseConfig;
		dbProxy = new DbProxy(
			new DatabaseConfig(),
			Schedulers.io(),
			null,
			dbStatementFactoryFactory,
			jsonSerializerFactory
		);
	}

	public <T> Observable<T> get(Class<T> daoCls, int barrier) {
		return just(createProxy(daoCls, barrier));
	}

	private <T> T createProxy(Class<T> daoCls, int barrier) {
		ConnectionProvider connectionProvider = createConnectionProvider(barrier);

		return dbProxy.usingConnectionProvider(connectionProvider, databaseConfig).create(daoCls);
	}

	private ConnectionProvider createConnectionProvider(int barrier) {
		return new BarrierConnectionProvider(databaseConfig, barrier);
	}

	static class BarrierConnectionProvider implements ConnectionProvider {
		private final DatabaseConfig databaseConfig;
		private final int barrier;

		public BarrierConnectionProvider(DatabaseConfig databaseConfig, int barrier) {
			this.databaseConfig = databaseConfig;
			this.barrier = barrier;
		}

		@Override
		public Connection get() {
			try {
				Connection connection = DriverManager.getConnection(databaseConfig.getUrl(), databaseConfig.getUser(), databaseConfig.getPassword());
				configureBarrier(connection, barrier);
				return connection;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		private void configureBarrier(Connection connection, int barrier) throws SQLException{
			try (Statement statement = connection.createStatement()) {
				// application.selected_barrier lasts for the duration of the session
				statement.execute("SET application.selected_barrier=" + barrier);
			}
		}
	}
}
