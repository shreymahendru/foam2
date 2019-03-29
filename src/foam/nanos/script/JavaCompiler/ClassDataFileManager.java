package foam.nanos.script.JavaCompiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;
import java.util.Map;

class ClassDataFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
  private Map<String, InMemoryJavaByteCodeFile> _compiledClassData;

  protected ClassDataFileManager(StandardJavaFileManager standardJavaFileManager, Map<String, InMemoryJavaByteCodeFile> compiledClassData) {
    super(standardJavaFileManager);
    this._compiledClassData = compiledClassData;
  }

  @Override
  public JavaFileObject getJavaFileForOutput(final Location location,
                                             final String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException
  {

    System.out.println("getting file");
    InMemoryJavaByteCodeFile byteCodeFile = new InMemoryJavaByteCodeFile(className);

    this._compiledClassData.put(className, byteCodeFile);

    return byteCodeFile;
  }
}
