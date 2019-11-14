package barrierexample;

import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;
import se.fortnox.reactivewizard.jaxrs.WebException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.PathParam;
import java.util.List;

@Singleton
public class EntryResourceImpl implements EntryResource {
	private final IsolatedDaoFactory daoFactory;

	@Inject
	public EntryResourceImpl(IsolatedDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public Observable<List<Entry>> getEntries(@PathParam("barrier") int barrier) {
		return daoFactory.get(EntryDao.class, barrier)
			.concatMap(entryDao -> {
				return entryDao
					.getEntries()
					.map(entry -> {
						if (entry.getBarrier() != barrier) {
							throw new WebException(HttpResponseStatus.INTERNAL_SERVER_ERROR, "barrier.crossed");
						}
						return entry;
					})
					.toList();
			});

	}
}
