package App;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class EigenExample {
    public static double[] addMatrix(double[][] matrixData) {
        // Определение матрицы парных сравнений

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        // Расчет собственных значений и векторов
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);
        double[] realEigenvalues = eigenDecomposition.getRealEigenvalues();
        double[][] eigenvectors = eigenDecomposition.getV().getData();

        // Поиск максимального собственного значения и соответствующего вектора
        double maxEigenvalue = Double.NEGATIVE_INFINITY;
        int indexMax = -1;
        for (int i = 0; i < realEigenvalues.length; i++) {
            if (realEigenvalues[i] > maxEigenvalue) {
                maxEigenvalue = realEigenvalues[i];
                indexMax = i;
            }
        }

        // Собственный вектор для максимального собственного значения
        double[] maxEigenvector = new double[eigenvectors.length];
        for (int i = 0; i < eigenvectors.length; i++) {
            maxEigenvector[i] = eigenvectors[i][indexMax];
        }

        // Нормализация собственного вектора
        double[] arr = new double[maxEigenvector.length];
        double sum = 0;
        for (double v : maxEigenvector) {
            sum += Math.abs(v); // Используем абсолютные значения для нормализации
        }
        int i = 0;
        System.out.println("Normalized Weights:");
        for (double v : maxEigenvector) {
            arr[i] = Math.abs(v) / sum;
            i++;
        }

        return arr;
    }
}