package freemind.swing

import java.util.*
import javax.swing.AbstractListModel

class DefaultListModel<E> : AbstractListModel<E>() {
    private val delegate = Vector<E>()

    /**
     * Returns the number of components in this list.
     *
     *
     * This method is identical to `size`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
     * This method exists in conjunction with `setSize` so that
     * `size` is identifiable as a JavaBean property.
     *
     * @return  the number of components in this list
     * @see .size
     */
    override fun getSize(): Int {
        return delegate.size
    }

    /**
     * Returns the component at the specified index.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `get(int)`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @exception  ArrayIndexOutOfBoundsException  if the `index`
     * is negative or greater than the current size of this
     * list
     * @see .get
     */
    override fun getElementAt(index: Int): E {
        return delegate.elementAt(index)
    }

    /**
     * Copies the components of this list into the specified array.
     * The array must be big enough to hold all the objects in this list,
     * else an `IndexOutOfBoundsException` is thrown.
     *
     * @param   anArray   the array into which the components get copied
     * @see Vector.copyInto
     */
    fun copyInto(anArray: Array<Any?>?) {
        delegate.copyInto(anArray)
    }

    /**
     * Trims the capacity of this list to be the list's current size.
     *
     * @see Vector.trimToSize
     */
    fun trimToSize() {
        delegate.trimToSize()
    }

    /**
     * Increases the capacity of this list, if necessary, to ensure
     * that it can hold at least the number of components specified by
     * the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity
     * @see Vector.ensureCapacity
     */
    fun ensureCapacity(minCapacity: Int) {
        delegate.ensureCapacity(minCapacity)
    }

    /**
     * Sets the size of this list.
     *
     * @param   newSize   the new size of this list
     * @see Vector.setSize
     */
    fun setSize(newSize: Int) {
        val oldSize = delegate.size
        delegate.setSize(newSize)
        if (oldSize > newSize) {
            fireIntervalRemoved(this, newSize, oldSize - 1)
        } else if (oldSize < newSize) {
            fireIntervalAdded(this, oldSize, newSize - 1)
        }
    }

    /**
     * Returns the current capacity of this list.
     *
     * @return  the current capacity
     * @see Vector.capacity
     */
    fun capacity(): Int {
        return delegate.capacity()
    }

    /**
     * Returns the number of components in this list.
     *
     * @return  the number of components in this list
     * @see Vector.size
     */
    fun size(): Int {
        return delegate.size
    }

    /**
     * Tests whether this list has any components.
     *
     * @return  `true` if and only if this list has
     * no components, that is, its size is zero;
     * `false` otherwise
     * @see Vector.isEmpty
     */
    val isEmpty: Boolean
        get() = delegate.isEmpty()

    /**
     * Returns an enumeration of the components of this list.
     *
     * @return  an enumeration of the components of this list
     * @see Vector.elements
     */
    fun elements(): Enumeration<E> {
        return delegate.elements()
    }

    /**
     * Tests whether the specified object is a component in this list.
     *
     * @param   elem   an object
     * @return  `true` if the specified object
     * is the same as a component in this list
     * @see Vector.contains
     */
    operator fun contains(elem: Any?): Boolean {
        return delegate.contains(elem)
    }

    /**
     * Searches for the first occurrence of `elem`.
     *
     * @param   elem   an object
     * @return  the index of the first occurrence of the argument in this
     * list; returns `-1` if the object is not found
     * @see Vector.indexOf
     */
    fun indexOf(elem: Any?): Int {
        return delegate.indexOf(elem)
    }

    /**
     * Searches for the first occurrence of `elem`, beginning
     * the search at `index`.
     *
     * @param   elem    an desired component
     * @param   index   the index from which to begin searching
     * @return  the index where the first occurrence of `elem`
     * is found after `index`; returns `-1`
     * if the `elem` is not found in the list
     * @see Vector.indexOf
     */
    fun indexOf(elem: Any?, index: Int): Int {
        return delegate.indexOf(elem, index)
    }

    /**
     * Returns the index of the last occurrence of `elem`.
     *
     * @param   elem   the desired component
     * @return  the index of the last occurrence of `elem`
     * in the list; returns `-1` if the object is not found
     * @see Vector.lastIndexOf
     */
    fun lastIndexOf(elem: Any?): Int {
        return delegate.lastIndexOf(elem)
    }

    /**
     * Searches backwards for `elem`, starting from the
     * specified index, and returns an index to it.
     *
     * @param  elem    the desired component
     * @param  index   the index to start searching from
     * @return the index of the last occurrence of the `elem`
     * in this list at position less than `index`;
     * returns `-1` if the object is not found
     * @see Vector.lastIndexOf
     */
    fun lastIndexOf(elem: Any?, index: Int): Int {
        return delegate.lastIndexOf(elem, index)
    }

    /**
     * Returns the component at the specified index.
     * Throws an `ArrayIndexOutOfBoundsException` if the index
     * is negative or not less than the size of the list.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `get(int)`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     *
     * @param      index   an index into this list
     * @return     the component at the specified index
     * @see .get
     * @see Vector.elementAt
     */
    fun elementAt(index: Int): E {
        return delegate.elementAt(index)
    }

    /**
     * Returns the first component of this list.
     * Throws a `NoSuchElementException` if this
     * vector has no components.
     * @return     the first component of this list
     * @see Vector.firstElement
     */
    fun firstElement(): E {
        return delegate.firstElement()
    }

    /**
     * Returns the last component of the list.
     * Throws a `NoSuchElementException` if this vector
     * has no components.
     *
     * @return  the last component of the list
     * @see Vector.lastElement
     */
    fun lastElement(): E {
        return delegate.lastElement()
    }

    /**
     * Sets the component at the specified `index` of this
     * list to be the specified element. The previous component at that
     * position is discarded.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException` if the index
     * is invalid.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `set(int,Object)`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     *
     * @param      element what the component is to be set to
     * @param      index   the specified index
     * @see .set
     * @see Vector.setElementAt
     */
    fun setElementAt(element: E, index: Int) {
        delegate.setElementAt(element, index)
        fireContentsChanged(this, index, index)
    }

    /**
     * Deletes the component at the specified index.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException` if the index
     * is invalid.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `remove(int)`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     *
     * @param      index   the index of the object to remove
     * @see .remove
     * @see Vector.removeElementAt
     */
    fun removeElementAt(index: Int) {
        delegate.removeElementAt(index)
        fireIntervalRemoved(this, index, index)
    }

    /**
     * Inserts the specified element as a component in this list at the
     * specified `index`.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException` if the index
     * is invalid.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `add(int,Object)`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     *
     * @param      element the component to insert
     * @param      index   where to insert the new component
     * @exception  ArrayIndexOutOfBoundsException  if the index was invalid
     * @see .add
     * @see Vector.insertElementAt
     */
    fun insertElementAt(element: E, index: Int) {
        delegate.insertElementAt(element, index)
        fireIntervalAdded(this, index, index)
    }

    /**
     * Adds the specified component to the end of this list.
     *
     * @param   element   the component to be added
     * @see Vector.addElement
     */
    fun addElement(element: E) {
        val index = delegate.size
        delegate.addElement(element)
        fireIntervalAdded(this, index, index)
    }

    /**
     * Removes the first (lowest-indexed) occurrence of the argument
     * from this list.
     *
     * @param   obj   the component to be removed
     * @return  `true` if the argument was a component of this
     * list; `false` otherwise
     * @see Vector.removeElement
     */
    fun removeElement(obj: Any?): Boolean {
        val index = indexOf(obj)
        val rv = delegate.removeElement(obj)
        if (index >= 0) {
            fireIntervalRemoved(this, index, index)
        }
        return rv
    }

    /**
     * Removes all components from this list and sets its size to zero.
     * <blockquote>
     * **Note:** Although this method is not deprecated, the preferred
     * method to use is `clear`, which implements the
     * `List` interface defined in the 1.2 Collections framework.
    </blockquote> *
     *
     * @see .clear
     * @see Vector.removeAllElements
     */
    fun removeAllElements() {
        val index1 = delegate.size - 1
        delegate.removeAllElements()
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1)
        }
    }

    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    override fun toString(): String {
        return delegate.toString()
    }
    /* The remaining methods are included for compatibility with the
     * Java 2 platform Vector class.
     */
    /**
     * Returns an array containing all of the elements in this list in the
     * correct order.
     *
     * @return an array containing the elements of the list
     * @see Vector.toArray
     */
    fun toArray(): Array<Any?> {
        val rv = arrayOfNulls<Any>(delegate.size)
        delegate.copyInto(rv)
        return rv
    }

    /**
     * Returns the element at the specified position in this list.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException`
     * if the index is out of range
     * (`index < 0 || index >= size()`).
     *
     * @param index index of element to return
     */
    operator fun get(index: Int): E {
        return delegate.elementAt(index)
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException`
     * if the index is out of range
     * (`index < 0 || index >= size()`).
     *
     * @param index index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    operator fun set(index: Int, element: E): E {
        val rv = delegate.elementAt(index)
        delegate.setElementAt(element, index)
        fireContentsChanged(this, index, index)
        return rv
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException` if the
     * index is out of range
     * (`index < 0 || index > size()`).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    fun add(index: Int, element: E) {
        delegate.insertElementAt(element, index)
        fireIntervalAdded(this, index, index)
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException`
     * if the index is out of range
     * (`index < 0 || index >= size()`).
     *
     * @param index the index of the element to removed
     * @return the element previously at the specified position
     */
    fun remove(index: Int): E {
        val rv = delegate.elementAt(index)
        delegate.removeElementAt(index)
        fireIntervalRemoved(this, index, index)
        return rv
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns (unless it throws an exception).
     */
    fun clear() {
        val index1 = delegate.size - 1
        delegate.removeAllElements()
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1)
        }
    }

    /**
     * Deletes the components at the specified range of indexes.
     * The removal is inclusive, so specifying a range of (1,5)
     * removes the component at index 1 and the component at index 5,
     * as well as all components in between.
     *
     *
     * Throws an `ArrayIndexOutOfBoundsException`
     * if the index was invalid.
     * Throws an `IllegalArgumentException` if
     * `fromIndex > toIndex`.
     *
     * @param      fromIndex the index of the lower end of the range
     * @param      toIndex   the index of the upper end of the range
     * @see .remove
     */
    fun removeRange(fromIndex: Int, toIndex: Int) {
        require(fromIndex <= toIndex) { "fromIndex must be <= toIndex" }
        for (i in toIndex downTo fromIndex) {
            delegate.removeElementAt(i)
        }
        fireIntervalRemoved(this, fromIndex, toIndex)
    }

    fun addAll(c: Collection<E>?) {
        delegate.addAll(c!!)
    }

    fun addAll(index: Int, c: Collection<E>?) {
        delegate.addAll(index, c!!)
    }

    fun unmodifiableList(): List<E> {
        return Collections.unmodifiableList(delegate)
    }
}