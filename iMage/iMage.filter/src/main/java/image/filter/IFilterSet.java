package image.filter;

import java.awt.image.BufferedImage;

/**
 * A Set of Filters
 */
public interface IFilterSet {
/**
     * Applies the filter
     * @param image input image
     * @return copy with filter applyed
     */
    BufferedImage apply(BufferedImage image);

    /**
     * Applies filter on array of images
     * @param images input images
     * @return copies with filter applyed
     */
    BufferedImage[] apply(BufferedImage[] images);
}