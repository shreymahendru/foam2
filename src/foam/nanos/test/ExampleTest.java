package foam.nanos.test;

import foam.nanos.script.ScriptStatus;
import foam.nanos.test.Test;

public class ExampleTest extends Test {

  public ExampleTest() {
    this.setId("ExampleTest");
    this.setEnabled(true);
    this.setDescription("This is an example java test.");
    this.setCode("serverScript");
    this.setJavaTestFile(this.getClass().getName());
    this.setStatus(ScriptStatus.SCHEDULED);
  }

  @Override
  public void runTest() {
    test(1 == 1, "This works!");
  }
}
