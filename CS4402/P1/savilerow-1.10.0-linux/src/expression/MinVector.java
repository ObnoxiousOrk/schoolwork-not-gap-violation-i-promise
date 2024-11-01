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


// Turns into a Min at the first opportunity.
// This is a 'safe' function. 

public class MinVector extends ASTNodeC
{
    public static final long serialVersionUID = 1L;
	public MinVector(ASTNode a)
	{
		super(a);
	}
	
	public ASTNode copy()
	{
	    return new MinVector(getChild(0));
	}
	
	public boolean typecheck(SymbolTable st) {
	    if(!getChild(0).typecheck(st))
            return false;
        if(getChild(0).getDimension()!=1) {
            CmdFlags.println("ERROR: Expected one-dimensional matrix in min function: "+this);
            return false;
        }
	    return true;
	}
	
	public ASTNode simplify()
	{
	    ASTNode mat=getChildConst(0);
	    if(mat instanceof CompoundMatrix || mat instanceof EmptyMatrix) {
	        if(mat instanceof EmptyMatrix) {
	            // length=0 so undefined
	            return NumberConstant.make(0); // default value.  
	        }
	        else {
	            ArrayList<ASTNode> ch=mat.getChildren(1);
	            if(mat==getChild(0)) {
	                for(int i=0; i<ch.size(); i++) ch.get(i).setParent(null);
	            }
                return new Min(ch);
            }
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
	    return "min("+getChild(0)+")";
	}
}