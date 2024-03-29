// TC: O(1) for all operations

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class PhoneDirectory {

    Set<Integer> availableNumbers;
    LinkedList<Integer> queue;

    public PhoneDirectory(int maxNumbers) {
        availableNumbers = new HashSet<>();
        queue = new LinkedList<>();

        for (int i = 0; i < maxNumbers; i++) {
            availableNumbers.add(i);
            queue.add(i);
        }
    }

    public int get() {
        if (availableNumbers.size() == 0) {
            return -1;
        }

        int val = queue.getFirst();
        queue.removeFirst();

        availableNumbers.remove(val);
        return val;
    }

    public boolean check(int number) {
        return availableNumbers.contains(number);
    }

    public void release(int number) {
        // number is already in the pool
        if (availableNumbers.contains(number)) {
            return;
        }

        availableNumbers.add(number);
        queue.add(number);
    }
}

/**
 * Your PhoneDirectory object will be instantiated and called as such:
 * PhoneDirectory obj = new PhoneDirectory(maxNumbers);
 * int param_1 = obj.get();
 * boolean param_2 = obj.check(number);
 * obj.release(number);
 */