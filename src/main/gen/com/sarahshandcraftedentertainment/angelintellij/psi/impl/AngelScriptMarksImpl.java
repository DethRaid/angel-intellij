// This is a generated file. Not intended for manual editing.
package com.sarahshandcraftedentertainment.angelintellij.psi.impl;

import com.sarahshandcraftedentertainment.angelintellij.psi.AngelScriptMarks;
import com.sarahshandcraftedentertainment.angelintellij.psi.AngelScriptVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.sarahshandcraftedentertainment.angelintellij.psi.*;

public class AngelScriptMarksImpl extends ASTWrapperPsiElement implements AngelScriptMarks {

  public AngelScriptMarksImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AngelScriptVisitor visitor) {
    visitor.visitMarks(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AngelScriptVisitor) accept((AngelScriptVisitor)visitor);
    else super.accept(visitor);
  }

}
