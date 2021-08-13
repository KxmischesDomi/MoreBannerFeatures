package de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface InventoryBannerable extends Bannerable {

	/**
	 * @return the slot where the banner is in.
	 * Used for the horse inventory
	 */
	default int getSlot() { return 2; }

}
