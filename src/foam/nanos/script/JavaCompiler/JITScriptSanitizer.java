package foam.nanos.script.JavaCompiler;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JITScriptSanitizer {

  private String _code;
  private ArrayList<String> _imports;
  private ArrayList<String> _methods;

  public String getCode() {
    return _code;
  }

  public ArrayList<String> getImports() {
    return _imports;
  }

  public ArrayList<String> getMethods() {
    return _methods;
  }



  public JITScriptSanitizer(String code) {
    this._code = code;
    this._imports = new ArrayList<>();
    this._methods = new ArrayList<>();
  }

  public void sanitize() {
      this.cleanComments();
      this.extractMethods();
      this.extractImports();
  }


  private void cleanComments() {
    String commentRegex = "^\\s*?(\\/\\/.*$|\\/\\*+[^*]*\\*+(?:[^\\/\\*][^\\*]*\\*+)*\\/$)";
    Pattern pattern = Pattern.compile(commentRegex, Pattern.UNICODE_CASE | Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(this._code);
    String cleaned = matcher.replaceAll("") .trim();
    this._code = cleaned;
  }


  private void extractMethods() {

    String code = this._code;

    Stack<Character> stack = new Stack<>();
    String methodDeclarationRegex = "^\\s*?(public\\s+|protected\\s+|private\\s+|static\\s+|void\\s+)+(?!class\\s+).*(\\n|\\s)*\\{$";
    Pattern pattern = Pattern.compile(methodDeclarationRegex, Pattern.UNICODE_CASE | Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(code);

    while (matcher.find()) {
      String match = matcher.group();
      int index = code.indexOf(match) + match.length();
      stack.push('{');
      while (! stack.empty() && index < code.length()) {
        char character =  code.charAt(index);
        if ( character == '{' )
          stack.push('{');
        else if ( character == '}' )
          stack.pop();
        index ++;
      }

      int startIndex = code.indexOf(match);
      String method = code.substring(startIndex, index);
      this._methods.add(method);
      code = code.replace(method, "");
    }

    this._code = code;
  }

  private void extractImports() {
    String code = this._code;

    String importRegex = "import\\p{javaIdentifierIgnorable}*\\p{javaWhitespace}+(?:static\\p{javaIdentifierIgnorable}*\\p{javaWhitespace}+)?(\\p{javaJavaIdentifierStart}[\\p{javaJavaIdentifierPart}\\p{javaIdentifierIgnorable}]*(?:\\p{javaWhitespace}*\\.\\p{javaWhitespace}*\\*|(?:\\p{javaWhitespace}*\\.\\p{javaWhitespace}*\\p{javaJavaIdentifierStart}[\\p{javaJavaIdentifierPart}\\p{javaIdentifierIgnorable}]*)+(?:\\p{javaWhitespace}*\\.\\p{javaWhitespace}*\\*)?))\\p{javaWhitespace}*;";

    Pattern pattern = Pattern.compile(importRegex);
    Matcher matcher = pattern.matcher(code);

    while (matcher.find()) {
      String match = matcher.group();
      this._imports.add(match);
    }

   this._code =  code.replaceAll(importRegex, "").trim();
  }
}
