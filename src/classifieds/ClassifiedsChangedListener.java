package classifieds;

/**
 * When added as a listener with {@link Classifieds#addListener}, an
 * implementation of this interface is notified whenever the Classifieds
 * change.
 * 
 * @author Erik Strottmann
 */
public interface ClassifiedsChangedListener {
	public void classifiedsUpdated();
}
