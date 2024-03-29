package io.github.compilerstuck.SortingAlgorithms;

import io.github.compilerstuck.Control.ArrayController;
import io.github.compilerstuck.Control.MainController;

public class MergeSort extends SortingAlgorithm {

    public MergeSort(ArrayController arrayController) {
        super(arrayController);
        this.name = "Merge Sort";
        alternativeSize = arrayController.getLength();
    }

    public void sort() {
        MainController.setCurrentOperation(name);
        startTime = System.nanoTime();

        sort(arrayController, 0, arrayController.getLength() - 1);

        arrayController.addRealTime(System.nanoTime() - startTime);

    }


    private void sort(ArrayController arrayController, int l, int r) {
        if (l < r && run) {
            int m = (l + r) / 2;

            sort(arrayController, l, m);
            sort(arrayController, m + 1, r);

            merge(arrayController, l, m, r);
        }
    }

    private void merge(ArrayController arrayController, int l, int m, int r) {

        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1 && run; ++i) {
            L[i] = arrayController.get(l + i);
        }
        arrayController.addWritesAux(n1);
        for (int j = 0; j < n2 && run; ++j) {
            R[j] = arrayController.get(m + 1 + j);
        }
        arrayController.addWritesAux(n2);


        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2 && run) {
            if (L[i] <= R[j]) {
                arrayController.set(k, L[i]);
                i++;
            } else {
                arrayController.set(k, R[j]);
                j++;
            }
            k++;

            arrayController.addComparisons(1);
            
            delay(new int[]{k});
        }

        k = copyRemainingElements(arrayController, n1, L, i, k);

        copyRemainingElements(arrayController, n2, R, j, k);
    }

    private int copyRemainingElements(ArrayController arrayController, int n1, int[] l, int i, int k) {
        while (i < n1 && run) {
            arrayController.set(k, l[i]);

            arrayController.addWritesAux(1);

            delay(new int[]{k});

            i++;
            k++;
        }
        return k;
    }

}
