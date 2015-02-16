package com.globalforge.infix;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*-
 The MIT License (MIT)

 Copyright (c) 2015 Global Forge LLC

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
/**
 * The definition of a repeating group.
 * 
 * @author Michael
 */
class FixRepeatingGroup {
    protected final String groupId;
    protected final String groupDelim;
    protected final Set<String> memberSet = new HashSet<String>();
    protected final Set<String> grpReferenceSet = new HashSet<String>();

    /**
     * A repeating group is defined principally by it's id and delimiter.
     * 
     * @param id The tag in the fix spec that is used to indicate the number of
     * groups present in a particular repeating group (e.g., NoContraBrokers
     * (Tag = 382)).
     * @param delim The first tag in the repeating group. Used to determine when
     * a group repeats (e.g., ContraGroup (Tag = 375)).
     */
    FixRepeatingGroup(String id, String delim) {
        groupId = id;
        groupDelim = delim;
    }

    /**
     * @return String The tag in the fix spec that is used to indicate the
     * number of groups present in a particular repeating group (e.g.,
     * NoContraBrokers (Tag = 382)).
     */
    String getId() {
        return groupId;
    }

    /**
     * @return String The first tag in the repeating group. Used to determine
     * when a group repeats (e.g., ContraGroup (Tag = 375)).
     */
    String getDelimiter() {
        return groupDelim;
    }

    /**
     * Determines if a tag is part of this repeating group.
     * 
     * @param tagNum The tag to check
     * @return boolean if true.
     */
    boolean containsMember(String tagNum) {
        return memberSet.contains(tagNum);
    }

    /**
     * Determines if a tag is part of this repeating group.
     * 
     * @param tagNum The tag to check
     * @return boolean if true.
     */
    boolean containsNestedGrp(String tagNum) {
        return grpReferenceSet.contains(tagNum);
    }

    /**
     * Returns the set of all members of a repeating group. This does not
     * include the id tag but does include the delimiter tag.
     * 
     * @return Set<String> The tag members belonging to this group.
     */
    Set<String> getMemberSet() {
        return Collections.unmodifiableSet(memberSet);
    }
}
