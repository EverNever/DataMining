package preprocessing;

import weka.core.Instances;

/**
 * Created by chao on 16/5/28.
 */
public class Print {
    public static void print(Instances instances) {
        for (int i = 0; i < instances.numAttributes(); i++) {
            System.out.println(instances.attribute(i).name() + "\n");
        }
        for (int i = 0; i < instances.numInstances(); i++) {
            System.out.println(instances.instance(i).toString() + "\n");
        }
    }

}
