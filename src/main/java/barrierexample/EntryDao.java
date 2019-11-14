package barrierexample;

import rx.Observable;
import se.fortnox.reactivewizard.db.Query;

public interface EntryDao {
	@Query("SELECT * FROM entry")
	Observable<Entry> getEntries();
}
