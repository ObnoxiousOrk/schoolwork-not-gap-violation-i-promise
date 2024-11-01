package savilerow;
/*

    Savile Row http://savilerow.cs.st-andrews.ac.uk/
    Copyright (C) 2014-2021 Peter Nightingale
    
    This file is part of Savile Row.
    
    Savile Row is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    Savile Row is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with Savile Row.  If not, see <http://www.gnu.org/licenses/>.

*/


import java.util.*;


// Turns into a Times at the first opportunity.

public class TimesVector extends ASTNodeC
{
    public static final long serialVersionUID = 1L;
	public TimesVector(ASTNode a)
	{
		super(a);
	}
	
	public ASTNode copy()
	{
	    return new TimesVector(getChild(0));
	}
	
	public boolean typecheck(SymbolTable st) {
	    if(!getChild(0).typecheck(st))
            return false;
        if(getChild(0).getDimension()!=1) {
            CmdFlags.println("ERROR: Expected one-dimensional matrix in product function: "+this);
            return false;
        }
	    return true;
	}
	
	public ASTNode simplify()
	{
	    if(getChild(0) instanceof Identifier && getChild(0).getCategory()==ASTNode.Constant) {
	        ASTNode cm=getChildConst(0);
	        long acc=1L;
	        for(int i=1; i<cm.numChildren(); i++) {
	            acc*= cm.getChild(i).getValue();
	        }
	        return NumberConstant.make(acc);
	    }
	    if(getChild(0) instanceof CompoundMatrix || getChild(0) instanceof EmptyMatrix) {
	        ASTNode[] ch=getChild(0).getChildrenArray(1);
	        for(int i=0; i<ch.length; i++) {
	            ch[i].setParent(null);
	        }
	        return new Times(ch);
	    }
	    return null;
	}
	
	public Intpair getBounds() {
	    return new Intpair(Long.MIN_VALUE, Long.MAX_VALUE);
	}
	public PairASTNode getBoundsAST() {
	    return new PairASTNode(new NegInfinity(), new PosInfinity());
	}
	
	public boolean isNumerical() {
        return true;
    }
    
	public String toString() {
	    return "product("+getChild(0)+")";
	}
}