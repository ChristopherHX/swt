package image.filter;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * A Set of Filters
 */
public class FilterSet implements IFilter {
    /**
     * Filters to Sequencally apply to each pixel
     */
    private List<IFilter2> filters;
    /**
     * Applies the filter
     * @param image input image
     * @return copy with filter applyed
     */
    public BufferedImage apply(BufferedImage image) {
        return null;
    }

    /**
     * Applies filter on array of images
     * @param images input images
     * @return copies with filter applyed
     */
    public BufferedImage[] apply(BufferedImage[] images) {
        return null;
    }
}