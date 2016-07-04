//
// Copyright (C) 2010 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

package gov.nasa.jpf.util;

import gov.nasa.jpf.JPFException;
import java.util.NoSuchElementException;

/**
 * BitSet like interface for fixed size bit sets
 * 
 * We keep this as an interface so that we can have java.util.BitSet
 * subclasses as implementations
 */
public interface FixedBitSet extends Cloneable, IntSet {

  void set (int i);
  void set (int i, boolean val);
  boolean get (int i);
  void clear (int i);
  
  int nextClearBit (int fromIndex);
  int nextSetBit (int fromIndex);

  boolean isEmpty();
  int size();
  int cardinality();
  int length();
  int capacity();
  
  void clear();
  
  void hash (HashData hd);
  
  FixedBitSet clone();
}

/**
 * this is the base class for our non java.util.BitSet based FixedBitSet implementations
 */
abstract class AbstractFixedBitSet implements FixedBitSet {
  
  class SetBitIterator implements IntIterator {
    int cur = 0;
    int nBits;
    
    @Override
    public void remove() {
      if (cur >0){
        clear(cur-1);
      }
    }

    @Override
    public boolean hasNext() {
      return nBits < cardinality;
    }

    @Override
    public int next() {
      if (nBits < cardinality){
        int idx = nextSetBit(cur);
        if (idx >= 0){
          nBits++;
          cur = idx+1;
        }
        return idx;
        
      } else {
        throw new NoSuchElementException();
      }
    }
  }

  
  protected int cardinality;
  
  public AbstractFixedBitSet clone(){
    try {
      return (AbstractFixedBitSet) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new JPFException("BitSet64 clone failed");
    }  
  }
  
  public void set (int i, boolean val){
    if (val) {
      set(i);
    } else {
      clear(i);
    }
  }

  public int cardinality() {
    return cardinality;
  }

  public boolean isEmpty() {
    return (cardinality == 0);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('{');

    boolean first = true;
    for (int i=nextSetBit(0); i>= 0; i = nextSetBit(i+1)){
      if (!first){
        sb.append(',');
      } else {
        first = false;
      }
      sb.append(i);
    }

    sb.append('}');

    return sb.toString();
  }

  //--- IntSet interface
  
    
  @Override
  public boolean add(int i) {
    if (get(i)) {
      return false;
    } else {
      set(i);
      return true;
    }
  }

  @Override
  public boolean remove(int i) {
    if (get(i)) {
      clear(i);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean contains(int i) {
    return get(i);
  }

  @Override
  public IntIterator intIterator() {
    return new SetBitIterator();
  }

}
