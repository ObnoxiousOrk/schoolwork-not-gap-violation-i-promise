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

import java.util.ArrayList;
import java.util.HashMap;

//  Table to a set of element constraints. 

public class TransformTableToElement extends TreeTransformerBottomUpNoWrapper
{
    public TransformTableToElement(Model _m) { super(_m); }
    
    protected NodeReplacement processNode(ASTNode curnode)
	{
	    if(curnode instanceof Table)
        {
            ASTNode tab=curnode.getChildConst(1);
            
            //boolean targetbase1=CmdFlags.getFlatzinctrans() || CmdFlags.getMinizinctrans();
            int numTups=tab.numChildren()-1;
            int numVars=curnode.getChild(0).numChildren()-1;
            
            ASTNode idx=m.global_symbols.newAuxiliaryVariable(1, numTups);
            
            ArrayList<ASTNode> cons=new ArrayList<ASTNode>();
            
            for(int i=1; i<=numVars; i++) {
                //  Slice the column
                ArrayList<ASTNode> idxs=new ArrayList<ASTNode>();
                idxs.add(new IntegerDomain(new Range(null, null)));
                idxs.add(NumberConstant.make(i));
                
                ASTNode sl=new MatrixSlice(m, curnode.getChild(1), idxs);
                
                cons.add(new ToVariable(new ElementOne(sl, idx), curnode.getChild(0).getChild(i)));
            }
            
            return new NodeReplacement(new And(cons));
        }
        return null;
    }
}

