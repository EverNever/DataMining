package preprocessing;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Created by chao on 16/5/28.
 */
public class TestInfoGainAttributeEval {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSource("data/iris.arff");
        Instances instances = dataSource.getDataSet();

        InfoGainAttributeEval info = new InfoGainAttributeEval();
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(3);
        ranker.setThreshold(0.0);

        AttributeSelection attrSelection = new AttributeSelection();
        attrSelection.setEvaluator(info);
        attrSelection.setSearch(ranker);
        attrSelection.setInputFormat(instances);

        Instances newInstances = Filter.useFilter(instances, attrSelection);
        Print.print(newInstances);
    }

}
