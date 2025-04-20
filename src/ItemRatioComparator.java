import java.util.Comparator;

class ItemRatioComparator implements Comparator<Item> {
    @Override
    public int compare(Item item1, Item item2) {
        // Sort in descending order of ratio
        // Double.compare is safer than subtraction for floating points
        return Double.compare(item2.ratio, item1.ratio);
    }
}