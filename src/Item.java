class Item {
    int weight;
    int value;
    int index;
    double ratio; // Value-to-weight ratio

    public Item(int weight, int value, int index) {
        this.weight = weight;
        this.value = value;
        this.index = index;
        // Calculate ratio, handle potential division by zero (though weight should > 0)
        if (weight > 0) {
            this.ratio = (double) value / weight;
        } else {
            // Assign a very high ratio if value > 0 and weight is 0 (infinite density)
            // Assign 0 if both are 0. Adjust logic as needed for specific constraints.
            this.ratio = (value > 0) ? Double.POSITIVE_INFINITY : 0.0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(index);
    }
}

