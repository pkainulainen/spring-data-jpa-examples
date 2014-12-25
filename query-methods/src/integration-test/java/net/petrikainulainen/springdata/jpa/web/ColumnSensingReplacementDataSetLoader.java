package net.petrikainulainen.springdata.jpa.web;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.core.io.Resource;
import java.io.InputStream;
/**
 * This class is a custom DbUnit data set loader that support flat XML data sets. This data set loader
 * adds support for the extra features:
 * <ul>
 * <li>You can use the column sensing feature of DbUnit.</li>
 * <li>You can specify that a column's value is null by using the string [null].</li>
 * </ul>
 * @author Petri Kainulainen
 */
public class ColumnSensingReplacementDataSetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        try (InputStream inputStream = resource.getInputStream()) {
            return createReplacementDataSet(builder.build(inputStream));
        }
    }
    private ReplacementDataSet createReplacementDataSet(FlatXmlDataSet dataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
        replacementDataSet.addReplacementObject("[null]", null);
        return replacementDataSet;
    }
}