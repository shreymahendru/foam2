package foam.nanos.script.JavaCompiler;

import foam.core.X;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JITScript {
  private String _code;
  private String _name;

  private final String CLASS_TEMPLATE =
    "import foam.core.X; \n" +
      "%1$s\n" +
      "public class %2$s {\n" +
      "  %3$s\n" +
      "  \n" +
      "  public Object execute(X x) {\n" +
      "    %4$s\n" +
      "  }\n" +
      "}";


  public JITScript(String code, String name) {
    this._code = code;
    this._name = "JITScript" + name.
      replaceAll(" ", "_")
      .replaceAll("-", "")
      .trim();
  }

  public Object runScript(X x) {

    PrintStream defaultOut = System.out;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    System.setOut(new PrintStream(outputStream));

    JITScriptSanitizer sanitizer = new JITScriptSanitizer(this._code);
    sanitizer.sanitize();

    StringBuilder imports = new StringBuilder();

    for(String imp : sanitizer.getImports())
      imports.append(imp).append("\n");

    StringBuilder methods = new StringBuilder();

    for(String method : sanitizer.getMethods())
      methods.append(method).append("\n");

    String sourceToBeCompiled = String.format(this.CLASS_TEMPLATE, imports, this._name, methods, sanitizer.getCode());

    Compiler compiler = new Compiler();

    compiler.addClass(this._name, sourceToBeCompiled);

    compiler.complie();

    Class<?>  cls = compiler.getCompiledClass(this._name);

    Object output = null;

    try {
      Object obj = cls.newInstance();
      output = cls.getMethod("execute", foam.core.X.class).invoke(obj, x);

    } catch (Exception e) {
      e.printStackTrace();
    }


    System.setOut(defaultOut);

    System.out.println(outputStream.toString());
    return output;
  }
}
