package de.kxmischesdomi.morebannerfeatures.common.morebannerfeatures;

import net.minecraft.util.math.Vec3d;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface SideBannerable extends Bannerable {

	default float getXOffset() { return 0;}
	default float getYOffset() { return 0; }
	default float getZOffset() { return 0; }
	default Vec3d getScaleOffset() { return null; }

}
