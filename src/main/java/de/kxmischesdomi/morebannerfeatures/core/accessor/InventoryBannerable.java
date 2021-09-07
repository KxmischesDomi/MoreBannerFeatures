package de.kxmischesdomi.morebannerfeatures.core.accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface InventoryBannerable extends Bannerable {

	/**
	 * @return the slot where the banner is in.
	 * Used for the horse inventories
	 */
	default int getSlot() { return 2; }

}
