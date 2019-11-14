package barrierexample;

import rx.Observable;
import se.fortnox.reactivewizard.db.Query;

public interface EntryDao {
	@Query("SELECT data FROM entry")
	Observable<String> getEntries();
}
