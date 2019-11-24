
package com.rameses.gov.etracs;

import test.rptis.FaasDataCaptureLandTest;
import test.entity.EntityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    EntityTest.class,
    FaasDataCaptureLandTest.class,
})
public class ProvinceTestSuite {
    
}
