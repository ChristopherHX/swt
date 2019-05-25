package image.filter;

/**
 * Filters image
 */
public interface IFilter2 {
    /**
     * Apply pixel based rgb filter, easy chainloading
     * @param x Coordinate
     * @param y Coordinate
     * @param orginal unmodified rgb color
     * @return copy of filtered color from orginal
     */
    int[] getRGB(int x, int y, int[] orginal);
}