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

// Remove all mappers, replace with standard types. 

public class TransformRemoveMappers extends TreeTransformerBottomUpNoWrapper
{
    public TransformRemoveMappers() {
        super(null);
    }
    
    protected NodeReplacement processNode(ASTNode curnode)
	{
	    if(curnode instanceof MultiplyMapper) {
	        return new NodeReplacement(new Times(curnode.getChild(0), curnode.getChild(1)));
        }
        if(curnode instanceof ShiftMapper) {
            return new NodeReplacement(new WeightedSum(curnode.getChild(0), curnode.getChild(1)));
        }
        return null;
    }
}
