package com.craftinginterpreters.vox;

import java.util.List;

class VoxFunction extends VoxObject implements Callable {
  final Stmt.Function declaration;
  final Variables closure;

  VoxFunction(Stmt.Function declaration, Variables closure) {
    this.declaration = declaration;
    this.closure = closure;
  }

  VoxFunction bind(VoxObject self) {
    return new VoxFunction(declaration, closure.define("this", self));
  }

  @Override
  public String toString() {
    return declaration.name.text;
  }

  @Override
  public int requiredArguments() {
    return declaration.parameters.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    try {
      Variables locals = closure;
      for (int i = 0; i < declaration.parameters.size(); i++) {
        locals = locals.define(declaration.parameters.get(i), arguments.get(i));
      }

      interpreter.execute(declaration.body, locals);
      return null;
    } catch (Return returnValue) {
      return returnValue.value;
    }
  }
}