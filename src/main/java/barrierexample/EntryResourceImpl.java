package barrierexample;

import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;
import se.fortnox.reactivewizard.jaxrs.WebException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.PathParam;

import java.util.stream.Collectors;

import static rx.Observable.just;


@Singleton
public class EntryResourceImpl implements EntryResource {

	private final EntryDao entryDao;

	@Inject
	public EntryResourceImpl(EntryDao entryDao) {
		this.entryDao = entryDao;
	}

	public Observable<String> getEntries(@PathParam("barrier") String barrier) {
		return entryDao
			.getEntries()
			.map(s -> {
				if(!s.contains(barrier)) {
					throw new WebException(HttpResponseStatus.INTERNAL_SERVER_ERROR, "barrier.crossed");
				}
				return s + "\n";
			})
			.toList()
			.map(strings -> strings.stream().collect(Collectors.joining("")));
	}
}
