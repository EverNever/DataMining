package preprocessing;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

/**
 * Created by chao on 16/5/28.
 */
public class TestNormalize {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSource("data/iris.arff");
        Instances instances = dataSource.getDataSet();

        Normalize normalize = new Normalize();
        normalize.setInputFormat(instances);

        Instances newInstances = Filter.useFilter(instances, normalize);
        Print.print(newInstances);
    }

}
