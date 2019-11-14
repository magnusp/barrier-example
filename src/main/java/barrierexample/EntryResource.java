package barrierexample;

import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/entry")
public interface EntryResource {
	@GET
	@Path("/{barrier}")
	Observable<List<Entry>> getEntries(@PathParam("barrier") int barrier);

}
