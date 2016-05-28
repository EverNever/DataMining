package preprocessing;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

/**
 * Created by chao on 16/5/28.
 */
public class TestReplaceMissingValues {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSource("data/iris.arff");
        Instances instances = dataSource.getDataSet();

        ReplaceMissingValues value = new ReplaceMissingValues();
        value.setInputFormat(instances);

        Instances newInstances = Filter.useFilter(instances, value);
        Print.print(newInstances);
    }

}
