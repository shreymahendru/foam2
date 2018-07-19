package foam.nanos.test;

import foam.core.X;
import foam.core.EmptyX;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.err;

public class SmartTest extends Test {
  public static final String TEST_METHOD_SUFFIX = "Test";
  public static final String CLEANUP_METHOD_SUFFIX = "Cleanup";
  public static final String SETUP_METHOD_SUFFIX = "Setup";

  private Method globalSetupMethod;
  private Method globalCleanupMethod;
  private Method preTestMethod;
  private Method postTestMethod;
  private final Map<String, TestChain> testChains = new HashMap<>();

  protected X x_ = EmptyX.instance();

  public X getX() {
    return x_;
  }

  public void setX(X x) {
    x_ = x;
  }

  @Override
  public void runTest(X x) {
    setX(x);
    prepareTestChains();
    runTestChains();
  }

  private void runTestChains() {
    if ( invokeMethod(globalSetupMethod) ) {
      for ( String test: testChains.keySet() ) {
        TestChain testChain = testChains.get(test);
        runTestChain(testChain);
      }
      invokeMethod(globalCleanupMethod);
    }
  }

  private void runTestChain(TestChain testChain) {

    // don't run when the test Chain if doesn't have a test method
    if ( testChain.test == null ) {
      return;
    }
    if ( invokeMethod(preTestMethod) ) {
      if ( invokeMethod(testChain.setup) )  {
        invokeMethod(testChain.test);
        invokeMethod(testChain.cleanup);
      }
      invokeMethod(postTestMethod);
    }
  }

  // returns false when the invocation was not succesfull, else returns true
  private boolean invokeMethod(Method method) {
    if ( method != null ) {
      try {
        method.invoke(this);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return false;
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        err.format("Invocation of %s failed: %s%n", method.getName(), cause.getMessage());
        return false;
      }
    }
    return true;
  }

  private void prepareTestChains() {
    Class cls = this.getClass();
    Method[] methods = cls.getMethods();

    for ( Method method : methods ) {
      String methodName = method.getName();
      switch ( methodName ) {
        case "runTest":
          break;
        case "preTest":
          preTestMethod = method;
          break;
        case "postTest":
          postTestMethod = method;
          break;
        case "setup":
          globalSetupMethod = method;
          break;
        case "cleanup":
          globalCleanupMethod = method;
          break;
        default:
          insertMethodInTestChain(method);
      }
    }
  }

  private void insertMethodInTestChain(Method method) {
    String methodName = method.getName();
    int methodNameLength = methodName.length();

    if ( methodName.endsWith(TEST_METHOD_SUFFIX) && methodNameLength > TEST_METHOD_SUFFIX.length() ) {
      String prefix =  methodName.substring(0, methodNameLength - TEST_METHOD_SUFFIX.length());
      TestChain testChain = getTestChain(prefix);
      testChain.test = method;
    } else if ( methodName.endsWith(CLEANUP_METHOD_SUFFIX) && methodNameLength > CLEANUP_METHOD_SUFFIX.length() ) {
      String prefix =  methodName.substring(0, methodNameLength - CLEANUP_METHOD_SUFFIX.length());
      TestChain testChain = getTestChain(prefix);
      testChain.cleanup = method;
    } else if ( methodName.endsWith(SETUP_METHOD_SUFFIX) && methodNameLength > SETUP_METHOD_SUFFIX.length() ) {
      String prefix =  methodName.substring(0, methodNameLength - SETUP_METHOD_SUFFIX.length());
      TestChain testChain = getTestChain(prefix);
      testChain.setup = method;
    }
  }

  private TestChain getTestChain(String prefix) {
    TestChain testChain;
    if ( ! testChains.containsKey(prefix) ) {
      testChain = new TestChain();
      testChains.put(prefix, testChain);
    } else {
      testChain = testChains.get(prefix);
    }
    return testChain;
  }
}

class TestChain {
  public Method setup = null;
  public Method test = null;
  public Method cleanup = null;
}