// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class CondFactor implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private ExprRelopList ExprRelopList;

    public CondFactor (ExprRelopList ExprRelopList) {
        this.ExprRelopList=ExprRelopList;
        if(ExprRelopList!=null) ExprRelopList.setParent(this);
    }

    public ExprRelopList getExprRelopList() {
        return ExprRelopList;
    }

    public void setExprRelopList(ExprRelopList ExprRelopList) {
        this.ExprRelopList=ExprRelopList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprRelopList!=null) ExprRelopList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprRelopList!=null) ExprRelopList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprRelopList!=null) ExprRelopList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactor(\n");

        if(ExprRelopList!=null)
            buffer.append(ExprRelopList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactor]");
        return buffer.toString();
    }
}
