package foam.nanos.script.JavaCompiler;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Compiler {

  private javax.tools.JavaCompiler _compiler;
  private List<JavaFileObject> _compilationUnits;
  private Map<String, InMemoryJavaByteCodeFile> _compiledClassData;
  private ClassDataFileManager _classDataFileManager;
  private MapClassLoader _mapClassLoader;

  public Compiler()
  {
    this._compiler = ToolProvider.getSystemJavaCompiler();
    this._compiledClassData = new LinkedHashMap<>();
    this._mapClassLoader = new MapClassLoader(this._compiledClassData);
    this._compilationUnits = new ArrayList<>();
    this._classDataFileManager = new ClassDataFileManager(
      this._compiler.getStandardFileManager(null , null, null),
      this._compiledClassData);

    this._classDataFileManager = new ClassDataFileManager(this._compiler
      .getStandardFileManager(null, null, null),  this._compiledClassData);
  }

  public boolean complie()
  {
    DiagnosticCollector<JavaFileObject> diagnosticsCollector =
      new DiagnosticCollector<>();

    javax.tools.JavaCompiler.CompilationTask task = this._compiler.getTask(null, this._classDataFileManager,
      diagnosticsCollector, null, null, this._compilationUnits);

    boolean success = task.call();
    this._compilationUnits.clear();

    for (Diagnostic<?> diagnostic : diagnosticsCollector.getDiagnostics())
    {
      System.out.println(diagnostic);
      System.out.println();
    }

//    System.out.println(this._compiledClassData);

    return success;
  }

  public void addClass(String className, String code) {
//    String javaFileName = className + ".java";
    JavaFileObject javaFileObject = new InMemoryJavaSourceFile(className, code);
    this._compilationUnits.add(javaFileObject);
  }

  public Class<?> getCompiledClass(String className)
  {
    return this._mapClassLoader.findClass(className);
  }
}
