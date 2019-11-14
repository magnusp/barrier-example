package barrierexample;

import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/entry")
public interface EntryResource {
	@GET
	@Path("/{barrier}")
	@Produces("text/plain")
	Observable<String> getEntries(@PathParam("barrier") String barrier);

}
