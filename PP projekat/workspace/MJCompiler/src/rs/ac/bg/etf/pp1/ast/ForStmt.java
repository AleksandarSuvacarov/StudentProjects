// generated with ast extension for cup
// version 0.8
// 26/5/2024 17:16:32


package src.rs.ac.bg.etf.pp1.ast;

public class ForStmt extends Statement {

    private ForBegin ForBegin;
    private DesignatorStatementListPre DesignatorStatementListPre;
    private CondFactorFor CondFactorFor;
    private DesignatorStatementListPost DesignatorStatementListPost;
    private Statement Statement;

    public ForStmt (ForBegin ForBegin, DesignatorStatementListPre DesignatorStatementListPre, CondFactorFor CondFactorFor, DesignatorStatementListPost DesignatorStatementListPost, Statement Statement) {
        this.ForBegin=ForBegin;
        if(ForBegin!=null) ForBegin.setParent(this);
        this.DesignatorStatementListPre=DesignatorStatementListPre;
        if(DesignatorStatementListPre!=null) DesignatorStatementListPre.setParent(this);
        this.CondFactorFor=CondFactorFor;
        if(CondFactorFor!=null) CondFactorFor.setParent(this);
        this.DesignatorStatementListPost=DesignatorStatementListPost;
        if(DesignatorStatementListPost!=null) DesignatorStatementListPost.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForBegin getForBegin() {
        return ForBegin;
    }

    public void setForBegin(ForBegin ForBegin) {
        this.ForBegin=ForBegin;
    }

    public DesignatorStatementListPre getDesignatorStatementListPre() {
        return DesignatorStatementListPre;
    }

    public void setDesignatorStatementListPre(DesignatorStatementListPre DesignatorStatementListPre) {
        this.DesignatorStatementListPre=DesignatorStatementListPre;
    }

    public CondFactorFor getCondFactorFor() {
        return CondFactorFor;
    }

    public void setCondFactorFor(CondFactorFor CondFactorFor) {
        this.CondFactorFor=CondFactorFor;
    }

    public DesignatorStatementListPost getDesignatorStatementListPost() {
        return DesignatorStatementListPost;
    }

    public void setDesignatorStatementListPost(DesignatorStatementListPost DesignatorStatementListPost) {
        this.DesignatorStatementListPost=DesignatorStatementListPost;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForBegin!=null) ForBegin.accept(visitor);
        if(DesignatorStatementListPre!=null) DesignatorStatementListPre.accept(visitor);
        if(CondFactorFor!=null) CondFactorFor.accept(visitor);
        if(DesignatorStatementListPost!=null) DesignatorStatementListPost.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForBegin!=null) ForBegin.traverseTopDown(visitor);
        if(DesignatorStatementListPre!=null) DesignatorStatementListPre.traverseTopDown(visitor);
        if(CondFactorFor!=null) CondFactorFor.traverseTopDown(visitor);
        if(DesignatorStatementListPost!=null) DesignatorStatementListPost.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForBegin!=null) ForBegin.traverseBottomUp(visitor);
        if(DesignatorStatementListPre!=null) DesignatorStatementListPre.traverseBottomUp(visitor);
        if(CondFactorFor!=null) CondFactorFor.traverseBottomUp(visitor);
        if(DesignatorStatementListPost!=null) DesignatorStatementListPost.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStmt(\n");

        if(ForBegin!=null)
            buffer.append(ForBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementListPre!=null)
            buffer.append(DesignatorStatementListPre.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactorFor!=null)
            buffer.append(CondFactorFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementListPost!=null)
            buffer.append(DesignatorStatementListPost.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStmt]");
        return buffer.toString();
    }
}
