
package com.shivam.keygenerationservice.keygen;

import com.shivam.keygenerationservice.model.KeyEntry;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class KeyGenerationTask extends RecursiveTask<Set<KeyEntry>> {
    private static final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALPHA_NUMERIC = UPPER_ALPHABETS + LOWER_ALPHABETS + NUMBERS;

    // specify length of random string
    private static final int LENGTH = 8;
    private long count;
    private long total;

    public KeyGenerationTask(long count, long total) {
        this.count = count;
        this.total = total;
    }

    @Override
    protected Set<KeyEntry> compute() {
        if (count <= (total/4)) {
            Set<KeyEntry> set = new HashSet<>();
            for (int i = 0; i < count; i++) {
                set.add(new KeyEntry(getKey(), true));
            }
            return set;
        }
        long mid = count / 2;
        RecursiveTask<Set<KeyEntry>> recursiveTask = new KeyGenerationTask(mid, total);
        recursiveTask.fork();
        Set<KeyEntry> set1 = new KeyGenerationTask(count - mid, total).compute();
        Set<KeyEntry> set2 = recursiveTask.join();
        set1.addAll(set2);
        return set1;
    }

    private String getKey() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < LENGTH; i++) {
            // generate random index number
            int index = random.nextInt(ALPHA_NUMERIC.length());
            // get character specified by index from the string
            char randomChar = ALPHA_NUMERIC.charAt(index);
            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
