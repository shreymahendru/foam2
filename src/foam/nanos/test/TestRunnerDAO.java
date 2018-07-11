package foam.nanos.test;

import foam.dao.DAO;
import foam.core.FObject;
import foam.core.X;
import foam.nanos.test.Test;
import foam.nanos.script.ScriptRunnerDAO;

public class TestRunnerDAO extends ScriptRunnerDAO {

  public TestRunnerDAO(DAO delegate) {
    super(delegate);
  }

  @Override
  public FObject put_(final X x, FObject obj) {
    Test test = (Test) obj;
    if ( test.getJavaTestFile() != null ) {
      try {
        Class testClass = Class.forName(test.getJavaTestFile());
        obj = (FObject) testClass.newInstance();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return super.put_(x, obj);
  }
}
