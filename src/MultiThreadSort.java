

public class MultiThreadSort implements Runnable{
    private String[] unsorted, sorted;
    public MultiThreadSort(String[] unsorted) {
        this.unsorted = unsorted;
    }

    public void run() {
        int middle;
        String[] left, right;

        if ( unsorted.length <= 1 ) {
            sorted = unsorted;
        } else {

            middle = unsorted.length / 2;
            left = new String[middle];
            right = new String[unsorted.length - middle];

            System.arraycopy(unsorted, 0, left, 0, middle);
            System.arraycopy(unsorted, middle, right, 0, unsorted.length - middle);
            PartOfArray leftSort = new PartOfArray(left);
            PartOfArray rightSort = new PartOfArray(right);
            leftSort.sort();
            rightSort.sort();

            sorted = PartOfArray.merge(leftSort.getSorted(), rightSort.getSorted());
        }
    }
    public String[] getSorted() {
        return sorted;
    }
}