package cluster;

/**
 * Created by chao on 16/5/28.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    List<double[]> dataList;
    int clusterNum; // 聚类簇个数
    int dim; // 数据点维度

    public KMeans(int clusterNum) {
        this.clusterNum = clusterNum;
        this.dataList = new ArrayList<double[]>();
    }

    public static void main(String[] args) {
        KMeans kmeans = new KMeans(4);
        kmeans.readData("data/kmeans");//TODO add path
        kmeans.cluster();
    }

    public void readData(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String tempString;
            while ((tempString = in.readLine()) != null) {
                String[] tempList = tempString.split(" ");
                double[] data = new double[tempList.length];
                for (int i = 0; i < tempList.length; i++) {
                    data[i] = Double.parseDouble(tempList[i]);
                }
                dataList.add(data);
            }
            // dim = 2
            dim = dataList.get(0).length;
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cluster() {
        Random rand = new Random();
        double[][] clusterMeans = new double[clusterNum][dim];
        System.out.println("initial point");
        for (int i = 0; i < clusterNum; i++) {
            double[] data = new double[dim];
            for (int j = 0; j < dim; j++) {
                data[j] = rand.nextDouble() * 100;
                System.out.print(data[j] + " ");
            }
            System.out.println("");
            clusterMeans[i] = data;
        }
        boolean isContinue = true;
        while (isContinue) {
            isContinue = false;
            double[][] nextClusterMeans = new double[clusterNum][dim];
            int[] clusterDataNum = new int[clusterNum];
            // initial cluster
            for (int i = 0; i < dataList.size(); i++) {
                double minDis = Double.MAX_VALUE;
                int whoCluster = -1;
                for (int j = 0; j < clusterNum; j++) {
                    double distance = getDist(clusterMeans[j], dataList.get(i));
                    if (distance < minDis) {
                        whoCluster = j;
                        minDis = distance;
                    }
                }
                clusterDataNum[whoCluster]++;
                for (int m = 0; m < dim; m++){
                    nextClusterMeans[whoCluster][m] += dataList.get(i)[m];
                }
            }
            // update cluster means
            for (int i = 0; i < clusterNum; i++) {
                for (int j = 0; j < dim; j++) {
                    if (clusterDataNum[i] != 0) {
                        nextClusterMeans[i][j] /= clusterDataNum[i];
                    } else {
                        nextClusterMeans[i][j] = Math.random() * 100;
                    }
                }
            }
            // stop
            for (int i = 0; i < clusterNum; i++) {
                if (getDist(nextClusterMeans[i], clusterMeans[i]) != 0) {
                    isContinue = true;
                }
            }
            clusterMeans = nextClusterMeans;
        }

        System.out.println("\nfinal point");
        for (int i = 0; i < clusterNum; i++) {
            for (int j = 0; j < dim; j++){
                System.out.print(clusterMeans[i][j] + " ");
            }
            System.out.println("");
        }

        // visual
        List<List<double[]>> clusters = new ArrayList<List<double[]>>();
        for (int n = 0; n < clusterNum; n++) {
            clusters.add(new ArrayList<double[]>());
        }
        for (int n = 0; n < dataList.size(); n++) {
            double minDis = Double.MAX_VALUE;
            int whoCluster = -1;
            for (int m = 0; m < clusterNum; m++) {
                double distance = getDist(clusterMeans[m], dataList.get(n));
                if (distance < minDis) {
                    whoCluster = m;
                    minDis = distance;
                }
            }
            clusters.get(whoCluster).add(dataList.get(n));
        }
        double[][][] datas = new double[clusterNum][][];

        for (int n = 0; n < clusterNum; n++) {
            double[][] cluster = new double[clusters.get(n).size()][];
            for (int m = 0; m < cluster.length; m++) {
                cluster[m] = clusters.get(n).get(m);
            }
            datas[n] = cluster;
        }

        PicUtil.show(datas, clusterNum);
    }

    public double getDist(double[] test, double[] data) {
        double dist = 0;
        double sum = 0;
        for (int i = 0; i < test.length; i++) {
            sum += (test[i] - data[i]) * (test[i] - data[i]);
        }
        dist = Math.sqrt(sum);
        return dist;
    }

}

